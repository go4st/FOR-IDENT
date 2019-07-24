package de.hswt.fi.web.service.def.controller;

import de.hswt.fi.search.service.mass.search.model.Entry;
import de.hswt.fi.search.service.mass.search.model.WebserviceEntry;
import de.hswt.fi.search.service.search.api.CompoundSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = SubstanceController.PATH)
@SuppressWarnings("unused")
public class SubstanceController {

	private static final int DELTA_MASS_LIMIT = 200;

	private static final int PARAMETER_ID_LIMIT = 100;

	private static final Logger LOGGER = LoggerFactory.getLogger(SubstanceController.class);

	static final String PATH = "/api/substances";

	private List<CompoundSearchService> compoundSearchServices;

	@Autowired
	public SubstanceController(List<CompoundSearchService> compoundSearchServices) {
		this.compoundSearchServices = Objects.requireNonNull(compoundSearchServices, "StoffIdentSearchService must not be null");
	}

	private <T> Page<T> combinePages(Stream<Page<T>> pages) {
		List<T> entityStream = pages.map(Page::stream)
				.flatMap(Function.identity())
				.collect(Collectors.toList());

		return new PageImpl<>(entityStream);
	}

	@GetMapping
	public Iterable<Entry> findAll() {
		throw new UnsupportedOperationException();
	}

	@GetMapping(path = "/inchiKeys")
	public Page<String> findAllInchiKeys(Pageable pageable) {
		LOGGER.debug("entering method findAllInchiKeys");
		return combinePages(compoundSearchServices.stream()
				.map(compoundSearchService -> compoundSearchService.findAllInchiKeys(pageable)));
	}

	@GetMapping(path = "/ids")
	public Page<String> findAllPublicIds(Pageable pageable) {
		LOGGER.debug("entering method findAllPublicIDs");
		return combinePages(compoundSearchServices.stream()
				.map(compoundSearchService -> compoundSearchService.findAllPublicIDs(pageable)));
	}

	@GetMapping(path = "/inchiKeysAndIds")
	public Page<Object> findAllInchiKeysAndPublicIds(Pageable pageable) {
		LOGGER.debug("entering method findAllPublicIds");
		return combinePages(compoundSearchServices.stream()
				.map(compoundSearchService -> compoundSearchService.findAllInchiKeysAndPublicIDs(pageable)));
	}

	@GetMapping(params = {"elementalFormula"})
	public Page<WebserviceEntry> findByElementalFormula(@RequestParam String elementalFormula, Pageable pageable) {
		LOGGER.debug("entering method findByElementalFormula");
		return combinePages(compoundSearchServices.stream()
				.map(compoundSearchService -> compoundSearchService.findByFormula(elementalFormula, pageable)));
	}

	@GetMapping(params = {"accurateMassMin", "accurateMassMax"})
	public Page<WebserviceEntry> findByMass(@RequestParam Double accurateMassMin, @RequestParam Double accurateMassMax,
								  Pageable pageable) {

		LOGGER.debug("entering method findByMass");
		if (accurateMassMax - accurateMassMin > DELTA_MASS_LIMIT) {
			throw new InvalidParameterException("Difference between min and max is > " + DELTA_MASS_LIMIT);
		}
		return combinePages(compoundSearchServices.stream()
				.map(compoundSearchService -> compoundSearchService.findByAccurateMass(accurateMassMin, accurateMassMax, pageable)));
	}

	@GetMapping(params = {"id"})
	public Collection<WebserviceEntry> findByPublicID(@RequestParam List<String> id) {
		LOGGER.debug("entering method findByPublicID");
		if (id.size() > PARAMETER_ID_LIMIT) {
			throw new InvalidParameterException("To much requests");
		}

		Collection<WebserviceEntry> results = new ArrayList<>();
		for (String currentId : id) {
			compoundSearchServices.stream()
					.map(compoundSearchService -> compoundSearchService.findByPublicID(currentId))
					.filter(Optional::isPresent)
					.map(Optional::get)
					.findFirst()
					.ifPresent(results::add);
		}
		return results;
	}

	@GetMapping(params = "inchiKey")
	public Page<WebserviceEntry> findByInchiKey(@RequestParam String inchiKey) {
		List<WebserviceEntry> entries = compoundSearchServices.stream()
				.map(compoundSearchService -> compoundSearchService.findByInchiKeyValue(inchiKey))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList());

		return new PageImpl<>(entries);
	}
}
