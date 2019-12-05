package de.hswt.fi.ui.vaadin.handler.processing;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.beans.BeanComponentMapper;
import de.hswt.fi.export.service.excel.ExcelSheetDefinition;
import de.hswt.fi.export.service.excel.ExcelSheetDefinition.ColumnDirection;
import de.hswt.fi.model.Feature;
import de.hswt.fi.processing.service.model.ProcessCandidate;
import de.hswt.fi.processing.service.model.ProcessingJob;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.container.ProcessingResultContainer;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DownloadRequestPayload;
import de.hswt.fi.ui.vaadin.handler.AbstractDownloadHandler;
import de.hswt.fi.ui.vaadin.views.states.ProcessingViewState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;

import java.util.ArrayList;
import java.util.List;

@SpringComponent
@ViewScope
public class ProcessingCompleteResultsDownloadHandler extends AbstractDownloadHandler<ProcessCandidate> {

	private static final long serialVersionUID = -3563563261353371277L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingCompleteResultsDownloadHandler.class);
	private final ProcessingViewState viewState;
	private final BeanComponentMapper mapper;
	private final I18N i18n;

	@Autowired
	public ProcessingCompleteResultsDownloadHandler(ProcessingViewState viewState, BeanComponentMapper mapper, I18N i18n) {
		this.viewState = viewState;
		this.mapper = mapper;
		this.i18n = i18n;
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_REQUEST_COMPLETE_DOWNLOAD)
	private void handleEvent(DownloadRequestPayload payload) {
		LOGGER.debug("entering event bus listener handleEvent topic {} with payload " + payload,
				EventBusTopics.TARGET_HANDLER_REQUEST_COMPLETE_DOWNLOAD);

		ProcessingResultContainer currentSearch = viewState.getCurrentSearch();
		ProcessingJob currentJob = currentSearch.getSearchParameter();

		List<ProcessCandidate> results = currentSearch.getResultsContainer();

		if (results.isEmpty()) return;

		List<ProcessCandidate> resultsContainer = currentSearch.getResultsContainer();

		List<String> resultTargetIdentifier = getResultTargetIdentifier(resultsContainer);
		List<String> allTargetIdentifier = getAllTargetIdentifier(
				currentJob.getFeatureSet().getFeatures());
		allTargetIdentifier.removeAll(resultTargetIdentifier);

		List<Feature> nonResults = new ArrayList<>(currentJob.getFeatureSet().getFeatures());
		List<Feature> nonResultEntries = new ArrayList<>();

		for (Feature targetData : nonResults) {
			for (String targetIdentifier : allTargetIdentifier) {
				if (targetData.getIdentifier().equals(targetIdentifier)) {
					nonResultEntries.add(targetData);
				}
			}
		}

		List<String> possibleColumnsProperties = mapper.getSelectorColumns(results.iterator().next());

		filterNullBeanColumns(possibleColumnsProperties, results.iterator().next());
		List<String> preselectedColumnProperties = possibleColumnsProperties;

		ExcelSheetDefinition<Feature> nonResultsSheet = new ExcelSheetDefinition<>(
				i18n.get(UIMessageKeys.RESULTS_DOWNLOAD_HANDLER_EXCEL_SPREADSHEET_DEFINITION_NON_RESULTS));
		nonResultsSheet.setColumnDirection(ColumnDirection.VERTICAL);
		nonResultsSheet.setData(nonResultEntries);

		List<ExcelSheetDefinition> optionalSheets = new ArrayList<>();
		optionalSheets.add(nonResultsSheet);
		optionalSheets.add(createParameterSheet(currentJob.getSettings()));
		optionalSheets.add(createScoreSettingsSheet(currentJob.getSettings().getScoreSettings()));

		boolean addSourceFiles = currentJob.getFeatureSet().getSourceFilePath().endsWith("xls") || currentJob.getFeatureSet().getSourceFilePath().endsWith("xlsx");

		downloadRecords(results, preselectedColumnProperties, possibleColumnsProperties, optionalSheets, i18n.get(UIMessageKeys.RESULTS_DOWNLOAD_HANDLER_EXCEL_SPREADSHEET_DEFINITION_RESULTS),
				currentJob.getFeatureSet().getSourceFilePath(), addSourceFiles, true);
	}
}
