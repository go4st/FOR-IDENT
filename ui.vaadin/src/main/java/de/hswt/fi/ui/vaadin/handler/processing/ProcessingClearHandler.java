package de.hswt.fi.ui.vaadin.handler.processing;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.ui.vaadin.UIConstants;
import de.hswt.fi.ui.vaadin.configuration.SessionSharedObjects;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DummyPayload;
import de.hswt.fi.ui.vaadin.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@SpringComponent
@ViewScope
public class ProcessingClearHandler extends AbstractHandler<ViewEventBus> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingClearHandler.class);

	private final SessionSharedObjects sessionObjects;

	private final Path searchTempDirectory;

	@Autowired
	public ProcessingClearHandler(SessionSharedObjects sessionObjects,
								  @Qualifier(UIConstants.TEMP_DIRECTORY_RTI) Path searchTempDirectory) {
		this.sessionObjects = sessionObjects;
		this.searchTempDirectory = searchTempDirectory;
	}

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_DELETE_FILES)
	private void handleClearFiles(DummyPayload payload) {
		LOGGER.debug("entering event bus listener handleClearFiles with payload ({}) in topic {}",
				payload, EventBusTopics.TARGET_HANDLER_DELETE_FILES);

		sessionObjects.clearProcessingData();

		try (Stream<Path> files = Files.list(searchTempDirectory)) {
			files.forEach(this::delete);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}

		LOGGER.debug("publish session event handleClearFiles with topic {}",
				EventBusTopics.SOURCE_HANDLER_DELETED_FILES_PROCESSING);
		eventBus.publish(EventScope.SESSION, EventBusTopics.SOURCE_HANDLER_DELETED_FILES_PROCESSING,
				this, DummyPayload.INSTANCE);
	}

	private void delete(Path path) {
		try {
			Files.delete(path);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
	}
}
