package de.hswt.fi.ui.vaadin.handler.processing;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.processing.service.model.ProcessCandidate;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.handler.AbstractHandler;
import de.hswt.fi.ui.vaadin.views.states.ProcessingViewState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

@SpringComponent
@ViewScope
public class ProcessingEntrySelectionHandler extends AbstractHandler<ViewEventBus> {

	private static final long serialVersionUID = 1L;

	private static Logger LOGGER = LoggerFactory.getLogger(ProcessingEntrySelectionHandler.class);

	@Autowired
	private ProcessingViewState viewState;

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_SELECT_ENTRY)
	private void handleEvent(ProcessCandidate currentSelection) {
		LOGGER.debug("entering event bus listener handleEvent with entry {} and topic {}",
				currentSelection, EventBusTopics.TARGET_HANDLER_SELECT_ENTRY);

		if (currentSelection.equals(viewState.getCurrentSearch().getCurrentSelection())) {
			return;
		}

		viewState.getCurrentSearch().setCurrentSelection(currentSelection);

		LOGGER.debug("publish event handleEntrySelection with topic {}",
				EventBusTopics.SOURCE_HANDLER_ENTRY_SELECTED);
		eventBus.publish(EventBusTopics.SOURCE_HANDLER_ENTRY_SELECTED, this, currentSelection);
	}
}
