package de.hswt.fi.fileimport.service.xml.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Chromatogram {

	@JacksonXmlProperty
	private String type;

	@JacksonXmlProperty(isAttribute = true, localName = "cpdAlgo")
	private String peakDetectionAlgorithm;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPeakDetectionAlgorithm() {
		return peakDetectionAlgorithm;
	}

	public void setPeakDetectionAlgorithm(String peakDetectionAlgorithm) {
		this.peakDetectionAlgorithm = peakDetectionAlgorithm;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Chromatogram [");
		if (type != null) {
			builder.append("type=");
			builder.append(type);
			builder.append(", ");
		}
		if (peakDetectionAlgorithm != null) {
			builder.append("peakDetectionAlgorithm=");
			builder.append(peakDetectionAlgorithm);
		}
		builder.append("]");
		return builder.toString();
	}

}
