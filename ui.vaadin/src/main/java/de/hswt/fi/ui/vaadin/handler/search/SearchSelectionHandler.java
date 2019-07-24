package de.hswt.fi.ui.vaadin.handler.search;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.ui.vaadin.container.SearchResultContainer;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.handler.AbstractHandler;
import de.hswt.fi.ui.vaadin.views.states.SearchViewState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

@SpringComponent
@ViewScope
public class SearchSelectionHandler extends AbstractHandler<ViewEventBus> {

	private static final long serialVersionUID = 3177602205693173517L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchSelectionHandler.class);

	private final SearchViewState viewState;

	@Autowired
	public SearchSelectionHandler(SearchViewState viewState) {
		this.viewState = viewState;
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_SELECT_CONTAINER)
	private void handleEvent(SearchResultContainer newContainer) {
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
