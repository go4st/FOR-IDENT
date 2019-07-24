package de.hswt.fi.ui.vaadin.views;

import de.hswt.fi.ui.vaadin.handler.search.SearchViewHandler;
import de.hswt.fi.ui.vaadin.views.components.DatabaseSourceComponent;
import de.hswt.fi.ui.vaadin.views.components.SearchDetailsComponent;
import de.hswt.fi.ui.vaadin.views.components.SearchHistoryTreeComponent;
import de.hswt.fi.ui.vaadin.views.components.SearchResultsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractSearchView extends AbstractLayoutView {

	private static final long serialVersionUID = -3686561714769024691L;

	public static final String VIEW_NAME = "indexsearch";

	private static final Logger LOG = LoggerFactory.getLogger(AbstractSearchView.class);

	// Do not remove. In order to be created by Spring, this bean needs to wired
	// here.
	@SuppressWarnings("unused")
	@Autowired
	private SearchViewHandler searchViewHandler;

	@Autowired
	private DatabaseSourceComponent databaseSourceComponent;

	@Autowired
	private SearchHistoryTreeComponent searchHistoryComponent;

	@Autowired
	private SearchDetailsComponent detailsComponent;

	@Autowired
	private SearchResultsComponent resultsComponent;

	@Override
	protected void initComponents() {

		initSourceListsComponent();
		initSearchHistoryComponent();
		initDetailsHeaderComponent();
		initResultsHeaderComponent();

		LOG.info("leaving method initComponents");

	}

	private void initSourceListsComponent() {
		addSourceListsComponent(databaseSourceComponent);
	}

	private void initSearchHistoryComponent() {
		addSearchHistoryComponent(searchHistoryComponent);
	}

	private void initDetailsHeaderComponent() {
		addDetailsComponent(detailsComponent);
	}

	private void initResultsHeaderComponent() {
		addResultsComponent(resultsComponent);
	}

	@Override
	protected Layout getDefaultLayout() {
		return Layout.COLUMN;
	}

	// Do not override hash() and equals() in abstract component classes because of identity issues when attach / remove
}
