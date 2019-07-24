package de.hswt.fi.search.service.tp.model;

import de.hswt.fi.model.Feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathwayCandidate {

	private String inChiKey;

	private Map<String, Pathway> pathways;

	private List<Feature> explainedFeatures;

	public PathwayCandidate(String inChiKey) {
		this.inChiKey = inChiKey;
		pathways = new HashMap<>();
		explainedFeatures = new ArrayList<>();
	}

	public String getInChiKey() {
		return inChiKey;
	}

	public void setInChiKey(String inChiKey) {
		this.inChiKey = inChiKey;
	}

	public void addPathway(Pathway pathway) {
		if (pathway == null) {
			return;
		}
		if (!pathways.containsKey(pathway.getId())) {
			pathways.put(pathway.getId(), pathway);
		}
	}

	public Map<String, Pathway> getPathways() {
		return pathways;
	}

	public void addExplainedFeature(Feature explainedFeature) {
		if (explainedFeatures.contains(explainedFeature)) {
			return;
		}
		explainedFeatures.add(explainedFeature);
	}

	public List<Feature> getExplainedFeatures() {
		return explainedFeatures;
	}

	@Override
	public String toString() {
		return "PathwayCandidate{" + "inChiKey='" + inChiKey + '\'' + ", pathways=" + pathways
				+ ", explainedFeatures=" + explainedFeatures + '}';
	}
}
