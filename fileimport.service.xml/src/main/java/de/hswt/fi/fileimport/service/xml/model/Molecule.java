package de.hswt.fi.fileimport.service.xml.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Molecule {

	@JacksonXmlProperty(isAttribute = true)
	private String formula;

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	@Override
	public String toString() {
		return "Molecule [" + (formula != null ? "formula=" + formula : "") + "]";
	}

}
