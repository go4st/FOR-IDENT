package de.hswt.fi.ui.vaadin.handler.search;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.ui.vaadin.handler.SingleEntryDownloadHandler;
import de.hswt.fi.ui.vaadin.handler.SingleEntryReportHandler;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@ViewScope
@SuppressWarnings("unused")
public class SearchViewHandler {

	@Autowired
	private SearchHandler searchHandler;

	@Autowired
	private SearchSelectionHandler searchSelectionHandler;

	@Autowired
	private SearchEntrySelectionHandler searchEntrySelectionHandler;

	@Autowired
	private SearchClearHistoryHandler searchClearHistoryHandler;

	@Autowired
	private SingleEntryDownloadHandler singleEntryDownloadHandler;

	@Autowired
	private SearchDownloadHandler searchDownloadHandler;

	@Autowired
	private SingleEntryReportHandler singleEntryReportHandler;

	@Autowired
	private SearchHistoryReportHandler searchReportHandler;

	@Autowired
	private SearchResultsReportHandler searchEntriesReportHandler;

}
