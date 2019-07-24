package de.hswt.fi.ui.vaadin.handler.processing;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.beans.BeanComponentMapper;
import de.hswt.fi.export.service.excel.ExcelSheetDefinition;
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
public class ProcessingResultsDownloadHandler extends AbstractDownloadHandler<ProcessCandidate> {

	private static final long serialVersionUID = -7188455301426872131L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingResultsDownloadHandler.class);

	private final BeanComponentMapper mapper;

	private final ProcessingViewState viewState;

	private final I18N i18n;

	@Autowired
	public ProcessingResultsDownloadHandler(BeanComponentMapper mapper, ProcessingViewState viewState, I18N i18n) {
		this.mapper = mapper;
		this.viewState = viewState;
		this.i18n = i18n;
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_REQUEST_RESULT_DOWNLOAD)
	private void handleEvent(DownloadRequestPayload payload) {
		LOGGER.debug("entering event bus listener handleEvent with payload {} topic {}", payload,
				EventBusTopics.TARGET_HANDLER_REQUEST_RESULT_DOWNLOAD);

		ProcessingResultContainer currentSearch = viewState.getCurrentSearch();
		ProcessingJob currentJob = currentSearch.getSearchParameter();

		List<ProcessCandidate> entries = currentSearch.getResultsContainer();

		if (entries.isEmpty()) {
			return;
		}

		List<String> possibleColumnsProperties = mapper.getSelectorColumns(entries.iterator().next());
		filterNullBeanColumns(possibleColumnsProperties, entries.iterator().next());
		List<String> preselectedColumnProperties = possibleColumnsProperties;

		List<ExcelSheetDefinition> optionalSheets = new ArrayList<>();
		optionalSheets.add(createParameterSheet(currentJob.getSettings()));
		optionalSheets.add(createScoreSettingsSheet(currentJob.getSettings().getScoreSettings()));
		
		downloadRecords(entries, preselectedColumnProperties, possibleColumnsProperties, optionalSheets,
				i18n.get(UIMessageKeys.RESULTS_DOWNLOAD_HANDLER_EXCEL_SPREADSHEET_DEFINITION_RESULTS),
				currentJob.getFeatureSet().getSourceFilePath(), false, true);
	}
}
