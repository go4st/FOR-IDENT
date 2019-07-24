package de.hswt.fi.ui.vaadin.handler.search;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.beans.BeanComponentMapper;
import de.hswt.fi.search.service.mass.search.model.Entry;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DownloadRequestPayload;
import de.hswt.fi.ui.vaadin.handler.AbstractDownloadHandler;
import de.hswt.fi.ui.vaadin.views.states.SearchViewState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;

import java.util.List;

@SpringComponent
@ViewScope
public class SearchDownloadHandler extends AbstractDownloadHandler<Entry> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchDownloadHandler.class);

	private final SearchViewState viewState;

	private final BeanComponentMapper mapper;

	private final I18N i18N;

	@Autowired
	public SearchDownloadHandler(SearchViewState viewState, BeanComponentMapper mapper, I18N i18N) {
		this.viewState = viewState;
		this.mapper = mapper;
		this.i18N = i18N;
	}

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_REQUEST_RESULT_DOWNLOAD)
	private void handleEvent(DownloadRequestPayload<Entry> payload) {
		LOGGER.debug("entering event bus listener handleEvent with payload {} topic{}", payload,
				EventBusTopics.TARGET_HANDLER_REQUEST_RESULT_DOWNLOAD);

		List<Entry> entries = viewState.getCurrentSearch().getResultsContainer();

		if (entries.isEmpty()) {
			return;
		}

		List<String> possibleColumnsProperties = mapper.getSelectorColumns(entries.iterator().next());
		List<String> filteredExportColumnIds = possibleColumnsProperties;

		downloadRecords(entries, filteredExportColumnIds, possibleColumnsProperties, i18N.get(UIMessageKeys.SEARCH_HISTORY_TREE_ITEM_SEARCH));
	}
}
