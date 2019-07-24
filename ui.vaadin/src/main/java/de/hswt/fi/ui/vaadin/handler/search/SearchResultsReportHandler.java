package de.hswt.fi.ui.vaadin.handler.search;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.handler.AbstractEntryReportHandler;
import de.hswt.fi.ui.vaadin.views.states.SearchViewState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

import java.util.List;

@SpringComponent
@ViewScope
public class SearchResultsReportHandler extends AbstractEntryReportHandler {

	private static final long serialVersionUID = 3033061802993063270L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchResultsReportHandler.class);

	private final SearchViewState viewState;

	@Autowired
	public SearchResultsReportHandler(SearchViewState viewState) {
		this.viewState = viewState;
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_REPORT_TO_STAFF)
	private void handleEvent(List<Object> entries) {
		LOGGER.debug("entering event bus listener handleEvent width payload list size({}) in topic {}",
				entries.size(), EventBusTopics.TARGET_HANDLER_REPORT_TO_STAFF);

		if (viewState.getCurrentSearch().getResultsContainer().isEmpty()) {
			return;
		}

		reportEntries(viewState.getCurrentSearch().getResultsContainer());
	}
	
	@Override
	protected String getSubject() {
		return "Bug report [Search Results " + errorNumber + "]";
	}

}
