package de.hswt.fi.processing.service.model;

import de.hswt.fi.model.FeatureSet;
import de.hswt.fi.search.service.search.api.CompoundSearchService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProcessingJob {

	private ProcessingSettings settings;

	private FeatureSet featureSet;

	private List<ProcessingUnit> requestedProcessUnits;

	private List<CompoundSearchService> selectedSearchServices;

	public ProcessingJob() {
	}

	public ProcessingJob(FeatureSet featureSet) {
		this(null, featureSet);
	}

	public ProcessingJob(ProcessingSettings settings, FeatureSet featureSet) {
		Objects.requireNonNull(featureSet, "Parameter must not be null.");

		this.settings = settings != null ? settings : new ProcessingSettings();
		this.featureSet = featureSet;
		requestedProcessUnits = new ArrayList<>();
	}

	public String getName() {
		return featureSet.getName();
	}

	public FeatureSet getFeatureSet() {
		return featureSet;
	}

	public ProcessingSettings getSettings() {
		return settings;
	}

	public void setSettings(ProcessingSettings settings) {
		this.settings = settings;
	}

	public List<ProcessingUnit> getRequestedProcessUnits() {
		return requestedProcessUnits;
	}

	public void setRequestedProcessUnits(List<ProcessingUnit> requestedProcessUnits) {
		this.requestedProcessUnits = requestedProcessUnits;
	}

	public void setSelectedSearchServices(List<CompoundSearchService> selectedSearchServices) {
		this.selectedSearchServices = selectedSearchServices;
	}

	public List<CompoundSearchService> getSelectedSearchServices() {
		return selectedSearchServices;
	}

	public int getDataSize() {
		return featureSet.getFeatures().size();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((featureSet == null) ? 0 : featureSet.hashCode());
		result = prime * result
				+ ((requestedProcessUnits == null) ? 0 : requestedProcessUnits.hashCode());
		result = prime * result + ((settings == null) ? 0 : settings.hashCode());
		result = prime * result + ((selectedSearchServices == null) ? 0 : selectedSearchServices.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ProcessingJob other = (ProcessingJob) obj;
		if (featureSet == null) {
			if (other.featureSet != null) {
				return false;
			}
		} else if (!featureSet.equals(other.featureSet)) {
			return false;
		}
		if (requestedProcessUnits == null) {
			if (other.requestedProcessUnits != null) {
				return false;
			}
		} else if (!requestedProcessUnits.equals(other.requestedProcessUnits)) {
			return false;
		}
		if (settings == null) {
			if (other.settings != null) {
				return false;
			}
		} else if (!settings.equals(other.settings)) {
			return false;
		}
		if (selectedSearchServices == null) {
			if (other.selectedSearchServices != null) {
				return false;
			}
		} else if (!selectedSearchServices.equals(other.selectedSearchServices)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("ProcessJob [");
		if (settings != null) {
			builder.append("settings=");
			builder.append(settings);
			builder.append(", ");
		}
		if (featureSet != null) {
			builder.append("featureSet=");
			builder.append(featureSet);
			builder.append(", ");
		}
		if (requestedProcessUnits != null) {
			builder.append("requestedProcessUnits=");
			builder.append(requestedProcessUnits.subList(0, Math.min(requestedProcessUnits.size(), maxLen)));
			builder.append(", ");
		}
		if (selectedSearchServices != null) {
			builder.append("sourceLists=");
			builder.append(selectedSearchServices.subList(0, Math.min(selectedSearchServices.size(), maxLen)));
		}
		builder.append("]");
		return builder.toString();
	}
}
