package de.hswt.fi.fileimport.service.xml.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Location {

	@JacksonXmlProperty(isAttribute = true)
	private Double m;

	@JacksonXmlProperty(isAttribute = true)
	private Double rt;
	
	@JacksonXmlProperty(isAttribute = true)
	private Double y;

	public Double getM() {
		return m;
	}

	public void setM(Double m) {
		this.m = m;
	}

	public Double getRt() {
		return rt;
	}

	public void setRt(Double rt) {
		this.rt = rt;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Location [m=");
		builder.append(m);
		builder.append(", rt=");
		builder.append(rt);
		builder.append(", y=");
		builder.append(y);
		builder.append("]");
		return builder.toString();
	}

}
