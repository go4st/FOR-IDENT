package de.hswt.fi.ui.vaadin.windows;

import com.vaadin.data.*;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import de.hswt.fi.export.service.excel.ExcelCreator;
import de.hswt.fi.export.service.excel.ExcelFileDefinition;
import de.hswt.fi.export.service.excel.ExcelSheetDefinition;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.viritin.button.DownloadButton;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@SpringComponent
@PrototypeScope
public class DownloadWindow<T> extends AbstractWindow {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadWindow.class);
	private static final String DOWNLOAD_FILE_SUFFIX = "_results";
	private static final String FILE_TYPE_SUFFIX = ".xlsx";
	private static final String DEFAULT_EXPORT_FILE_NAME = "results.xlsx";
	private final ExcelCreator exportCreator;
	private CssLayout contentLayout;
	private TextField fileNameTextField;
	private CheckBox includeSourceCheckBox;
	private CheckBoxGroup<String> columnOptionGroup;
	private Map<String, String> possibleExportColumns;
	protected List<T> entries;
	private List<ExcelSheetDefinition> optionalSheets;
	private DownloadButton downloadButton;
	protected Binder<ExcelSheetDefinition<T>> binder;
	private Path sourceFilePath;
	private boolean addSourceFile = false;
	private CheckBox selectAllCheckbox;

	@Autowired
	protected DownloadWindow(ComponentFactory componentFactory, I18N i18n, ExcelCreator exportCreator) {
		super(componentFactory, i18n, false);
		this.exportCreator = exportCreator;
		setWidth(LayoutConstants.WINDOW_WIDTH_MEDIUM);
	}

	@Override
	protected String getWindowCaption() {
		return i18n.get(UIMessageKeys.EXPORT_WINDOW_WINDOW_CAPTION);
	}

	@Override
	protected Component getContentComponent() {

		contentLayout = new CssLayout();
		contentLayout.setSizeFull();
		contentLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_COLUMN);

		initFileNameTextField();
		initIncludeSourceCheckBox();
		initOptionGroup();
		initDownloadButton();
		initFieldGroups();
		initBeanValidation();

		return contentLayout;
	}

	private void initFileNameTextField() {
		fileNameTextField = componentFactory.createTextField(
				i18n.get(UIMessageKeys.EXPORT_WINDOW_FILE_TEXT_FIELD_CAPTION), 255);
		contentLayout.addComponent(componentFactory.createRowLayout(fileNameTextField));
	}

	private void initIncludeSourceCheckBox() {

		CssLayout layout = new CssLayout();
		layout.setWidth("100%");

		layout.addComponent(new Label(i18n.get(UIMessageKeys.EXPORT_WINDOW_INCLUDE_SOURCES_CHECKBOX_CAPTION)));

		includeSourceCheckBox = new CheckBox();
		includeSourceCheckBox.addStyleName(CustomValoTheme.MARGIN_LEFT);
		includeSourceCheckBox.setValue(false);
		layout.addComponent(includeSourceCheckBox);

		contentLayout.addComponent(componentFactory.createRowLayout(layout));
	}

	private void initOptionGroup() {

		CssLayout layout = new CssLayout();
		layout.setCaption(i18n.get(UIMessageKeys.DOWNLOAD_WINDOW_COLUMNS_CAPTION));
		layout.addStyleName(CustomValoTheme.BORDER_COLOR_ALT1);
		layout.addStyleName(CustomValoTheme.PADDING);
		layout.setSizeFull();

		selectAllCheckbox = new CheckBox(i18n.get(UIMessageKeys.SELECT_CHECKBOX_CAPTION));
		selectAllCheckbox.addValueChangeListener(event -> selectAll(event.getValue()));
		layout.addComponent(selectAllCheckbox);

		Label line = componentFactory.createHorizontalLine("1px");
		line.addStyleName(CustomValoTheme.MARGIN_HALF_BOTTOM);
		layout.addComponent(line);

		columnOptionGroup = new CheckBoxGroup<>();
		columnOptionGroup.setWidth("100%");
		columnOptionGroup.setHeight(LayoutConstants.LARGE);
		columnOptionGroup.addStyleName(CustomValoTheme.SCROLL_OVERFLOW);
		layout.addComponent(columnOptionGroup);

		contentLayout.addComponent(componentFactory.createRowLayout(layout));
	}

	private void initDownloadButton() {
		downloadButton = new DownloadButton(getExcel());
		downloadButton.setCaption(i18n.get(UIMessageKeys.EXPORT_WINDOW_DOWNLOAD_BUTTON_CAPTION));
		replaceOkButton(downloadButton);
	}

	private void selectAll(boolean select) {
		if(select) possibleExportColumns.values().forEach(exportColumn -> columnOptionGroup.select(exportColumn));
		else possibleExportColumns.values().forEach(exportColumn -> columnOptionGroup.deselect(exportColumn));
	}

	private DownloadButton.ContentWriter getExcel() {

		return stream -> {
			try (Workbook workbook = exportCreator.createWorkbook(createExcelFileDefinition(sourceFilePath, addSourceFile))) {
				workbook.write(stream);
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
			}
			close();
		};
	}

	private void initFieldGroups() {
		binder = new Binder<>();
		binder.forField(fileNameTextField)
				.withValidator(new BeanValidator(ExcelFileDefinition.class, "filename"))
				.bind(ExcelSheetDefinition::getName, ExcelSheetDefinition::setName);
		binder.bind(includeSourceCheckBox, ExcelSheetDefinition::isIncludeSource, ExcelSheetDefinition::setIncludeSource);
		binder.forField(columnOptionGroup)
				.withConverter(new Converter<Set<String>, List<String>>() {

					@Override
					public Result<List<String>> convertToModel(Set<String> value, ValueContext context) {

						if (value == null || possibleExportColumns == null) return Result.error("");

						List<String> model = possibleExportColumns.keySet().stream()
								.filter(property -> value.contains(possibleExportColumns.get(property)))
								.collect(Collectors.toList());
						return Result.ok(model);
					}

					@Override
					public Set<String> convertToPresentation(List<String> value, ValueContext context) {
						if (value == null || possibleExportColumns == null) return Collections.emptySet();
						return value.stream().map(property -> possibleExportColumns.get(property)).collect(Collectors.toSet());
					}
				}).bind(ExcelSheetDefinition::getColumnPropertyIds, ExcelSheetDefinition::setColumnPropertyIds);


		binder.setBean(createNewExcelSheetDefinition());
		binder.addValueChangeListener(listener -> updateCanFinish());
	}

	private void initBeanValidation() {
		downloadButton.setFileName(DEFAULT_EXPORT_FILE_NAME);
		fileNameTextField.setValue(DEFAULT_EXPORT_FILE_NAME);
	}

	private void updateCanFinish() {
		setCanFinish(binder.isValid());
		downloadButton.setFileName(fileNameTextField.getValue());
	}

	@Override
	protected void handleOk() {
		resetFieldGroup();
		close();
	}

	private void resetFieldGroup() {
		binder.setBean(createNewExcelSheetDefinition());
	}

	private ExcelSheetDefinition<T> createNewExcelSheetDefinition() {
		return new ExcelSheetDefinition<>(i18n.get(UIMessageKeys.RESULTS_DOWNLOAD_HANDLER_EXCEL_SPREADSHEET_DEFINITION_RESULTS));
	}

	public void setEntries(List<T> entries) {
		this.clear();
		this.entries = entries;
	}

	public void clear() {
		includeSourceCheckBox.setEnabled(true);
		includeSourceCheckBox.clear();
		selectAllCheckbox.clear();
		columnOptionGroup.clear();
		initDownloadButton();
	}

	public void setIncludeSourcesEnabled(boolean enabled) {
		includeSourceCheckBox.setEnabled(enabled);
	}

	public void setOptionalSheets(List<ExcelSheetDefinition> optionalSheets) {
		this.optionalSheets = optionalSheets;
	}

	public void setPossibleExportColumns(Map<String, String> possibleExportColumns) {
		this.possibleExportColumns = possibleExportColumns;
		columnOptionGroup.setItems(possibleExportColumns.values());
	}

	public void setExportColumnIds(List<String> exportColumnIds) {
		resetFieldGroup();

		if (entries.size() == 1) {
			binder.getBean().setColumnPropertyIds(exportColumnIds);
			return;
		}

		binder.getBean().setColumnPropertyIds(exportColumnIds);
	}

	private ExcelFileDefinition createExcelFileDefinition(Path sourceFilePath, boolean addSourceFile) {

		try {
			binder.writeBean(binder.getBean());
		} catch (ValidationException e) {
			LOGGER.error(e.getMessage());
			return null;
		}

		binder.getBean().setData(entries);
		binder.getBean().setCreateIndex(true);

		List<ExcelSheetDefinition> sheets = new ArrayList<>();
		sheets.add(binder.getBean());
		if (optionalSheets != null) {
			sheets.addAll(optionalSheets);
		}

		ExcelFileDefinition fileDefinition = new ExcelFileDefinition();
		fileDefinition.setFilename(fileNameTextField.getValue());
		if (sourceFilePath != null) {
			fileDefinition.setSourceFile(sourceFilePath);
			fileDefinition.setAddSources(addSourceFile);
		}
		fileDefinition.setSheets(sheets);

		return fileDefinition;
	}

	public void setSheetName(String sheetName) {
		binder.getBean().setName(sheetName);
	}

	public void setFileName(String fileName) {

		int lastIndex = fileName.lastIndexOf('.');

		if (lastIndex != -1) {
			fileName = fileName.substring(0, lastIndex);
		}

		fileNameTextField.setValue(fileName + FILE_TYPE_SUFFIX);
		downloadButton.setFileName(fileNameTextField.getValue());
	}
	
	public void setSourceFilePath(Path sourceFilePath, boolean addSourceFile) {
		this.sourceFilePath = sourceFilePath;
		this.addSourceFile = addSourceFile;
	}
}
