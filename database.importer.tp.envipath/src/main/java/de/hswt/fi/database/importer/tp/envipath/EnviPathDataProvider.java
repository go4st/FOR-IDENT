package de.hswt.fi.database.importer.tp.envipath;

import de.hswt.fi.calculation.service.api.CalculationService;
import de.hswt.fi.database.importer.tp.api.TransformationProductDataProvider;
import de.hswt.fi.database.importer.tp.envipath.model.EnviCompoundPair;
import de.hswt.fi.database.importer.tp.envipath.model.EnviNode;
import de.hswt.fi.database.importer.tp.envipath.model.EnviPathway;
import de.hswt.fi.database.importer.tp.envipath.model.Parameter;
import de.hswt.fi.search.service.tp.model.Compound;
import de.hswt.fi.search.service.tp.model.Pathway;
import de.hswt.fi.search.service.tp.model.Transformation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Class to query and parse pathways from envipath.org. Some project internal
 * model classes are used, to make the parsing a little bit more readable.
 *
 * The rest interface of envipath.org is plain text based.
 *
 * @see de.hswt.fi.database.importer.tp.envipath.model.Parameter
 * @see de.hswt.fi.database.importer.tp.envipath.model.EnviPathway
 * @see de.hswt.fi.database.importer.tp.envipath.model.EnviNode
 *
 * @author Marco Luthardt
 */
/*
 * The interface of envipath.org is not optimal, so it is hard to get the right
 * parsing. The methods to parse the results from get queries seems to be not
 * much robust.
 *
 * Maybe in the future it is possible, to use a json format, when it's provided
 * from envipath.org
 *
 * TODO improve the parsing and make it more robust
 */
