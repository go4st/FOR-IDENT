package de.hswt.fi.search.service.index.model;

import de.hswt.fi.model.FeatureSet;

import java.io.Serializable;

public class IndexJob implements Serializable {

	private static final long serialVersionUID = 1L;

	private IndexSettings settings;
	
	private FeatureSet featureSet;

	public IndexJob(IndexSettings settings, FeatureSet featureSet) {
		this.settings = settings;
		this.featureSet = featureSet;
	}

	public FeatureSet getFeatureSet() {
		return featureSet;
	}

	public void setFeatureSet(FeatureSet featureSet) {
		this.featureSet = featureSet;
	}

	public IndexSettings getSettings() {
		return settings;
	}

}
