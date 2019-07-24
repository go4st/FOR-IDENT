package de.hswt.filehandler.definition;

import java.util.*;

public class ColumnDefinition {

	private String id;
	
	private String namePrefix;
	
	private String groupID;
	
	private boolean multiple;
	
	private int singularColumnIndex;

	// Contains the full column names and their column indices in the data file
	private HashMap<String, Integer> multipleColumns;

	// Contains the full column names and their column indices in the data file.
	// Map is sorted from low column indices to high column indices
	private LinkedHashMap<String, Integer> sortedMultipleColumns;

	public ColumnDefinition(String id, String name, String multiple, String groupID) {

		this.id = id;
		namePrefix = name;
		this.groupID = groupID;
		singularColumnIndex = -1;

		switch (multiple) {
			case "true":
				this.multiple = true;
				break;
			case "false":
				this.multiple = false;
				break;
			default:
				throw new IllegalArgumentException("The attribute \"multiple\" must ne true or false");
		}

		multipleColumns = new HashMap<>();
		sortedMultipleColumns = new LinkedHashMap<>();
	}

	public String getID() {
		return id;
	}

	public String getName() {
		return namePrefix;
	}

	public boolean getMultiple() {
		return multiple;
	}

	public int getColumnIndex() {
		return singularColumnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		singularColumnIndex = columnIndex;
	}

	/**
	 * Returns a map containing the full column names and their corresponding
	 * column indices in the data file. The map is sorted from low column
	 * indices to high column indices.
	 * 
	 * @return
	 */
	public Map<String, Integer> getColumnIndices() {
		return sortedMultipleColumns;
	}

	public void setColumnIndices(String columnName, int columnIndex) {
		multipleColumns.put(columnName, columnIndex);

		// ensure that sortedMultipleColumns has a sorted value set
		sortMultipleColumns();
	}

	private void sortMultipleColumns() {

		@SuppressWarnings("unchecked")
		HashMap<String, Integer> clone = (HashMap<String, Integer>) multipleColumns.clone();
		sortedMultipleColumns = new LinkedHashMap<>();
		String keyToSmallestValue = null;
		Integer smallestValue = null;

		while (!clone.isEmpty()) {

			for (String key : clone.keySet()) {

				int value = clone.get(key);

				if (smallestValue == null) {
					keyToSmallestValue = key;
					smallestValue = value;
				}

				if (smallestValue > value) {
					keyToSmallestValue = key;
					smallestValue = value;
				}
			}
			clone.remove(keyToSmallestValue);
			sortedMultipleColumns.put(keyToSmallestValue, smallestValue);
			keyToSmallestValue = null;
			smallestValue = null;
		}
	}

	public void clearColumnIndices() {
		multipleColumns.clear();
	}

	public String getGroupID() {
		return groupID;
	}

	public void reset() {
		singularColumnIndex = -1;
		multipleColumns = new HashMap<>();
		sortedMultipleColumns = new LinkedHashMap<>();
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("ColumnDefinition [");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if (namePrefix != null) {
			builder.append("namePrefix=");
			builder.append(namePrefix);
			builder.append(", ");
		}
		if (groupID != null) {
			builder.append("groupID=");
			builder.append(groupID);
			builder.append(", ");
		}
		builder.append("multiple=");
		builder.append(multiple);
		builder.append(", singularColumnIndex=");
		builder.append(singularColumnIndex);
		builder.append(", ");
		if (multipleColumns != null) {
			builder.append("multipleColumns=");
			builder.append(toString(multipleColumns.entrySet(), maxLen));
			builder.append(", ");
		}
		if (sortedMultipleColumns != null) {
			builder.append("sortedMultipleColumns=");
			builder.append(toString(sortedMultipleColumns.entrySet(), maxLen));
		}
		builder.append("]");
		return builder.toString();
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

}
