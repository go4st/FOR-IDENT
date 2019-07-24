package de.hswt.fi.search.service.tp.model;

import de.hswt.fi.model.FeatureSet;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class TransformationProductJob implements Serializable {

	private static final long serialVersionUID = 1L;

		private TransformationProductSettings settings;

	private List<String> inChiKeys;

	private FeatureSet featureSet;

	public TransformationProductJob(TransformationProductSettings settings, List<String> inChiKeys,
			FeatureSet featureSet) {
		Objects.requireNonNull(settings, "Parmater settings must not be null.");
		Objects.requireNonNull(inChiKeys, "Parmater inChiKeys must not be null.");
		Objects.requireNonNull(featureSet, "Parmater featureSet must not be null.");

		this.settings = settings;
		this.inChiKeys = inChiKeys;
		this.featureSet = featureSet;
	}

	public TransformationProductSettings getSettings() {
		return settings;
	}

	public void setSettings(TransformationProductSettings settings) {
		this.settings = settings;
	}

	public List<String> getInChiKeys() {
		return inChiKeys;
	}

	public void setInChiKeys(List<String> inChiKeys) {
		this.inChiKeys = inChiKeys;
	}

	public FeatureSet getFeatureSet() {
		return featureSet;
	}

	public void setFeatureSet(FeatureSet featureSet) {
		this.featureSet = featureSet;
	}
}
