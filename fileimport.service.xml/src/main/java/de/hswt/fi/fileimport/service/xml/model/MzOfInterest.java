package de.hswt.fi.fileimport.service.xml.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class MzOfInterest {

	@JacksonXmlProperty
	private double mz;

	public double getMz() {
		return mz;
	}

	public void setMz(double mz) {
		this.mz = mz;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MzOfInterest [mz=");
		builder.append(mz);
		builder.append("]");
		return builder.toString();
	}
	
}
