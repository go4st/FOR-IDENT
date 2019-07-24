package de.hswt.filehandler.definition;

import java.util.Map;


public class SheetDefinition {

	private String id;
	private String nameInXmlFile;
	private String nameInExcelFile;
	private int identifierRow;
	private int contentRow;
	private Map<String, ColumnDefinition> identifierColumns;
	private Map<String, Integer> groupRequirements;

	public SheetDefinition(String id, String nameInXmlFile, int identifierRow, int contentRow,
			Map<String, ColumnDefinition> identifierColumns, Map<String, Integer> groupRequirements) {
		this.id = id;
		this.nameInXmlFile = nameInXmlFile;
		this.identifierRow = identifierRow;
		this.contentRow = contentRow;
		this.identifierColumns = identifierColumns;
		this.groupRequirements = groupRequirements;
	}

	public String getID() {
		return id;
	}

	public String getNameInXmlFile() {
		return nameInXmlFile;
	}

	public void setNameInExcelFile(String nameInExcelFile) {
		this.nameInExcelFile = nameInExcelFile;
	}

	public String getNameInExcelFile() {
		return nameInExcelFile;
	}

	public int getIdentifierRow() {
		return identifierRow;
	}

	public int getContentRow() {
		return contentRow;
	}

	public Map<String, ColumnDefinition> getIdentifierColumns() {
		return identifierColumns;
	}

	public Map<String, Integer> getGroupRequirements() {
		return groupRequirements;
	}

	public void reset() {
		nameInExcelFile = null;
		identifierColumns.values().forEach(ColumnDefinition::reset);
	}
}
