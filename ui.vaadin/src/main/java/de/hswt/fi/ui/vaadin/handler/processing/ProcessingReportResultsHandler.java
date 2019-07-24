package de.hswt.fi.ui.vaadin.handler.processing;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.processing.service.model.ProcessCandidate;
import de.hswt.fi.search.service.mass.search.model.Entry;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.handler.AbstractEntryReportHandler;
import de.hswt.fi.ui.vaadin.views.states.ProcessingViewState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

import java.util.List;
import java.util.stream.Collectors;

@SpringComponent
@ViewScope
public class ProcessingReportResultsHandler extends AbstractEntryReportHandler {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(ProcessingReportResultsHandler.class);

	@Autowired
	private ProcessingViewState viewState;

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_REPORT_TO_STAFF)
	private void handleEvent(List<Object> entryIds) {
		LOG.debug("entering event bus listener handleEvent width payload list size({}) in topic {}",
				entryIds.size(), EventBusTopics.TARGET_HANDLER_REPORT_TO_STAFF);

		if (entryIds.isEmpty()) {
			return;
		}

		List<ProcessCandidate> container = viewState.getCurrentSearch().getResultsContainer();

		List<Entry> entries = container.stream().filter(e -> e.getEntry() != null)
				.map(e -> e.getEntry()).collect(Collectors.toList());

		reportEntries(entries);
	}

	@Override
	protected String getSubject() {
		return "Bug report [Processing results " + errorNumber + "]";
	}
}
