package de.hswt.fi.ui.vaadin.handler;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.ui.vaadin.handler.search.SearchDownloadHandler;
import de.hswt.fi.ui.vaadin.handler.search.SearchHandler;
import de.hswt.fi.ui.vaadin.handler.search.SearchHistoryReportHandler;
import de.hswt.fi.ui.vaadin.handler.search.SearchResultsReportHandler;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@ViewScope
@SuppressWarnings("unused")
public class ViewScopeHandler {

	@Autowired
	private SearchHandler searchHandler;

	@Autowired
	private SearchHistoryReportHandler reportSearchHandler;

	@Autowired
	private SearchDownloadHandler downloadEntriesHandler;

	@Autowired
	private SearchResultsReportHandler reportSearchEntriesHandler;
	
}