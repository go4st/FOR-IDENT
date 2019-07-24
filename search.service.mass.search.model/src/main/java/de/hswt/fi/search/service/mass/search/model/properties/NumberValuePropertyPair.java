package de.hswt.fi.search.service.mass.search.model.properties;

public class NumberValuePropertyPair {

	private int valueMappingID;

	private NumberValueProperty first;

	private NumberValueProperty second;

	public NumberValuePropertyPair(int valueMappingID, NumberValueProperty first,
			NumberValueProperty second) {
		if (first == null || second == null) {
			throw new IllegalArgumentException("The arguments must not be null.");
		}
		this.valueMappingID = valueMappingID;
		this.first = first;
		this.second = second;
	}

	public int getValueMappingID() {
		return valueMappingID;
	}

	public NumberValueProperty getFirst() {
		return first;
	}

	public NumberValueProperty getSecond() {
		return second;
	}
}
