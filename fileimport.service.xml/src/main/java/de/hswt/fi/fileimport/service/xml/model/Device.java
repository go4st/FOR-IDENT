package de.hswt.fi.fileimport.service.xml.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Device {

	@JacksonXmlProperty(isAttribute = true)
	private String type;
	
	@JacksonXmlProperty(isAttribute = true, localName = "num")
	private int number;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Device [");
		if (type != null) {
			builder.append("type=");
			builder.append(type);
			builder.append(", ");
		}
		builder.append("number=");
		builder.append(number);
		builder.append("]");
		return builder.toString();
	}
	
}
