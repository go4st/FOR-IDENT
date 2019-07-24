package de.hswt.fi.ui.vaadin.handler.search;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.search.service.mass.search.model.Entry;
import de.hswt.fi.search.service.mass.search.model.SearchParameter;
import de.hswt.fi.search.service.mass.search.model.SearchResult;
import de.hswt.fi.ui.vaadin.container.SearchResultContainer;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DummyPayload;
import de.hswt.fi.ui.vaadin.handler.AbstractClearHistoryHandler;
import de.hswt.fi.ui.vaadin.views.states.SearchViewState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

@SpringComponent
@ViewScope
public class SearchClearHistoryHandler
		extends AbstractClearHistoryHandler<SearchParameter, SearchResult, Entry, SearchResultContainer> {

	private static final long serialVersionUID = -6997520812013872390L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchClearHistoryHandler.class);

	private final SearchViewState viewState;

	@Autowired
	public SearchClearHistoryHandler(SearchViewState viewState) {
		this.viewState = viewState;
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_CLEAR_HISTORY)
	private void handleClearHistory(DummyPayload payload) {
		LOGGER.debug("entering event bus listener handleClearHistory with payload ({}) in topic {}",
				payload, EventBusTopics.TARGET_HANDLER_CLEAR_HISTORY);

		clear(viewState.getSearchHistoryContainer());

		LOGGER.debug("publish session event handleClearHistory width topic {}",
				EventBusTopics.SOURCE_HANDLER_CLEARED_SEARCH_HISTORY);
		eventBus.publish(EventScope.SESSION, EventBusTopics.SOURCE_HANDLER_CLEARED_SEARCH_HISTORY,
				this, DummyPayload.INSTANCE);
	}
}
