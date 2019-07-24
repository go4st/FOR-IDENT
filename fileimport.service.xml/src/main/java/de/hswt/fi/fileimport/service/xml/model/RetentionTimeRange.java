package de.hswt.fi.fileimport.service.xml.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class RetentionTimeRange {
	
	@JacksonXmlProperty(isAttribute = true, localName = "min")
	private double minimum;
	
	@JacksonXmlProperty(isAttribute = true, localName = "max")
	private double maximum;

	public double getMinimum() {
		return minimum;
	}

	public void setMinimum(double minimum) {
		this.minimum = minimum;
	}

	public double getMaximum() {
		return maximum;
	}

	public void setMaximum(double maximum) {
		this.maximum = maximum;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RetentionTimeRange [minimum=");
		builder.append(minimum);
		builder.append(", maximum=");
		builder.append(maximum);
		builder.append("]");
		return builder.toString();
	}
	
}
