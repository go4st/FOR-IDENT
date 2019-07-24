package de.hswt.fi.search.service.mass.search.model;

import de.hswt.fi.model.FeatureSet;

import java.io.Serializable;

public class MassSearchJob implements Serializable {

	private static final long serialVersionUID = 1;

	private FileSearchSettings settings;

	private FeatureSet featureSet;

	public MassSearchJob(FileSearchSettings settings, FeatureSet featureSet) {
		this.settings = settings;
		this.featureSet = featureSet;
	}

	public FileSearchSettings getSettings() {
		return settings;
	}

	public void setSettings(FileSearchSettings settings) {
		this.settings = settings;
	}

	public FeatureSet getFeatureSet() {
		return featureSet;
	}

	public void setFeatureSet(FeatureSet featureSet) {
		this.featureSet = featureSet;
	}

	public boolean isEmpty() {
		return featureSet.getFeatures().isEmpty();
	}
}
