package de.hswt.fi.common;

public enum Ionisation {

	NEUTRAL_IONISATION("\u00B10", 0.0),
	POSITIVE_IONISATION("+H", -SearchUtil.H_MASS),
	NEGATIVE_IONISATION("-H", SearchUtil.H_MASS);

	private String label;

	private double ionisation;

	Ionisation(String label, double ionisation) {
		this.label = label;
		this.ionisation = ionisation;
	}

	public double getIonisation() {
		return ionisation;
	}

	@Override
	public String toString() {
		return label;
	}
}
