package de.hswt.fi.fileimport.service.xml.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class MsDetails {

	@JacksonXmlProperty(isAttribute = true)
	private String scanType;

	@JacksonXmlProperty(isAttribute = true)
	private String is;

	@JacksonXmlProperty(isAttribute = true)
	private String p;

	@JacksonXmlProperty(isAttribute = true)
	private String ce;

	@JacksonXmlProperty(isAttribute = true)
	private String fv;

	public String getScanType() {
		return scanType;
	}

	public void setScanType(String scanType) {
		this.scanType = scanType;
	}

	public String getIs() {
		return is;
	}

	public void setIs(String is) {
		this.is = is;
	}

	public String getP() {
		return p;
	}

	public void setP(String p) {
		this.p = p;
	}

	public String getCe() {
		return ce;
	}

	public void setCe(String ce) {
		this.ce = ce;
	}

	public String getFv() {
		return fv;
	}

	public void setFv(String fv) {
		this.fv = fv;
	}

	@Override
	public String toString() {
		return "MsDetails [" + (scanType != null ? "scanType=" + scanType + ", " : "")
				+ (is != null ? "is=" + is + ", " : "") + (p != null ? "p=" + p + ", " : "")
				+ (ce != null ? "ce=" + ce + ", " : "") + (fv != null ? "fv=" + fv : "") + "]";
	}

}
