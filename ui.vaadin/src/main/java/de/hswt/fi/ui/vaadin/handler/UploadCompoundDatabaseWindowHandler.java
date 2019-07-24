package de.hswt.fi.ui.vaadin.handler;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.CompoundDatabasePayload;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DummyPayload;
import de.hswt.fi.ui.vaadin.windows.AbstractWindow;
import de.hswt.fi.ui.vaadin.windows.UploadCompoundDatabaseWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

@SpringComponent
@ViewScope
public class UploadCompoundDatabaseWindowHandler extends AbstractWindowHandler<ViewEventBus> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UploadCompoundDatabaseWindowHandler.class);

	private final UploadCompoundDatabaseWindow uploadCompoundDatabaseWindow;

	@Autowired
	public UploadCompoundDatabaseWindowHandler(UploadCompoundDatabaseWindow uploadCompoundDatabaseWindow) {
		this.uploadCompoundDatabaseWindow = uploadCompoundDatabaseWindow;
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_SHOW_UPLOAD_COMPOUND_DATABASE_WINDOW)
	private void handleShowUploadCompoundDatabaseWindow(DummyPayload dummy) {
		LOGGER.debug(
				"entering event bus listener handleShowUploadCompoundDatabaseWindow with dummy payload and topic {}",
				EventBusTopics.TARGET_HANDLER_SHOW_UPLOAD_COMPOUND_DATABASE_WINDOW);
		return;
//		UI.getCurrent().addWindow(uploadCompoundDatabaseWindow);
//		uploadCompoundDatabaseWindow.clear();
	}

	@Override
	public void windowClose(CloseEvent e) {
		if (AbstractWindow.CloseType.OK.equals(uploadCompoundDatabaseWindow.getCloseType())) {

			CompoundDatabasePayload payload = new CompoundDatabasePayload(uploadCompoundDatabaseWindow.getPath(),
					uploadCompoundDatabaseWindow.getDate(), uploadCompoundDatabaseWindow.getCompoundSearchService());

			LOGGER.debug("publish event inside windowClose with file {}", payload);
			eventBus.publish(EventBusTopics.TARGET_HANDLER_CONFIRM_UPLOAD_COMPOUND_DATABASE_WINDOW,this, payload);
		}
	}

	@Override
	protected Window getWindow() {
		return uploadCompoundDatabaseWindow;
	}
}