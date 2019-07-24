package de.hswt.fi.export.service.excel;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

class ExcelSheetImporter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelSheetImporter.class);

	void addSheetsFromFileToWorkbook(File importFile, Workbook workbook) {

		try (Workbook sourceData = WorkbookFactory.create(importFile)) {
			for (int i = 0; i < sourceData.getNumberOfSheets(); i++) {
				Sheet sheet = sourceData.getSheetAt(i);
				Sheet newSheet = workbook.createSheet(sheet.getSheetName());
				copySheet(sheet, newSheet);
			}
		} catch (IOException | EncryptedDocumentException | InvalidFormatException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private void copySheet(Sheet oldSheet, Sheet newSheet) {

		for (int i = oldSheet.getFirstRowNum(); i <= oldSheet.getLastRowNum(); i++) {
			Row oldRow = oldSheet.getRow(i);
			Row newRow = newSheet.createRow(i);
			if (oldRow != null) {
				copyRow(oldRow, newRow);
			}
		}
	}

	private void copyRow(Row oldRow, Row newRow) {

		if(oldRow.getFirstCellNum() == -1) {
			return;
		}

		for (int i = oldRow.getFirstCellNum(); i <= oldRow.getLastCellNum(); i++) {

			Cell oldCell = oldRow.getCell(i);
			Cell newCell = newRow.createCell(i);

			if (oldCell != null) {

				switch (oldCell.getCellType()) {
				case HSSFCell.CELL_TYPE_STRING:
					newCell.setCellValue(oldCell.getStringCellValue());
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					newCell.setCellValue(oldCell.getNumericCellValue());
					break;
				case HSSFCell.CELL_TYPE_BLANK:
					newCell.setCellType(HSSFCell.CELL_TYPE_BLANK);
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN:
					newCell.setCellValue(oldCell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_ERROR:
					newCell.setCellErrorValue(oldCell.getErrorCellValue());
					break;
				case HSSFCell.CELL_TYPE_FORMULA:
					//TODO Handle invalid Excel formulas
					try {
						newCell.setCellFormula(oldCell.getCellFormula());
					} catch(FormulaParseException e) {
						newCell.setCellErrorValue(oldCell.getErrorCellValue());
					}
					break;
				default:
					break;
				}
			}
		}
	}

}
