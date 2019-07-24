package de.hswt.fi.export.service.excel;

import java.util.List;
import java.util.Objects;

public class ExcelSheetDefinition<T> {

	public enum ColumnDirection {
		VERTICAL,
		HORIZONTAL
	}

	private String name;

	private boolean includeSource;

	private List<String> columnPropertyIds;

	private List<T> data;

	private ColumnDirection columnDirection;

	private boolean createIndex;

	public ExcelSheetDefinition(String name) {
		Objects.requireNonNull(name);
		if (name.isEmpty()) {
			throw new IllegalArgumentException("The name argument must not be empty.");
		}
		this.name = name;
		columnDirection = ColumnDirection.VERTICAL;
		createIndex = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public List<T> getData() {
		return data;
	}

	public List<String> getColumnPropertyIds() {
		return columnPropertyIds;
	}

	public void setColumnPropertyIds(List<String> columnPropertyIds) {
		this.columnPropertyIds = columnPropertyIds;
	}

	public boolean isIncludeSource() {
		return includeSource;
	}

	public void setIncludeSource(boolean includeSource) {
		this.includeSource = includeSource;
	}

	ColumnDirection getColumnDirection() {
		return columnDirection;
	}

	public void setColumnDirection(ColumnDirection columnDirection) {
		Objects.requireNonNull(columnDirection);
		this.columnDirection = columnDirection;
	}

	boolean isCreateIndex() {
		return createIndex;
	}

	public void setCreateIndex(boolean createIndex) {
		this.createIndex = createIndex;
	}
}
