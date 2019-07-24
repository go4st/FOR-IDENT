package de.hswt.fi.msms.service.model;

import de.hswt.fi.model.Peak;

import java.awt.image.RenderedImage;

public class MsMsCandidateFragmentBuilder {
	private double mass;
	private String formula;
	private String smiles;
	private Peak peak;
	private RenderedImage image;

	public MsMsCandidateFragmentBuilder withMass(double mass) {
		this.mass = mass;
		return this;
	}

	public MsMsCandidateFragmentBuilder withFormula(String formula) {
		this.formula = formula;
		return this;
	}

	public MsMsCandidateFragmentBuilder withSmiles(String smiles) {
		this.smiles = smiles;
		return this;
	}

	public MsMsCandidateFragmentBuilder withPeak(Peak peak) {
		this.peak = peak;
		return this;
	}

	public MsMsCandidateFragmentBuilder withImage(RenderedImage image) {
		this.image = image;
		return this;
	}

	public MsMsCandidateFragment createMsMsCandidateFragment() {
		return new MsMsCandidateFragment(mass, formula, smiles, peak, image);
	}
}