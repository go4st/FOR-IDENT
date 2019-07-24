package de.hswt.fi.ui.vaadin.handler.processing;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.beans.BeanComponentMapper;
import de.hswt.fi.export.service.excel.ExcelSheetDefinition;
import de.hswt.fi.model.Feature;
import de.hswt.fi.processing.service.model.ProcessCandidate;
import de.hswt.fi.processing.service.model.ProcessingJob;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.container.ProcessingResultContainer;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DummyPayload;
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
public class ProcessingNonResultsDownloadHandler extends AbstractDownloadHandler<Feature> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProcessingNonResultsDownloadHandler.class);

	private final ProcessingViewState viewState;

	private final BeanComponentMapper mapper;

	private final I18N i18n;

	@Autowired
	public ProcessingNonResultsDownloadHandler(ProcessingViewState viewState, BeanComponentMapper mapper, I18N i18n) {
		this.viewState = viewState;
		this.mapper = mapper;
		this.i18n = i18n;
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_REQUEST_NON_RESULT_DOWNLOAD)
	private void handleEvent(DummyPayload payload) {
		LOGGER.debug("entering event bus listener handleEvent topic {} with DummyPayload",
				EventBusTopics.TARGET_HANDLER_REQUEST_NON_RESULT_DOWNLOAD);

		ProcessingResultContainer currentSearch = viewState.getCurrentSearch();
		ProcessingJob currentJob = currentSearch.getSearchParameter();

		List<ProcessCandidate> resultsContainer = currentSearch
				.getResultsContainer();
		List<String> resultTargetIdentifier = getResultTargetIdentifier(resultsContainer);
		List<String> allTargetIdentifier = getAllTargetIdentifier(
				currentJob.getFeatureSet().getFeatures());
		allTargetIdentifier.removeAll(resultTargetIdentifier);

		if (allTargetIdentifier.isEmpty()) {
			return;
		}

		List<Feature> entries = new ArrayList<>(currentJob.getFeatureSet().getFeatures());
		List<Feature> nonResultEntries = new ArrayList<>();

		for (Feature targetData : entries) {
			for (String targetIdentifier : allTargetIdentifier) {
				if (targetData.getIdentifier().equals(targetIdentifier)) {
					nonResultEntries.add(targetData);
				}
			}
		}

		List<String> possibleColumnsProperties = mapper.getSelectorColumns(nonResultEntries.iterator().next());

		List<ExcelSheetDefinition> optionalSheets = new ArrayList<>();
		optionalSheets.add(createParameterSheet(currentJob.getSettings()));
		optionalSheets.add(createScoreSettingsSheet(currentJob.getSettings().getScoreSettings()));

		downloadRecords(nonResultEntries, possibleColumnsProperties, possibleColumnsProperties, optionalSheets, i18n.get(UIMessageKeys.RESULTS_DOWNLOAD_HANDLER_EXCEL_SPREADSHEET_DEFINITION_NON_RESULTS),
				currentJob.getFeatureSet().getSourceFilePath(), false, false);
	}
}
