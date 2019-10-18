package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import com.vaadin.ui.themes.ValoTheme;
import de.hswt.fi.search.service.mass.search.model.Entry;
import de.hswt.fi.search.service.mass.search.model.SearchParameter;
import de.hswt.fi.search.service.mass.search.model.SearchResult;
import de.hswt.fi.search.service.mass.search.model.SourceList;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.container.SearchResultContainer;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import de.hswt.fi.ui.vaadin.grid.FilterGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.i18n.I18N;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SpringComponent
@ViewScope
public class SearchResultsComponent
		extends AbstractResultsComponent<SearchParameter, SearchResult, Entry, SearchResultContainer> {

	private static final long serialVersionUID = 6326600623156331195L;

	private final ComponentFactory componentFactory;

	private FilterGrid<Entry> grid;

	private Label resultsLabel;

	@Autowired
	public SearchResultsComponent(EventBus.ViewEventBus eventBus, I18N i18n, ComponentFactory componentFactory) {
		super(eventBus, i18n);
		this.componentFactory = componentFactory;
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

		Button clearFilterButton = componentFactory.createButton(VaadinIcons.FILTER,
				i18n.get(UIMessageKeys.CLEAR_FILTER_BUTTON_CAPTION));
		clearFilterButton.addClickListener(e -> clearFilter());
		headerLayout.addComponent(clearFilterButton);

			Button downloadButton = componentFactory.createButton(VaadinIcons.DOWNLOAD,
					i18n.get(UIMessageKeys.DOWNLOAD_BUTTON_CAPTION));
			downloadButton.addClickListener(e -> handleResultDownload());
			headerLayout.addComponent(downloadButton);

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
		setSizeFull();
		initGrid();
		initColumns();
	}

	private void initGrid() {
		grid = new FilterGrid<>(Entry.class);
		grid.setSizeFull();
		grid.setHeightMode(HeightMode.CSS);
		grid.setSelectionMode(SelectionMode.SINGLE);

		((SingleSelectionModel) grid.getSelectionModel()).setDeselectAllowed(false);
		
		grid.setPrimaryStyleName(CustomValoTheme.GRID_CUSTOM);

		grid.addSelectionListener(event -> {

			Entry item = event.getFirstSelectedItem().orElse(null);
			
			if (item == null) {
				return;
			}
			selectionChanged(item);
		});

		grid.removeAllColumns();

		addComponent(grid);
	}

	private void initColumns() {

		grid.addFilterColumn(entry -> entry.getName().getValue())
				.setFilterType(String.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_LARGE)
				.setCaption(i18n.get(UIMessageKeys.SEARCH_RESULT_GRID_COLUMN_NAME));

		grid.addFilterColumn(entry -> entry.getCas().getValue())
				.setFilterType(String.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setCaption(i18n.get(UIMessageKeys.SEARCH_RESULT_GRID_COLUMN_CAS));

		grid.addFilterColumn(entry -> entry.getSmiles().getValue())
				.setFilterType(String.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_LARGE)
				.setCaption(i18n.get(UIMessageKeys.SEARCH_RESULT_GRID_COLUMN_SMILES));

		grid.addFilterColumn(entry -> entry.getIupac().getValue())
				.setFilterType(String.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_LARGE)
				.setCaption(i18n.get(UIMessageKeys.SEARCH_RESULT_GRID_COLUMN_IUPAC));

		grid.addFilterColumn(entry -> entry.getElementalFormula().getValue())
				.setFilterType(String.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setCaption(i18n.get(UIMessageKeys.SEARCH_RESULT_GRID_COLUMN_FORMULA));

		grid.addFilterColumn(entry -> entry.getAccurateMass().getValue())
				.setFilterType(Double.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setCaption(i18n.get(UIMessageKeys.SEARCH_RESULT_GRID_COLUMN_MASS));

		grid.addFilterColumn(entry -> entry.getHenryBond().getValue())
				.setFilterType(Double.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setCaption(i18n.get(UIMessageKeys.SEARCH_RESULT_GRID_COLUMN_HENRY_BOND));

		grid.addFilterColumn(entry -> entry.getHenryGroup().getValue())
				.setFilterType(Double.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setCaption(i18n.get(UIMessageKeys.SEARCH_RESULT_GRID_COLUMN_HENRY_GROUP));

		grid.addFilterColumn(entry -> entry.getHenryExper().getValue())
				.setFilterType(Double.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setCaption(i18n.get(UIMessageKeys.SEARCH_RESULT_GRID_COLUMN_HENRY_EXPER));

		grid.addFilterColumn(entry -> entry.getTonnage().getValue())
				.setFilterType(String.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_LARGE)
				.setCaption(i18n.get(UIMessageKeys.SEARCH_RESULT_GRID_COLUMN_TONNAGE));

		grid.addFilterColumn(entry -> {
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
				.setWidth(LayoutConstants.COLUMN_WIDTH_LARGE)
				.setCaption(i18n.get(UIMessageKeys.SEARCH_RESULT_GRID_COLUMN_DATASOURCE));
	}

	@Override
	protected void resultsContainerChanged() {

		clearFilter();

		grid.setListDataProvider(DataProvider.ofCollection(results));
		grid.getDataProvider().addDataProviderListener(event -> updateResultsCountLabel());
		grid.recalculateColumnWidths();

		updateResultsCountLabel();
	}

	private void updateResultsCountLabel() {
		resultsLabel.setValue(i18n.get(UIMessageKeys.RESULTS_COUNT_PARAMETERIZED,
				results.size(), grid.getDataProvider().size(new Query<>())));
	}

	@Override
	protected void select(Entry entry) {
		grid.select(entry);
	}

	@Override
	protected void clearFilter() {
		grid.clearFilter();
	}

	@Override
	public String getTitle() {
		return i18n.get(UIMessageKeys.SEARCH_VIEW_RESULTS_TITLE);
	}

	@Override
	protected List<Entry> getSelectedResults() {
		return results == null ? Collections.emptyList() : results;
	}

	@Override
	protected List<Column<Entry, ?>> getColumns() {
		return grid.getColumns();
	}

	@Override
	protected boolean isNonResultsPossible() {
		return false;
	}
	
	@Override
	protected void setFrozenColumns(boolean frozen) {
		//TODO Not needed maybe remove
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		SearchResultsComponent that = (SearchResultsComponent) o;
		return Objects.equals(componentFactory, that.componentFactory) &&
				Objects.equals(grid, that.grid) &&
				Objects.equals(resultsLabel, that.resultsLabel);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), componentFactory, grid, resultsLabel);
	}
}
