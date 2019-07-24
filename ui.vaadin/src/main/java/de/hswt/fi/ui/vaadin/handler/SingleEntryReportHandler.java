package de.hswt.fi.ui.vaadin.handler;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.search.service.mass.search.model.Entry;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

import java.util.Collections;

@SpringComponent
@ViewScope
public class SingleEntryReportHandler extends AbstractEntryReportHandler {

	private static final long serialVersionUID = -1655042482998047541L;

	private static final Logger LOG = LoggerFactory.getLogger(SingleEntryReportHandler.class);

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_REPORT_TO_STAFF)
	private void handleEvent(Entry entry) {
		LOG.debug("entering event bus listener handleEvent width payload {} in topic {}", entry,
				EventBusTopics.TARGET_HANDLER_REPORT_TO_STAFF);

		reportEntries(Collections.singletonList(entry));
	}
	
	@Override
	protected String getSubject() {
		return "Bug report [Entry " + errorNumber + "]";
	}

}
