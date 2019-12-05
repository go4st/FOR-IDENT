package de.hswt.fi.export.service.excel;

import de.hswt.fi.beans.BeanColumnDefinition;
import de.hswt.fi.beans.BeanComponentMapper;
import de.hswt.fi.calculation.service.api.CalculationService;
import de.hswt.fi.export.service.excel.ExcelSheetDefinition.ColumnDirection;
import de.hswt.fi.export.service.excel.RowCreator.CellValue;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class WorkbookCreator {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkbookCreator.class);

	private CalculationService calculationService;

	private BeanComponentMapper mapper;

	private boolean includeSource;

	private Workbook workbook;

	private Sheet sheet;

	private int nextRowIndex;

	private int resultIndex;

	private int columnCount;

	private int structureColumnIndex;

	private Set<String> postfixFilter;

	private Map<String, CellStyle> numberCellFormats;

	private Map<BeanColumnDefinition, Integer> columnCounts;

	private ExecutorService executor;

	private ExcelSheetDefinition currentSheetDefinition;

	private int columnOffset;

	WorkbookCreator(BeanComponentMapper mapper, CalculationService calculationService) {
		if (mapper == null || calculationService == null) {
			throw new NullPointerException("Parameter must not be null.");
		}
		this.mapper = mapper;
		this.calculationService = calculationService;
	}

	Workbook createWorkbook(ExcelFileDefinition definition) {
		if (definition == null) {
			throw new NullPointerException("Parameter definition is null.");
		}

		initialize();

		for (ExcelSheetDefinition sheetDefinition : definition.getSheets()) {
			currentSheetDefinition = sheetDefinition;
			createSheet();
		}

		if(definition.isAddSources() &&  definition.getSourceFile() != null && definition.getSourceFile().toFile().exists()) {
			new ExcelSheetImporter().addSheetsFromFileToWorkbook(definition.getSourceFile().toFile(), workbook);
		}
		
		return workbook;
	
	}

	private void createSheet() {
		if (currentSheetDefinition.getData() == null
				|| currentSheetDefinition.getData().isEmpty()) {
			return;
		}

		if (!currentSheetDefinition.isIncludeSource()) {
			postfixFilter.add("source");
		} else {
			postfixFilter.remove("source");
		}

		sheet = workbook.createSheet(checkSheetName(currentSheetDefinition.getName()));

		Object targetBean = currentSheetDefinition.getData().iterator().next();

		List<String> propertyIds = currentSheetDefinition.getColumnPropertyIds();
		if (propertyIds == null) {
			propertyIds = mapper.getSelectorColumns(currentSheetDefinition.getData().iterator().next());
		}

		Map<String, BeanColumnDefinition> columnDefinitions = mapper
				.getBeanColumnDefinitions(targetBean, propertyIds);

		if (columnDefinitions.size() != propertyIds.size()) {
			throw new TypeNotPresentException("Unavailable column definition requested.", null);
		}

		Map<String, String> captions = mapper.getNestedPropertyCaptions(columnDefinitions.values(),
				postfixFilter);

		columnOffset = currentSheetDefinition.isCreateIndex() ? 1 : 0;

		// find index of structure column, if any
		structureColumnIndex = -1;
		int i = -1;
		for (String key : captions.keySet()) {
			i++;
			if (key.endsWith("structure")) {
				structureColumnIndex = i + columnOffset;
				break;
			}
		}

		nextRowIndex = 0;
		columnCount = columnOffset + captions.size();
		resultIndex = 1;

		if (currentSheetDefinition.getColumnDirection().equals(ColumnDirection.VERTICAL)) {
			createTitleRow(captions.values());
		} else {
			createTitleColumn(captions.values());
		}

		columnDefinitions.values().forEach(d -> columnCounts.put(d, getColumnCount(d)));

		executeSheetCreation(columnDefinitions);

		resizeColumns();
	}

	private String checkSheetName(String sheetName) {
		// XSSFWorkbook sheet names can only be 31 characters and are truncated if longer.
		// Therefore the actual sheet name is truncated already here to  prevent failures
		// in comparison within checkSheetName method!
		if(sheetName.length() > 31) sheetName = sheetName.substring(0, 31);
		WorkbookUtil.validateSheetName(sheetName);

		for(int i = 0; i < workbook.getNumberOfSheets(); i++) {
			if(workbook.getSheetName(i).equals(sheetName)) {
					return checkSheetName("1_" + sheetName);
			}
		}
		return sheetName;
	}

	private void executeSheetCreation(Map<String, BeanColumnDefinition> columnDefinitions) {
		List<FutureTask<List<List<CellValue>>>> tasks = new ArrayList<>();

		executor = Executors.newCachedThreadPool();

		for (Object bean : currentSheetDefinition.getData()) {
			FutureTask<List<List<CellValue>>> task = new FutureTask<>(() -> {
				RowCreator rowCreator = new RowCreator(bean, mapper, calculationService,
						columnCounts, columnDefinitions, postfixFilter, columnCount - columnOffset);
				return rowCreator.getCells();
			});
			tasks.add(task);
			executor.execute(task);
		}

		do {
			FutureTask<List<List<CellValue>>> task = tasks.get(0);
			List<List<CellValue>> rows = null;
			try {
				rows = task.get();
			} catch (InterruptedException e) {
				LOGGER.error(e.getMessage());
				Thread.currentThread().interrupt();
			} catch (ExecutionException e) {
				LOGGER.error(e.getMessage());
			}
			if (rows != null && !rows.isEmpty()) {
				if (currentSheetDefinition.getColumnDirection().equals(ColumnDirection.VERTICAL)) {
					insertRows(rows);
				} else {
					insertColumns(rows);
				}
			}
			tasks.remove(0);
			resultIndex++;
		} while (!tasks.isEmpty());
	}

	private void initialize() {
		columnCounts = new HashMap<>();

		workbook = new XSSFWorkbook();

		initNumberCellFormats();

		postfixFilter = new HashSet<>();
		postfixFilter.add("lastModified");
		postfixFilter.add("additional");

		postfixFilter.add("scoreValue");
		postfixFilter.add("weight");
	}

	private void initNumberCellFormats() {

		DataFormat dataFormat = workbook.createDataFormat();

		numberCellFormats = new HashMap<>();
		numberCellFormats.put("%.1f", getCellStyle(dataFormat, "0.0"));
		numberCellFormats.put("%.2f", getCellStyle(dataFormat, "0.00"));
		numberCellFormats.put("%.3f", getCellStyle(dataFormat, "0.000"));
		numberCellFormats.put("%.4f", getCellStyle(dataFormat, "0.0000"));
		numberCellFormats.put("%.5f", getCellStyle(dataFormat, "0.00000"));
		numberCellFormats.put("%.6f", getCellStyle(dataFormat, "0.000000"));
	}

	private CellStyle getCellStyle(DataFormat dataFormat, String style) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setDataFormat(dataFormat.getFormat(style));
		return cellStyle;
	}

	private void initRow() {
		Row row = sheet.createRow(nextRowIndex++);
		if (currentSheetDefinition.isCreateIndex()) {
			row.createCell(0).setCellValue(resultIndex);
		}
	}

	private int getColumnCount(BeanColumnDefinition column) {
		int count = 0;
		for (BeanColumnDefinition childColumn : column.getChildren()) {
			if (postfixFilter.stream().noneMatch(f -> childColumn.getPropertyId().endsWith(f))) {
				count += getColumnCount(childColumn);
			}
		}
		return Math.max(count, 1);
	}

	private void createTitleRow(Collection<String> captions) {
		Row row = sheet.createRow(nextRowIndex++);

		int cellId = 0;
		Cell cell;

		if (currentSheetDefinition.isCreateIndex()) {
			cell = row.createCell(cellId++);
			cell.setCellValue("ID");
		}

		for (String caption : captions) {
			cell = row.createCell(cellId++);
			cell.setCellValue(caption);
		}
	}

	private void createTitleColumn(Collection<String> captions) {
		for (String caption : captions) {
			initRow();
			resultIndex++;
			Row row = sheet.getRow(nextRowIndex - 1);
			Cell cell = row.createCell(columnOffset);
			cell.setCellValue(caption);
		}
	}

	private void insertRows(List<List<CellValue>> rows) {
		int startRowIndex = nextRowIndex;

		rows.get(0).forEach(r -> initRow());

		int columnIndex = columnOffset;
		for (List<CellValue> column : rows) {
			int rowIndex = startRowIndex;
			for (CellValue cellValue : column) {
				if (!cellValue.getValue().isPresent()) {
					continue;
				}
				if (columnIndex == structureColumnIndex) {
					writeStructure((byte[]) cellValue.getValue().get(), rowIndex++);
				} else {
					Cell cell = sheet.getRow(rowIndex++).createCell(columnIndex);
					writeCell(cell, cellValue);
				}
			}
			columnIndex++;
		}
	}

	private void insertColumns(List<List<CellValue>> rows) {
		int startColumnIndex = 1 + columnOffset;

		int rowIndex = 0;
		for (List<CellValue> column : rows) {
			int columnIndex = startColumnIndex;
			for (CellValue cellValue : column) {
				if (!cellValue.getValue().isPresent()) {
					continue;
				}
				Cell cell = sheet.getRow(rowIndex).createCell(columnIndex++);
				writeCell(cell, cellValue);
			}
			rowIndex++;
		}
	}

	private void writeCell(Cell cell, CellValue cellValue) {

		if (!cellValue.getValue().isPresent()) {
			return;
		}

		Object value = cellValue.getValue().get();

		if (Number.class.isAssignableFrom(cellValue.getType())) {
			if (Double.class.isAssignableFrom(value.getClass())) {
				cell.setCellValue((Double) value);
				cellValue.getFormat().ifPresent(format -> cell.setCellStyle(numberCellFormats.get(format)));
			}
		} else if (Date.class.isAssignableFrom(cellValue.getType()) && Date.class.isAssignableFrom(value.getClass())) {
			cell.setCellValue((Date) value);
		} else {
			cell.setCellValue(value.toString());
		}
	}

	private void writeStructure(byte[] imageData, int rowIndex) {
		Drawing drawing = sheet.createDrawingPatriarch();

		Row row = sheet.getRow(rowIndex);
		row.setHeightInPoints(RowCreator.IMAGE_HEIGHT);

        sheet.setColumnWidth(structureColumnIndex,  PixelUtil.pixel2WidthUnits(RowCreator.IMAGE_WIDTH));

		int pictureIdx = workbook.addPicture(imageData, Workbook.PICTURE_TYPE_PNG);

		ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();
		anchor.setCol1(structureColumnIndex);
		anchor.setRow1(rowIndex);

		Picture pict = drawing.createPicture(anchor, pictureIdx);
		pict.resize(1,1);
	}

	private void resizeColumns() {
		for (int i = columnOffset; i < columnCount; i++) {
			final int index = i;
			executor.execute(() -> resizeColumn(index));
		}
		executor.shutdown();
		try {
			executor.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage());
			Thread.currentThread().interrupt();
		}
	}

	private void resizeColumn(int columnIndex) {
		int maxNumCharacters = 0;

		if (columnIndex == structureColumnIndex) return;

		for (int row = 0; row <= sheet.getLastRowNum(); row++) {
			DataFormatter formatter = new DataFormatter();
			int numberOfCharacters = formatter
					.formatCellValue(sheet.getRow(row).getCell(columnIndex)).length();
			if (numberOfCharacters > maxNumCharacters) {
				maxNumCharacters = numberOfCharacters;
			}
		}

		int width = (Math.min((int) ((maxNumCharacters) * (maxNumCharacters < 50 ? 1.3 : 1.1)), 100)
				* 256);
		synchronized (sheet) {
			sheet.setColumnWidth(columnIndex, width);
		}
	}

	static public class PixelUtil {

		public static final short EXCEL_COLUMN_WIDTH_FACTOR = 256;
		public static final short EXCEL_ROW_HEIGHT_FACTOR = 20;
		public static final int UNIT_OFFSET_LENGTH = 7;
		public static final int[] UNIT_OFFSET_MAP = new int[] { 0, 36, 73, 109, 146, 182, 219 };

		public static short pixel2WidthUnits(int pxs) {
			short widthUnits = (short) (EXCEL_COLUMN_WIDTH_FACTOR * (pxs / UNIT_OFFSET_LENGTH));
			widthUnits += UNIT_OFFSET_MAP[(pxs % UNIT_OFFSET_LENGTH)];
			return widthUnits;
		}

		public static int widthUnits2Pixel(short widthUnits) {
			int pixels = (widthUnits / EXCEL_COLUMN_WIDTH_FACTOR) * UNIT_OFFSET_LENGTH;
			int offsetWidthUnits = widthUnits % EXCEL_COLUMN_WIDTH_FACTOR;
			pixels += Math.floor((float) offsetWidthUnits / ((float) EXCEL_COLUMN_WIDTH_FACTOR / UNIT_OFFSET_LENGTH));
			return pixels;
		}

		public static int heightUnits2Pixel(short heightUnits) {
			int pixels = (heightUnits / EXCEL_ROW_HEIGHT_FACTOR);
			int offsetWidthUnits = heightUnits % EXCEL_ROW_HEIGHT_FACTOR;
			pixels += Math.floor((float) offsetWidthUnits / ((float) EXCEL_ROW_HEIGHT_FACTOR / UNIT_OFFSET_LENGTH));
			return pixels;
		}
	}

	public boolean includeSource() {
		return includeSource;
	}

	public void setIncludeSource(boolean includeSource) {
		this.includeSource = includeSource;
	}
}
