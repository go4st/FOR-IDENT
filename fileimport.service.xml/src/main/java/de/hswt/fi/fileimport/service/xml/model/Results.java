package de.hswt.fi.fileimport.service.xml.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Results {

	@JacksonXmlProperty(localName = "Molecule")
	private Molecule molecule;

	public Molecule getMolecule() {
		return molecule;
	}

	public void setMolecule(Molecule molecule) {
		this.molecule = molecule;
	}

	@Override
	public String toString() {
		return "Results [" + (molecule != null ? "molecule=" + molecule : "") + "]";
	}

}
