package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.ProgressBarRenderer;
import com.vaadin.ui.themes.ValoTheme;
import de.hswt.fi.processing.service.model.ProcessCandidate;
import de.hswt.fi.processing.service.model.ProcessingJob;
import de.hswt.fi.processing.service.model.ProcessingResult;
import de.hswt.fi.processing.service.model.ProcessingUnit;
import de.hswt.fi.search.service.mass.search.model.Entry;
import de.hswt.fi.search.service.mass.search.model.SourceList;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.GridRendererProvider;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.container.ProcessingResultContainer;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DummyPayload;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import de.hswt.fi.ui.vaadin.grid.FilterGrid;
import de.hswt.fi.ui.vaadin.views.components.processing.ProcessingResultDetailsComponent;
import de.hswt.vaadin.score.renderer.ScoreRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.i18n.I18N;

import javax.inject.Provider;
import java.util.*;
import java.util.stream.Collectors;

@SpringComponent
@ViewScope
public class ProcessingResultsComponent extends
		AbstractResultsComponent<ProcessingJob, ProcessingResult, ProcessCandidate, ProcessingResultContainer> {

	private static final long serialVersionUID = 1L;

	private static final int MIN_SCREEN_WIDTH_FOR_FROZEN_COLUMN = 1600;

	private static final int FROZEN_COLUMNS = 6;

	private final ComponentFactory componentFactory;

	private final Provider<ProcessingResultDetailsComponent> detailsProvider;

	private FilterGrid<ProcessCandidate> grid;

	private boolean frozenColumns;

	private HeaderRow groupingRow;

	private Locale locale;

	private Label resultsLabel;

	@Autowired
	public ProcessingResultsComponent(EventBus.ViewEventBus eventBus, I18N i18n, ComponentFactory componentFactory,
									  Provider<ProcessingResultDetailsComponent> detailsProvider) {
		super(eventBus, i18n);
		this.componentFactory = componentFactory;
		this.detailsProvider = detailsProvider;
	}

	@Override
	protected Layout getHeaderLayout() {

		headerLayout = new CssLayout();

		resultsLabel = new Label();
		resultsLabel.setWidthUndefined();
		resultsLabel.addStyleName(CustomValoTheme.MARGIN_RIGHT);
		resultsLabel.addStyleName(CustomValoTheme.COLOR_ALT1);
		resultsLabel.addStyleName(ValoTheme.LABEL_BOLD);
		resultsLabel.addStyleName(ValoTheme.LABEL_LARGE);
		headerLayout.addComponent(resultsLabel);

		Button showSummaryWindowButton = componentFactory.createButton(VaadinIcons.LINE_CHART, i18n.get(UIMessageKeys.OPEN_SUMMARY_WINDOW_BUTTON_DESCRIPTION));
		showSummaryWindowButton.addClickListener(listener -> showSummaryWindow());
		headerLayout.addComponent(showSummaryWindowButton);

		Button clearFilterButton = componentFactory.createButton(VaadinIcons.FILTER,
				i18n.get(UIMessageKeys.CLEAR_FILTER_BUTTON_CAPTION));
		clearFilterButton.addClickListener(e -> clearFilter());
		headerLayout.addComponent(clearFilterButton);

		if (isNonResultsPossible()) {
			MenuBar split = new MenuBar();
			split.addStyleName(CustomValoTheme.MENU_BAR_COMPONENT_HEADER);
			split.addStyleName(CustomValoTheme.MENUBAR_BORDERLESS);
			headerLayout.addComponent(split);

			MenuBar.MenuItem dropdown = split.addItem("", VaadinIcons.DOWNLOAD, null);
			dropdown.addItem(i18n.get(UIMessageKeys.ABSTRACT_RESULTS_COMPONENT_DOWNLOAD_MENU_RESULTS), event -> handleResultDownload());
			dropdown.addItem(i18n.get(UIMessageKeys.ABSTRACT_RESULTS_COMPONENT_DOWNLOAD_MENU_NON_RESULTS), event -> handleNonResultDownload());
			dropdown.addItem(i18n.get(UIMessageKeys.ABSTRACT_RESULTS_COMPONENT_DOWNLOAD_MENU_COMPLETE), event -> handleCompleteDownload());
		} else {
			Button downloadButton = componentFactory.createButton(VaadinIcons.DOWNLOAD, i18n.get(UIMessageKeys.DOWNLOAD_BUTTON_CAPTION));
			downloadButton.addClickListener(e -> handleResultDownload());
			headerLayout.addComponent(downloadButton);
		}

		Button reportButton = componentFactory.createButton(VaadinIcons.BUG,
				i18n.get(UIMessageKeys.REPORT_RECORDS_BUTTON_CAPTION));
		reportButton.addClickListener(e -> handleReportRecords());
		headerLayout.addComponent(reportButton);

		if (hasFrozenColumns()) {
			initFrozenColumnSwitch();
		}

		return headerLayout;
	}

	@Override
	protected void initComponents() {
		locale = LocaleContextHolder.getLocale();
		setSizeFull();
		initGrid();
	}

	private void initGrid() {

		grid = new FilterGrid<>(ProcessCandidate.class);
		grid.setSizeFull();
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.setPrimaryStyleName(CustomValoTheme.GRID_CUSTOM);
		grid.addStyleName(CustomValoTheme.GRID_CIRCLE_BOOLEAN);

		((SingleSelectionModel<ProcessCandidate>) grid.setSelectionMode(SelectionMode.SINGLE)).setDeselectAllowed(false);

		grid.addSelectionListener(event -> {
			ProcessCandidate item = event.getFirstSelectedItem().orElse(null);
			if (item == null) {
				return;
			}
			selectionChanged(item);
		});

		grid.addItemClickListener(event -> {
			if (event.getMouseEventDetails().isDoubleClick()) {
				toggleDetails(event.getItem());
			}
		});

		groupingRow = grid.addHeaderRowAt(0);

		addComponent(grid);
	}

	private void initColumns() {

		grid.removeAllColumns();

		initDetailsColumn();
		initResultColumns();
		initScoringColumns();
		initTPPresentColumn();
		initMassScreeningColumns();
		if (isProcessExecuted(ProcessingUnit.RTI_SCREENING)) {
			initRtiScreeningColumns();
		}
		initEntryColumns();
	}

	private void initDetailsColumn() {
		grid.setDetailsGenerator(rowReference -> {
			ProcessingResultDetailsComponent details = detailsProvider.get();
			details.init(rowReference);
			return details;
		});

		grid.addComponentColumn(processCandidate -> {
			Button detailsButton = new Button("details");
			detailsButton.addStyleName(CustomValoTheme.BACKGROUND_COLOR_LIGHTGRAY);
			detailsButton.addStyleName(CustomValoTheme.BORDER_COLOR_WHITE);
			detailsButton.addStyleName(CustomValoTheme.COLOR_DEFAULT_TEXT);
			detailsButton.addStyleName(CustomValoTheme.PADDING_HALF_HORIZONTAL);
			detailsButton.addClickListener(event -> toggleDetails(processCandidate));
			return detailsButton;
		})
				.setWidth(LayoutConstants.COLUMN_WIDTH_TINY)
				.setCaption(i18n.get(UIMessageKeys.PROCESSING_RESULT_COLUMN_DETAILS))
				.setSortable(false);
	}

	private void initTPPresentColumn() {
		if (isProcessExecuted(ProcessingUnit.TP)) {
			grid.addColumn(processCandidate ->
					componentFactory.getCheckedIconHtml(processCandidate.getPathwayCandidate() != null))
					.setWidth(LayoutConstants.COLUMN_WIDTH_TINY)
					.setCaption(i18n.get(UIMessageKeys.PROCESSING_RESULT_COLUMN_HAS_PATHWAY))
					.setStyleGenerator(item -> CustomValoTheme.TEXT_CENTER)
					.setRenderer(new HtmlRenderer());
		}
	}

	private void initResultColumns() {

		Column<ProcessCandidate, String> targetIdentifierColumn = grid.addFilterColumn(
				processCandidate -> processCandidate.getMassSearchResult().getTargetIdentifier())
				.setFilterType(String.class)
				.setWidth(LayoutConstants.COLUMN_WIDTH_SMALL)
				.setCaption(i18n.get(UIMessageKeys.PROCESSING_RESULT_COLUMN_TARGET));

		Column<ProcessCandidate, Double> targetRtColumn = grid.addFilterColumn(
				processCandidate -> processCandidate.getMassSearchResult().getTargetRetentionTime())
				.setFilterType(Double.class)
				.setWidth(LayoutConstants.COLUMN_WIDTH_SMALL)
				.setCaption(i18n.get(UIMessageKeys.PROCESSING_RESULT_COLUMN_TARGET_RT))
				.setRenderer(this::convertDoubleHtmlSpan, new HtmlRenderer());

		Column<ProcessCandidate, String> resultName = grid.addFilterColumn(
				processCandidate -> processCandidate.getMassSearchResult().getEntry().getName().getValue())
				.setFilterType(String.class)
				.setWidth(LayoutConstants.COLUMN_WIDTH_SMALL)
				.setCaption(i18n.get(UIMessageKeys.PROCESSING_RESULT_COLUMN_RESULT_NAME));

		Column<ProcessCandidate, String> bestMatchColumn = grid.addColumn(
				processCandidate -> componentFactory.getBooleanIconHtml(processCandidate.isBestMatch()))
				.setWidth(LayoutConstants.COLUMN_WIDTH_TINY)
				.setCaption(i18n.get(UIMessageKeys.PROCESSING_RESULT_COLUMN_LOOK_AT))
				.setStyleGenerator(item -> CustomValoTheme.TEXT_CENTER)
				.setRenderer(new HtmlRenderer());

		Column<ProcessCandidate, Double> overallScoreColumn = grid.addColumn(
				processCandidate -> processCandidate.getScore().getScoreValue())
				.setWidth(LayoutConstants.COLUMN_WIDTH_TINY)
				.setCaption(i18n.get(UIMessageKeys.PROCESSING_RESULT_COLUMN_SCORE))
				.setStyleGenerator(item -> CustomValoTheme.TEXT_CENTER)
				.setRenderer(new ScoreRenderer(createExclamationTriangle(i18n.get(UIMessageKeys.SCORING_DETAILS_COMPONENT_NO_SCORE_AVAILABLE))));

		groupingRow.join(targetIdentifierColumn, targetRtColumn, resultName, bestMatchColumn, overallScoreColumn)
				.setText("Results");
	}

	private String convertDoubleHtmlSpan(Double value) {
		if (value == null) {
			return "";
		}
		return "<span>" + String.format(locale, "%.1f", value) + "</span>";
	}

	private void initScoringColumns() {

		List<ProcessingUnit> units = getResultContainer().getSearchParameter()
				.getRequestedProcessUnits();

		Set<Column<?, ?>> scoringColumns = new HashSet<>();

		if (units.contains(ProcessingUnit.MASS_SCREENING)) {
			scoringColumns.add(grid.addColumn(
					processCandidate -> processCandidate.getMassSearchResult().getScore().getScoreValue())
					.setWidth(LayoutConstants.COLUMN_WIDTH_TINY)
					.setCaption(i18n.get(UIMessageKeys.PROCESSING_RESULT_COLUMN_MASS_SCREENING_SCORE))
					.setRenderer(new ProgressBarRenderer()));
		}

		if (isProcessExecuted(ProcessingUnit.RTI_SCREENING)) {
			scoringColumns.add(grid.addColumn(
					processCandidate -> processCandidate.getIndexSearchResult().getScore().getScoreValue())
					.setWidth(LayoutConstants.COLUMN_WIDTH_TINY)
					.setCaption(i18n.get(UIMessageKeys.PROCESSING_RESULT_COLUMN_RTI_SCREENING_SCORE))
					.setStyleGenerator(item -> CustomValoTheme.TEXT_CENTER)
					.setRenderer(new ScoreRenderer(createExclamationTriangle(i18n.get(UIMessageKeys.PROCESSING_RESULTS_NO_RTI_SCORE)))));
		}

		if (units.contains(ProcessingUnit.MSMS)) {
			scoringColumns.add(grid.addColumn(
					processCandidate -> processCandidate.getMsMsCandidate().getScore().getScoreValue())
					.setWidth(LayoutConstants.COLUMN_WIDTH_TINY)
					.setCaption(i18n.get(UIMessageKeys.PROCESSING_FORM_MSMS_SCORE_WEIGHT))
					.setStyleGenerator(item -> CustomValoTheme.TEXT_CENTER)
					.setRenderer(new ScoreRenderer(createExclamationTriangle(i18n.get(UIMessageKeys.PROCESSING_RESULTS_NO_MSMS_SCORE)))));
		}

		if (units.contains(ProcessingUnit.MASSBANK_SIMPLE)) {
			scoringColumns.add(grid.addColumn(
					processCandidate -> processCandidate.getMassBankSimpleScore().getScoreValue())
					.setWidth(LayoutConstants.COLUMN_WIDTH_TINY)
					.setCaption(i18n.get(UIMessageKeys.PROCESSING_RESULT_COLUMN_MASSBANK_SCORE))
					.setRenderer(new ProgressBarRenderer()));
		}

		if (scoringColumns.size() > 1) {
			Column<?, ?>[] columnArray = scoringColumns.toArray(new Column<?, ?>[0]);
			groupingRow.join(columnArray).setText(i18n.get(UIMessageKeys.PROCESSING_RESULT_TOP_COLUMN_SCORES));
		}
	}

	private void initMassScreeningColumns() {
		Column<ProcessCandidate, Double> targetMassColumn = grid.addFilterColumn(
				processCandidate -> processCandidate.getMassSearchResult().getTargetMass())
				.setFilterType(Double.class)
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setCaption(i18n.get(UIMessageKeys.FILE_SEARCH_COMPONENT_COLUMN_TARGET_MASS))
				.setRenderer(GridRendererProvider.getLocalizedRenderer(4));

		Column<ProcessCandidate, Double> deltaMassColumn = grid.addFilterColumn(
				processCandidate -> processCandidate.getMassSearchResult().getDeltaMass())
				.setFilterType(Double.class)
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setCaption(i18n.get(UIMessageKeys.FILE_SEARCH_COMPONENT_COLUMN_DELTA_MASS))
				.setRenderer(GridRendererProvider.getLocalizedRenderer(4));

		groupingRow.join(targetMassColumn, deltaMassColumn)
				.setText("Mass Screening");
	}

	private void initRtiScreeningColumns() {

		Column<ProcessCandidate, Double> rtiColumn = grid.addFilterColumn(
				processCandidate -> (processCandidate.getIndexSearchResult()).getRetentionTimeIndex())
				.setFilterType(Double.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM).setCaption("RTI")
				.setRenderer(GridRendererProvider.getLocalizedRenderer(1));

		Column<ProcessCandidate, Double> resultSignalColumn = grid.addFilterColumn(
				processCandidate -> (processCandidate.getEntry().getHenryBond().getValue()))
				.setFilterType(Double.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setCaption("Result Henry Konstante")
//				.setCaption(i18n.get(UIMessageKeys.RTI_RESULT_COLUMN_RESULT_LOG_D))
				.setRenderer(GridRendererProvider.getLocalizedRenderer(2));

		Column<ProcessCandidate, Double> targetSignalColumn = grid.addFilterColumn(
				processCandidate -> (processCandidate.getIndexSearchResult().getRetentionTimeSignal()))
				.setFilterType(Double.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setCaption("Target Henry Konstante")
//				.setCaption(i18n.get(UIMessageKeys.RTI_RESULT_COLUMN_TARGET_LOG_D))
				.setRenderer(GridRendererProvider.getLocalizedRenderer(2));

		Column<ProcessCandidate, Double> deltaLogDRtiDbColumn = grid.addFilterColumn(
				processCandidate -> (processCandidate.getIndexSearchResult().getDeltaRetentionTimeSignal()))
				.setFilterType(Double.class)
				.setHidable(true).setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setRenderer(GridRendererProvider.getLocalizedRenderer(2))
				.setCaption("Delta Henry");

		groupingRow.join(rtiColumn, resultSignalColumn, targetSignalColumn, deltaLogDRtiDbColumn).setText("RTI Screening");
	}

	private void initEntryColumns() {

		Column<ProcessCandidate, String> casColumn = grid.addFilterColumn(
				processCandidate -> processCandidate.getEntry().getCas().getValue())
				.setFilterType(String.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setCaption(i18n.get(UIMessageKeys.SEARCH_RESULT_GRID_COLUMN_CAS));

		Column<ProcessCandidate, String> smilesColumn = grid.addFilterColumn(
				processCandidate -> processCandidate.getEntry().getSmiles().getValue())
				.setFilterType(String.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_LARGE)
				.setCaption(i18n.get(UIMessageKeys.SEARCH_RESULT_GRID_COLUMN_SMILES));

		Column<ProcessCandidate, String> iupacColumn = grid.addFilterColumn(
				processCandidate -> processCandidate.getEntry().getIupac().getValue())
				.setFilterType(String.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_LARGE)
				.setCaption(i18n.get(UIMessageKeys.SEARCH_RESULT_GRID_COLUMN_IUPAC));

		Column<ProcessCandidate, String> formulaColumn = grid.addFilterColumn(
				processCandidate -> processCandidate.getEntry().getElementalFormula().getValue())
				.setFilterType(String.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setCaption(i18n.get(UIMessageKeys.SEARCH_RESULT_GRID_COLUMN_FORMULA));

		Column<ProcessCandidate, String> tonnageColumn = grid.addFilterColumn(
				processCandidate -> processCandidate.getEntry().getTonnage().getValue())
				.setFilterType(String.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setCaption(i18n.get(UIMessageKeys.SEARCH_RESULT_GRID_COLUMN_TONNAGE));

		Column<ProcessCandidate, String> dataSourceColumn = grid.addFilterColumn(processCandidate -> {
			Entry entry = processCandidate.getEntry();
			String datasourceName = entry.getDatasourceName();
			if (!entry.getSourceLists().isEmpty()) {
				datasourceName += " [" + entry.getSourceLists().stream()
						.map(SourceList::getName)
						.collect(Collectors.joining(",")) + "]";
			}
			return datasourceName;
		})
				.setFilterType(String.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setCaption(i18n.get(UIMessageKeys.SEARCH_RESULT_GRID_COLUMN_DATASOURCE));

		groupingRow.join(casColumn, smilesColumn, iupacColumn, formulaColumn, tonnageColumn, dataSourceColumn)
				.setText(i18n.get(UIMessageKeys.PROCESSING_RESULT_TOP_COLUMN_SUBSTANCE));
	}

	private String createExclamationTriangle(String errorMessage) {
		return "<div class=\"colored-icon-yellow\" title=\"" + errorMessage + "\">"
				+ VaadinIcons.WARNING.getHtml() + "</div>";
	}

	private void toggleDetails(ProcessCandidate item) {
		boolean isVisible = grid.isDetailsVisible(item);
		grid.setDetailsVisible(item, !isVisible);
	}

	private boolean isProcessExecuted(ProcessingUnit processingUnit) {
		ProcessingResultContainer resultContainer = getResultContainer();
		return resultContainer != null &&
				resultContainer.getSearchParameter().getRequestedProcessUnits().contains(processingUnit);
	}

	@Override
	protected void resultsContainerChanged() {

		initColumns();

		grid.setListDataProvider(DataProvider.ofCollection(results));
		grid.getDataProvider().addDataProviderListener(listener -> updateResultsCountLabel());

		updateResultsCountLabel();
		updateFrozenColumns();
	}

	private void updateResultsCountLabel() {
		resultsLabel.setValue(i18n.get(UIMessageKeys.RESULTS_COUNT_PARAMETERIZED,
				results.size(), grid.getDataProvider().size(new Query<>())));
	}

	@Override
	protected void select(ProcessCandidate item) {
		grid.select(item);
	}

	@Override
	protected void clearFilter() {
		grid.clearFilter();
	}

	@Override
	public String getTitle() {
		return i18n.get(UIMessageKeys.FILE_SEARCH_VIEW_RESULTS_TITLE);
	}

	@Override
	protected List<ProcessCandidate> getSelectedResults() {
		if (results == null) {
			return Collections.emptyList();
		}

		return results;
	}

	@Override
	protected List<Column<ProcessCandidate, ?>> getColumns() {
		return grid.getColumns();
	}

	@Override
	protected int getMinScreenWidthForFrozenColumn() {
		return MIN_SCREEN_WIDTH_FOR_FROZEN_COLUMN;
	}

	@Override
	protected boolean hasFrozenColumns() {
		return true;
	}

	@Override
	protected void setFrozenColumns(boolean frozen) {
		frozenColumns = frozen;
		if (grid != null) {
			updateFrozenColumns();
		}
	}

	private void updateFrozenColumns() {
		if (frozenColumns) {
			//TODO Readd when Vaadin Bug #10653 fix is released
//			grid.setFrozenColumnCount(FROZEN_COLUMNS);
		} else {
			grid.setFrozenColumnCount(0);
		}
	}

	private void showSummaryWindow() {
		eventBus.publish(EventBusTopics.OPEN_SUMMARY_WINDOW, this, DummyPayload.INSTANCE);
	}
}