@Component
public class EnviPathDataProvider implements TransformationProductDataProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnviPathDataProvider.class);

	private static final String URL_PATHWAY = "https://envipath.org/package/{package}/pathway/{pathway}";

	private static final String URL_PATHWAY_LIST = "https://envipath.org/package/{package}/pathway";

	private static final String URL_POSTFIX_NODE = "/node";

	private static final String URL_POSTFIX_EDGE = "/edge";

	private CalculationService calculationService;

	private List<String> pathwaysWithError;

	private RestOperations restTemplate;

	@Autowired
	public EnviPathDataProvider(CalculationService calculationService) {
		this.calculationService = calculationService;
		pathwaysWithError = new ArrayList<>();
		restTemplate = new RestTemplate();
	}

	@Override
	public List<Pathway> getPathways() {

		LOGGER.debug("start reading envipath.org");

		List<String> packageIds = new ArrayList<>();
		// TODO add more enviPath packages when available
		packageIds.add("32de3cf4-e3e6-4168-956e-32fa5ddb0ce1");

		List<Pathway> pathways = getPathways(packageIds);

		LOGGER.debug("found compounds: {}", pathways.size());
		LOGGER.debug("error compounds: {}", pathwaysWithError.size());
		pathwaysWithError.forEach(LOGGER::error);

		return pathways;
	}

	/**
	 * Get a list of pathway root compounds. The compounds should include all
	 * compounds, related to the requested packages.
	 *
	 * @param packageIds
	 *            package ids to get the pathways from
	 *
	 * @return list of all compounds (root nodes) contained in the packages,
	 *         never null
	 */
	private List<Pathway> getPathways(List<String> packageIds) {
		List<Pathway> pathways = new ArrayList<>();

		LOGGER.debug("searching for compounds in {} packages", pathways.size());

		for (String packageId : packageIds) {
			List<Pathway> pathwaysOfPackage = getPathways(packageId);
			LOGGER.debug("found {} pathways for package {}", pathwaysOfPackage.size(), packageId);
			pathways.addAll(pathwaysOfPackage);
		}
		return pathways;
	}

	/**
	 * Get a list of pathway root compounds. The compounds should include all
	 * compounds, related to the requested package.
	 *
	 * @param packageId
	 *            package id to get the pathways from
	 *
	 * @return list of all compounds (root nodes) contained in the package,
	 *         never null
	 */
	private List<Pathway> getPathways(String packageId) {
		LOGGER.debug("searching for pathways in package {}", packageId);

		Parameter parameter = new Parameter();
		parameter.setPackageId(packageId);

		List<String> lines = executeRestQueryForString(URL_PATHWAY_LIST, parameter);
		List<EnviPathway> pathways = parsePathways(lines);

		LOGGER.debug("found {} pathways in package {}", pathways.size(), packageId);

		return getPathwaysAsync(pathways, parameter);
	}

	/**
	 * Retrieves a list of pathways in an asynchronous way.
	 *
	 * @param pathways
	 *            the pathways to retrieve from enviPath
	 * @param parentParameter
	 *            parameter to derive local parameters for each pathway
	 * @return a list of the retrieved pathways
	 */
	private List<Pathway> getPathwaysAsync(List<EnviPathway> pathways, Parameter parentParameter) {
		List<Pathway> compounds = new ArrayList<>();

		ExecutorService executor = Executors.newCachedThreadPool();

		List<FutureTask<Optional<Pathway>>> pathwayTasks = new ArrayList<>();

		for (EnviPathway pathway : pathways) {
			Parameter parameter = new Parameter(parentParameter);
			parameter.setPathwayId(pathway.getId());

			FutureTask<Optional<Pathway>> task = new FutureTask<>(
					() -> getPathway(pathway, parameter));
			pathwayTasks.add(task);
			executor.execute(task);
		}

		int i = 0;
		for (FutureTask<Optional<Pathway>> task : pathwayTasks) {
			Optional<Pathway> pathway = Optional.empty();
			LOGGER.debug("waiting for pathway number {}", i++);
			try {
				pathway = task.get();
			} catch (InterruptedException e) {
				LOGGER.info("Unable to get read pathway.", e.getCause());
				Thread.currentThread().interrupt();
			} catch (ExecutionException e) {
				LOGGER.info("Unable to get read pathway.", e.getCause());
			}
			pathway.ifPresent(compounds::add);
		}

		executor.shutdown();

		return compounds;
	}

	/**
	 * Parse the lines of a result request of getting all pathways of a package.
	 *
	 * @param lines
	 *            the result lines from the request
	 *
	 * @return list all parsed pathways, never null
	 */
	private List<EnviPathway> parsePathways(List<String> lines) {
		List<EnviPathway> pathways = new ArrayList<>();

		for (String line : lines) {
			// hard to detect if line is valid, not nice, but should work
			if (!line.contains(", https://envipath.org/package/")) {
				continue;
			}
			pathways.add(parsePathway(line));
		}

		return pathways;
	}

	/**
	 * Parse the information of a line, which defines a pathway
	 *
	 * @param line
	 *            the line, contains information about a pathway
	 * @return the parsed pathway, never null
	 */
	private EnviPathway parsePathway(String line) {
		EnviPathway pathway = new EnviPathway();

		pathway.setId(StringUtils.substringAfterLast(line, "/pathway/"));
		pathway.setName(StringUtils.substringBefore(line, ", https://"));

		return pathway;
	}

	/**
	 * Get a pathway, represented by the root compound (root node).
	 *
	 * @param pathway
	 *            the information about the pathway to create
	 * @param parameter
	 *            the REST request parameter
	 *
	 * @return the root compound of the requested pathway
	 */
	private Optional<Pathway> getPathway(EnviPathway pathway, Parameter parameter) {
		LOGGER.debug("read pathway {} in package {}", pathway.getName(), parameter.getPackageId());

		Map<String, EnviNode> nodes = getNodes(parameter);
		LOGGER.debug("found {} nodes for pathway {}", nodes.size(), pathway.getName());

		Map<String, Transformation> transformations = getTransformations(parameter);
		LOGGER.debug("found {} transformations for pathway {}", transformations.size(),
				pathway.getName());

		Optional<Compound> compound = getCompound(pathway, parameter, nodes, transformations);
		Pathway p = null;
		if (compound.isPresent()) {
			p = new Pathway();
			p.setRoot(compound.get());
		}

		return Optional.ofNullable(p);
	}

	/*
	 * ----- Reading nodes from node urls -----
	 */
	/**
	 * Get the information about the nodes of a certain pathway, which is
	 * defined with the parameter.
	 *
	 * @param parameter
	 *            the parameter for the REST request
	 * @return list of nodes, containing all relevant infromations about a node,
	 *         never null
	 */
	private Map<String, EnviNode> getNodes(Parameter parameter) {
		Map<String, EnviNode> nodes = new HashMap<>();

		List<String> lines = executeRestQueryForString(URL_PATHWAY + URL_POSTFIX_NODE, parameter);

		int postLineCount = 4;

		for (int i = 1; i < lines.size() - postLineCount; i += 2) {
			EnviNode node = getNode(lines.get(i));
			if (node != null) {
				nodes.put(node.getSmiles(), node);
			}
		}

		return nodes;
	}

	/**
	 * Read a single node, read from the given link.
	 *
	 * @param link
	 *            the link to the node which is used to execute a REST request
	 *
	 * @return the parsed node, or null if node could not be found.
	 */
	private EnviNode getNode(String link) {

		EnviNode node;

		List<String> lines = executeRestQueryForString(link);

		if (lines.size() < 3) {
			return null;
		}

		node = new EnviNode();
		node.setName(lines.get(1));
		node.setSmiles(lines.get(2));
		if (lines.get(4).startsWith("http")) {
			node.setLink(lines.get(4));
		}
		if (!lines.get(6).startsWith("The node")) {
			node.setAdditionalInformation(lines.get(6));
		}

		return node;
	}

	/*
	 * ----- read reactions or transformations (edges) -----
	 */
	/**
	 * Get informations about the reactions (edges) of a given pathway, defined
	 * through the parameter.
	 *
	 * @param parameter
	 *            the parameter for the REST request
	 * @return a map with all transformations, with the link of start and end
	 *         node concatenated as id
	 */
	private Map<String, Transformation> getTransformations(Parameter parameter) {
		Map<String, Transformation> transformations = new HashMap<>();

		List<String> lines = executeRestQueryForString(URL_PATHWAY + URL_POSTFIX_EDGE, parameter);

		int postLineCount = 4;

		for (int i = 1; i < lines.size() - postLineCount; i += 2) {
			Transformation transformation = new Transformation();
			transformation.setReactionLink(lines.get(i));
			transformations.putAll(parseTransformation(transformation));
		}

		return transformations;
	}

	/**
	 * Update the transformation with informations from the transformation link.
	 *
	 * @param transformation
	 *            the transformation to update
	 *
	 * @return an id with the link of start and end node concatenated
	 */
	private Map<String, Transformation> parseTransformation(Transformation transformation) {
		List<String> lines = executeRestQueryForString(transformation.getReactionLink());
		transformation.setReaction(lines.get(1));

		// a transformation can have more start and end nodes
		// e.g. a compound can be transformed into 2 or more products
		// by one reaction: compound => product1 + product2
		//
		// find no suitable way to map this, so in this case, there must
		// be one single relation to each product, having the same reaction
		// id, to indicate their relationship

		Map<String, Transformation> transformations = new HashMap<>();
		List<String> startNodes = new ArrayList<>();
		List<String> endNodes = new ArrayList<>();

		for (String line : lines) {
			if (line.startsWith("Start Node URI: ")) {
				startNodes.add(StringUtils.substringAfter(line, "Start Node URI: "));
			} else if (line.startsWith("End Node URI: ")) {
				endNodes.add(StringUtils.substringAfter(line, "End Node URI: "));
			} else if (line.startsWith("The link to the reaction is: ")) {
				transformation.setReactionId(StringUtils.substringAfter(line, "/reaction/"));
			}
		}

		for (String startNode : startNodes) {
			for (String endNode : endNodes) {
				transformations.put(startNode + endNode, new Transformation(transformation));
			}
		}

		return transformations;
	}

	/*
	 * ----- compounds -----
	 */
	/**
	 * Create the pathway with compounds and transformations, represented by the
	 * root compound (root node).
	 *
	 * @param pathway
	 *            definition of the pathway
	 * @param parameter
	 *            parameter for a REST request
	 * @param nodes
	 *            the informations of all possible existing nodes (compounds)
	 * @param transformations
	 *            the informations of all possible existing edges
	 *            (transformations)
	 * @return
	 */
	private Optional<Compound> getCompound(EnviPathway pathway, Parameter parameter, Map<String, EnviNode> nodes, Map<String, Transformation> transformations) {
		Map<String, Compound> compounds = new HashMap<>();

		List<EnviCompoundPair> pairs = getCompoundPairs(parameter);

		LOGGER.debug("found {} edges for pathway {}", pairs.size(), pathway.getName());

		for (EnviCompoundPair pair : pairs) {
			String compoundSmiles = pair.getStartSmiles();
			String transformationProductSmiles = pair.getEndSmiles();
			// create start node, when needed
			Compound compound = getCompound(compounds, nodes, compoundSmiles);

			// create end node, when needed
			Compound transformationProduct = getCompound(compounds, nodes,
					transformationProductSmiles);

			// find and update an edge, make the link between two compounds
			Transformation transformation = transformations
					.get(compound.getLink() + transformationProduct.getLink());

			if (transformation == null) {
				LOGGER.debug("problem while reading pathway {} with id {}", pathway.getName(), pathway.getId());
				pathwaysWithError.add(pathway.getId());
				return Optional.empty();
			}

			transformation.setCompound(compound);
			transformation.setTransformationProduct(transformationProduct);

			compound.addTransformation(transformation);
		}

		// get the root compound depending, node with only outgoing
		// transformations (edges)
		Optional<Compound> rootCompound = compounds.values().stream()
				.filter(c -> c.getTransformations() != null && c.getTransformations().stream()
						.anyMatch(t -> t.getTransformationProduct() != null))
				.findFirst();

		// mark root node
		rootCompound.ifPresent(compound -> compound.setRoot(true));

		return rootCompound;
	}

	/**
	 * Request the pathway and create the structure of the graph out of it. Each
	 * edge will be represented as pair of smiles values.
	 *
	 * @param parameter
	 *            the parameter for the REST request
	 *
	 * @return list of compound pairs, each pair represents a edge, the sum of
	 *         all pairs define the structure of the pathway graph
	 */
	private List<EnviCompoundPair> getCompoundPairs(Parameter parameter) {
		List<EnviCompoundPair> pairs = new ArrayList<>();
		List<String> lines = executeRestQueryForString(URL_PATHWAY, parameter);

		if (lines.size() < 5) {
			return pairs;
		}

		String startSmiles = "";
		String endSmiles;
		for (int i = 3; i < lines.size() - 1; i++) {
			if (lines.get(i).isEmpty() || lines.get(i + 1).isEmpty()) {
				break;
			}

			if (lines.get(i + 2).startsWith(" ")) {
				i++;
			}
			if (lines.get(i + 1).startsWith(" ")) {
				startSmiles = lines.get(i);
			}
			endSmiles = parseSmiles(lines.get(i + 1));
			pairs.add(new EnviCompoundPair(startSmiles, endSmiles));
		}

		return pairs;
	}

	/**
	 * Get a compound out of caching map based on the smiles or create a new
	 * compound and initialize it.
	 *
	 * @param compoundMap
	 *            caching map with compounds and their smiles as key
	 * @param nodes
	 *            map of nodes to get data for initialization with smiles as key
	 * @param smiles
	 *            the smiles to get a compound for
	 * @return the found or newly created compound
	 */
	private Compound getCompound(Map<String, Compound> compoundMap, Map<String, EnviNode> nodes, String smiles) {
		Compound compound;
		if (compoundMap.containsKey(smiles)) {
			compound = compoundMap.get(smiles);
		} else {
			compound = new Compound();
			compound.setSmiles(smiles);
			initCompound(compound, nodes);
			compoundMap.put(smiles, compound);
		}
		return compound;
	}

	/**
	 * Update a compound with informations from a given node (identified via
	 * smiles).
	 *
	 * @param compound
	 *            the compound to update
	 * @param nodes
	 *            the list of nodes to search for the node the compound belongs
	 *            to
	 */
	private void initCompound(Compound compound, Map<String, EnviNode> nodes) {
		if (!nodes.containsKey(compound.getSmiles())) {
			return;
		}
		EnviNode node = nodes.get(compound.getSmiles());
		compound.setName(node.getName());
		compound.setLink(node.getLink());
		compound.setAdditionalInformation(node.getAdditionalInformation());

		compound.setInChi(calculationService.getInChi(compound.getSmiles()));
		compound.setInChiKey(calculationService.getInChiKey(compound.getSmiles()));
		compound.setFormula(calculationService.getMolecularFormula(compound.getSmiles()));
		compound.setNeutralMass(calculationService.getMassFromFormula(compound.getFormula()));
	}

	private String parseSmiles(String line) {
		String smiles = "";

		if (line.startsWith(" no rule associated")) {
			smiles = line.replace(" no rule associated", "");
		} else if (line.contains(") ")) {
			int fromIndex = line.lastIndexOf(") ");
			if (fromIndex >= 0) {
				smiles = line.substring(fromIndex + 1, line.length());
			}
		} else {
			smiles = line;
		}

		return smiles.trim();
	}

	private List<String> executeRestQueryForString(String url) {
		return executeRestQueryForString(url, null);
	}

	private List<String> executeRestQueryForString(String url, Parameter parameter) {
		String result = "";

		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class,
				getParameterMap(parameter));
		if (response.getStatusCode() == HttpStatus.OK) {
			result = response.getBody();
		} else {
			LOGGER.debug("HTTP REST Request failed with status code {}", response.getStatusCode());
		}
		return Arrays.asList(result.split("\n"));
	}

	private Map<String, String> getParameterMap(Parameter parameter) {
		Map<String, String> params = new HashMap<>();

		if (parameter != null) {
			params.put("package", parameter.getPackageId());
			params.put("pathway", parameter.getPathwayId());
		}

		return params;
	}

}
