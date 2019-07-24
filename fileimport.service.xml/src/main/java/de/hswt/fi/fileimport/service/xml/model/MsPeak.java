package de.hswt.fi.fileimport.service.xml.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class MsPeak {
	
	@JacksonXmlProperty(isAttribute = true)
	private double x;
	
	@JacksonXmlProperty(isAttribute = true)
	private double y;
	

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MsPeak [x=");
		builder.append(x);
		builder.append(", y=");
		builder.append(y);
		builder.append("]");
		return builder.toString();
	}
	
	

}
