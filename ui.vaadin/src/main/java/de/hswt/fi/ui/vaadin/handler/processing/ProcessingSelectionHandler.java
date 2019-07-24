package de.hswt.fi.ui.vaadin.handler.processing;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.ui.vaadin.container.ProcessingResultContainer;
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
public class ProcessingSelectionHandler extends AbstractHandler<ViewEventBus> {

	private static final long serialVersionUID = 1L;

	private static Logger LOGGER = LoggerFactory.getLogger(ProcessingSelectionHandler.class);

	@Autowired
	private ProcessingViewState viewState;

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_SELECT_CONTAINER)
	private void handleEvent(ProcessingResultContainer newContainer) {
		LOGGER.debug("entering event bus listener handleEvent with payload {} and topic {}",
				newContainer, EventBusTopics.TARGET_HANDLER_SELECT_CONTAINER);

		if (newContainer.equals(viewState.getCurrentSearch())) {
			return;
		}

		// call search handler, this will select the right search container
		LOGGER.debug("publish event handleEntrySelection with payload {}",
				viewState.getCurrentSearch().getSearchParameter());
		eventBus.publish(this, newContainer.getSearchParameter());
	}
}
