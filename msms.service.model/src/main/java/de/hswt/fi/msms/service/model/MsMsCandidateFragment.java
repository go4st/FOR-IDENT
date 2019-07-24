package de.hswt.fi.msms.service.model;

import de.hswt.fi.model.Peak;

import java.awt.image.RenderedImage;
import java.io.Serializable;

public class MsMsCandidateFragment implements Serializable {

	private static final long serialVersionUID = 1L;

	private double mass;

	private String formula;

	private String smiles;

	private Peak peak;

	private RenderedImage image;

	public MsMsCandidateFragment(double mass, String formula, String smiles, Peak peak,
			RenderedImage image) {
		this.mass = mass;
		this.formula = formula;
		this.smiles = smiles;
		this.peak = peak;
		this.image = image;
	}

	public double getMass() {
		return mass;
	}

	public String getFormula() {
		return formula;
	}

	public String getSmiles() {
		return smiles;
	}

	public Peak getPeak() {
		return peak;
	}

	public RenderedImage getImage() {
		return image;
	}
}
