package de.hswt.fi.search.service.mass.search.model.properties;

public class StringValuePropertyPair {

	private int valueMappingID;

	private StringValueProperty first;

	private StringValueProperty second;

	public StringValuePropertyPair(int valueMappingID, StringValueProperty first,
			StringValueProperty second) {
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

	public StringValueProperty getFirst() {
		return first;
	}

	public StringValueProperty getSecond() {
		return second;
	}
}
