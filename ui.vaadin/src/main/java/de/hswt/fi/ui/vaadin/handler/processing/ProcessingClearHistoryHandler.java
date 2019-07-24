package de.hswt.fi.ui.vaadin.handler.processing;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.processing.service.model.ProcessCandidate;
import de.hswt.fi.processing.service.model.ProcessingJob;
import de.hswt.fi.processing.service.model.ProcessingResult;
import de.hswt.fi.ui.vaadin.container.ProcessingResultContainer;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DummyPayload;
import de.hswt.fi.ui.vaadin.handler.AbstractClearHistoryHandler;
import de.hswt.fi.ui.vaadin.views.states.ProcessingViewState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

@SpringComponent
@ViewScope
public class ProcessingClearHistoryHandler extends
        AbstractClearHistoryHandler<ProcessingJob, ProcessingResult, ProcessCandidate, ProcessingResultContainer> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(ProcessingClearHistoryHandler.class);

	@Autowired
	private ProcessingViewState viewState;

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_CLEAR_HISTORY)
	private void handleClearHistory(DummyPayload payload) {
		LOG.debug("entering event bus listener handleClearHistory with payload ({}) in topic {}",
				payload, EventBusTopics.TARGET_HANDLER_CLEAR_HISTORY);

		clear(viewState.getSearchHistoryContainer());

		viewState.setCurrentSearch(null);

		LOG.debug("publish session event handleClearHistory width topic {}",
				EventBusTopics.SOURCE_HANDLER_CLEARED_PROCESSING_SEARCH_HISTORY);
		eventBus.publish(EventScope.SESSION,
				EventBusTopics.SOURCE_HANDLER_CLEARED_PROCESSING_SEARCH_HISTORY, this,
				DummyPayload.INSTANCE);
	}
}
