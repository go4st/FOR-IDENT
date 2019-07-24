package de.hswt.fi.ui.vaadin.handler.processing;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.model.FeatureSet;
import de.hswt.fi.processing.service.api.ProcessingService;
import de.hswt.fi.processing.service.model.ProcessingJob;
import de.hswt.fi.ui.vaadin.configuration.SessionSharedObjects;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DummyPayload;
import de.hswt.fi.ui.vaadin.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

@SpringComponent
@ViewScope
public class ProcessingReloadHandler extends AbstractHandler<ViewEventBus> {

	private static final long serialVersionUID = 1L;

	private static Logger LOG = LoggerFactory.getLogger(ProcessingReloadHandler.class);

	private final ProcessingService processImportService;

	private final SessionSharedObjects sessionObjects;

	@Autowired
	public ProcessingReloadHandler(ProcessingService processImportService, SessionSharedObjects sessionObjects) {
		this.processImportService = processImportService;
		this.sessionObjects = sessionObjects;
	}

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_RELOAD_FILE_DATA)
	protected void handleEvent(DummyPayload payload) {
		LOG.debug("entering event bus listener handleEvent " + "with payload {}", payload);

		for (FeatureSet featureSet : sessionObjects.getAvailableProcessingData()) {
			ProcessingJob job = processImportService.getProcessingJob(featureSet);
			LOG.debug("publish event file added with payload {}", job);
			eventBus.publish(EventBusTopics.SOURCE_HANDLER_FILE_ADDED, this, job);
		}
	}
}
