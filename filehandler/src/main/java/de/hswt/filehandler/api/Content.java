package de.hswt.filehandler.api;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;

public interface Content {

	void addTableToContent(Sheet sheet, String id, int numberOfIdentifiers, int identifierRow, int contentRow, ArrayList<Integer> columnsWithIdentifiers);

	void tellContent();
}
