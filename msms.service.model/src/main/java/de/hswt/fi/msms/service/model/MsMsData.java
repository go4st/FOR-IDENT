package de.hswt.fi.msms.service.model;

import de.hswt.fi.model.Feature;

import java.util.List;

public class MsMsData {

	private Feature feature;

	private List<String> candidatesSmiles;

	public MsMsData(Feature feature, List<String> candidatesSmiles) {
		this.feature = feature;
		this.candidatesSmiles = candidatesSmiles;
	}

	public Feature getFeature() {
		return feature;
	}

	public List<String> getCandidatesSmiles() {
		return candidatesSmiles;
	}
}
