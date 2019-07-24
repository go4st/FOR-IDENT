package de.hswt.fi.ui.vaadin.handler.search;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.search.service.mass.search.model.Entry;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.handler.AbstractHandler;
import de.hswt.fi.ui.vaadin.views.states.SearchViewState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

@SpringComponent
@ViewScope
public class SearchEntrySelectionHandler extends AbstractHandler<EventBus.ViewEventBus> {

	private static final long serialVersionUID = -4939479199673931252L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchEntrySelectionHandler.class);

	private final SearchViewState viewState;

	@Autowired
	public SearchEntrySelectionHandler(SearchViewState viewState) {
		this.viewState = viewState;
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_SELECT_ENTRY)
	private void handleEvent(Entry currentSelection) {
		LOGGER.debug("entering event bus listener handleEvent with entry {} and topic {}",
				currentSelection, EventBusTopics.TARGET_HANDLER_SELECT_ENTRY);

		if (viewState.getCurrentSearch() == null || currentSelection.equals(viewState.getCurrentSearch().getCurrentSelection())) {
			return;
		}

		viewState.getCurrentSearch().setCurrentSelection(currentSelection);

		LOGGER.debug("publish event handleEntrySelection with topic {}",
				EventBusTopics.SOURCE_HANDLER_ENTRY_SELECTED);
		eventBus.publish(EventBusTopics.SOURCE_HANDLER_ENTRY_SELECTED, this, currentSelection);
	}
}
