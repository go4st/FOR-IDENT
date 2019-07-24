package de.hswt.fi.ui.vaadin.handler;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import de.hswt.fi.database.importer.tp.api.TransformationProductImportService;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DummyPayload;
import de.hswt.fi.ui.vaadin.windows.AbstractWindow;
import de.hswt.fi.ui.vaadin.windows.RefreshTPDatabaseWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

@SpringComponent
@ViewScope
public class RefreshTPDatabaseWindowHandler extends AbstractWindowHandler<ViewEventBus> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTPDatabaseWindowHandler.class);

	private final TransformationProductImportService tpImportService;

	private final RefreshTPDatabaseWindow refreshTPDatabaseWindow;

	@Autowired
	public RefreshTPDatabaseWindowHandler(TransformationProductImportService tpImportService,
										  RefreshTPDatabaseWindow refreshTPDatabaseWindow) {
		this.tpImportService = tpImportService;
		this.refreshTPDatabaseWindow = refreshTPDatabaseWindow;
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_SHOW_REFRESH_TP_DATABASE_WINDOW)
	private void handleShowRefreshTPDatabaseWindow(DummyPayload dummy) {
		LOGGER.debug(
				"entering event bus listener handleShowRefreshTPDatabaseWindow with dummy payload and topic {}",
				EventBusTopics.TARGET_HANDLER_SHOW_REFRESH_TP_DATABASE_WINDOW);
		UI.getCurrent().addWindow(refreshTPDatabaseWindow);
	}

	@Override
	public void windowClose(CloseEvent e) {
		if (AbstractWindow.CloseType.OK.equals(refreshTPDatabaseWindow.getCloseType())) {
			tpImportService.createData();
		}
	}

	@Override
	protected Window getWindow() {
		return refreshTPDatabaseWindow;
	}
}