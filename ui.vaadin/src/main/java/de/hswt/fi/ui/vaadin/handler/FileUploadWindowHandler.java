package de.hswt.fi.ui.vaadin.handler;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DummyPayload;
import de.hswt.fi.ui.vaadin.eventbus.payloads.ProcessingFilePayload;
import de.hswt.fi.ui.vaadin.windows.AbstractWindow;
import de.hswt.fi.ui.vaadin.windows.FileUploadWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

@SpringComponent
@ViewScope
public class FileUploadWindowHandler extends AbstractWindowHandler<ViewEventBus> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadWindowHandler.class);

	@Autowired
	private FileUploadWindow fileUploadWindow;

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.OPEN_FILE_UPLOAD_WINDOW)
	private void handleScoreFileUpload(DummyPayload dummy) {
		LOGGER.debug(
				"entering event bus listener handleScoreFileUpload with dummy payload and topic {}",
				EventBusTopics.OPEN_FILE_UPLOAD_WINDOW);
		UI.getCurrent().addWindow(fileUploadWindow);
		fileUploadWindow.initialize();
	}

	@Override
	public void windowClose(CloseEvent e) {
		if (AbstractWindow.CloseType.OK.equals(fileUploadWindow.getCloseType())) {

			// Publish files to import
			ProcessingFilePayload payload = new ProcessingFilePayload(
					fileUploadWindow.getUploadedFiles(), fileUploadWindow.getRtiCalibrationFile());

			LOGGER.debug("publish event inside windowClose with file {}", payload);
			eventBus.publish(this, payload);
		}
	}

	@Override
	protected Window getWindow() {
		return fileUploadWindow;
	}
}