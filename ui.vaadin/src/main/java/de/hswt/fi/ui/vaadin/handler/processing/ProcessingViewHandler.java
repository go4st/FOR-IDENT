package de.hswt.fi.ui.vaadin.handler.processing;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.ui.vaadin.handler.*;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@ViewScope
@SuppressWarnings("unused")
public class ProcessingViewHandler {

	@Autowired
	private ProcessingImportHandler processingImportHandler;

	@Autowired
	private ProcessingReloadHandler processingReloadHandler;

	@Autowired
	private ProcessingHandler processingProcessHandler;

	@Autowired
	private ProcessingSelectionHandler processingSelectionHandler;

	@Autowired
	private ProcessingEntrySelectionHandler processingEntrySelectionHandler;

	@Autowired
	private ProcessingResultsDownloadHandler processingDownloadHandler;
	
	@Autowired
	private ProcessingNonResultsDownloadHandler processingNonResultsDownloadHandler;

	@Autowired
	private ProcessingCompleteResultsDownloadHandler processingCompleteResultsDownloadHandler;
	
	@Autowired
	private ProcessingClearHandler processingClearHandler;

	@Autowired
	private ProcessingClearHistoryHandler processingClearHistoryHandler;

	@Autowired
	private ProcessingReportHandler processingReportHandler;

	@Autowired
	private ProcessingReportResultsHandler processingReportResultsHandler;
	
	@Autowired
	private FileUploadWindowHandler fileUploadWindowHandler;
	
	@Autowired
	private ShowSummaryWindowHandler showSummaryWindowHandler;

	@Autowired
	private SingleEntryReportHandler singleEntryReportHandler;

	@Autowired
	private SingleEntryDownloadHandler singleEntryDownloadHandler;
	
	@Autowired
	private ScoreWindowHandler scoreWindowHandler;
}