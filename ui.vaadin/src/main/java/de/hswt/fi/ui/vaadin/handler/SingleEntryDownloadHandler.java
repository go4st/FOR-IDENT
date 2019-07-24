package de.hswt.fi.ui.vaadin.handler;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.beans.BeanComponentMapper;
import de.hswt.fi.search.service.mass.search.model.Entry;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

import java.util.Collections;
import java.util.List;

@SpringComponent
@ViewScope
public class SingleEntryDownloadHandler extends AbstractDownloadHandler<Entry> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SingleEntryDownloadHandler.class);

	private final BeanComponentMapper mapper;

	@Autowired
	public SingleEntryDownloadHandler(BeanComponentMapper mapper) {
		this.mapper = mapper;
	}

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_REQUEST_RESULT_DOWNLOAD)
	private void handleEvent(Entry entry) {
		LOGGER.debug("entering event bus listener handleEvent with payload {} topic {}", entry,
				EventBusTopics.TARGET_HANDLER_REQUEST_RESULT_DOWNLOAD);

		List<String> possibleColumnsProperties = mapper.getSelectorColumns(entry);
		downloadRecords(Collections.singletonList(entry), possibleColumnsProperties, possibleColumnsProperties, entry.getName().getValue());
	}
}
