package de.hswt.fi.search.service.mass.search.model.properties;

public final class NumberSearchProperty {

	private final double value;

	private final double range;

	public NumberSearchProperty(double value, double range) {
		this.value = value;
		this.range = range;
	}

	public double getValue() {
		return value;
	}

	public double getRange() {
		return range;
	}
}
