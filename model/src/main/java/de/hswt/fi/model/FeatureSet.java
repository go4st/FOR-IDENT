package de.hswt.fi.model;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.*;

public class FeatureSet implements Serializable {

	private static final long serialVersionUID = 1L;

	private Path sourceFilePath;
	
	private String name;

	private List<Feature> features;

	private List<RTICalibrationData> rtiCalibrationData;
	
	protected FeatureSet() {
		rtiCalibrationData = new ArrayList<>();
		features = Collections.emptyList();
	}

	public FeatureSet(Path sourceFilePath, List<Feature> features) {
		Objects.requireNonNull(sourceFilePath, "Parameter name must not be null.");
		Objects.requireNonNull(features, "Parameter features must not be null.");

		this.sourceFilePath = sourceFilePath;
		this.name = sourceFilePath.toFile().getName();
		this.features = features;
		rtiCalibrationData = new ArrayList<>();
	}

	public FeatureSet(String name, List<Feature> features) {
		Objects.requireNonNull(name, "Parameter name must not be null.");
		Objects.requireNonNull(features, "Parameter features must not be null.");

		this.name = name;
		this.features = features;
		rtiCalibrationData = new ArrayList<>();
	}

	public List<RTICalibrationData> getRtiCalibrationData() {
		return rtiCalibrationData;
	}

	public void setRtiCalibrationData(List<RTICalibrationData> rtiCalibrationData) {
		this.rtiCalibrationData = rtiCalibrationData;
	}

	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Path getSourceFilePath() {
		return sourceFilePath;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FeatureSet that = (FeatureSet) o;
		return Objects.equals(sourceFilePath, that.sourceFilePath) &&
				Objects.equals(name, that.name) &&
				Objects.equals(features, that.features) &&
				Objects.equals(rtiCalibrationData, that.rtiCalibrationData);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sourceFilePath, name, features, rtiCalibrationData);
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("FeatureSet [");
		if (name != null) {
			builder.append("name=");
			builder.append(name);
			builder.append(", ");
		}
		if (features != null) {
			builder.append("features=");
			builder.append(toString(features, maxLen));
			builder.append(", ");
		}
		if (rtiCalibrationData != null) {
			builder.append("rtiCalibrationData=");
			builder.append(toString(rtiCalibrationData, maxLen));
		}
		builder.append("]");
		return builder.toString();
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

}