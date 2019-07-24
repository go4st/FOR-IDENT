package de.hswt.fi.ui.vaadin.handler.security;

import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import de.hswt.fi.application.properties.ApplicationProperties;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DummyPayload;
import de.hswt.fi.ui.vaadin.handler.AbstractWindowHandler;
import de.hswt.fi.ui.vaadin.windows.AbstractWindow;
import de.hswt.fi.ui.vaadin.windows.ConfirmationDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.SessionEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;

@SpringComponent
@VaadinSessionScope
public class LogoutHandler extends AbstractWindowHandler<SessionEventBus> {

	private static final long serialVersionUID = 3033061802993063270L;

	private static final Logger LOGGER = LoggerFactory.getLogger(LogoutHandler.class);

	@Autowired
	private ConfirmationDialog<Object> confirmationDialog;

	@Autowired
	private I18N i18n;

	@Autowired
	private ApplicationProperties applicationProperties;

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_LOGOUT)
	private void handleLogout(DummyPayload payload) {
		LOGGER.debug("entering event bus listener handleLogout in topic {}",
				EventBusTopics.TARGET_HANDLER_LOGOUT);

		confirmationDialog.initDialog(
				i18n.get(UIMessageKeys.LOGOUT_HANDLER_CONFIRMATION_DIALOG_CAPTION),
				i18n.get(UIMessageKeys.LOGOUT_HANDLER_CONFIRMATION_DIALOG_DESCRIPTION));

		UI.getCurrent().addWindow(confirmationDialog);
	}

	@Override
	public void windowClose(CloseEvent e) {
		if (AbstractWindow.CloseType.OK.equals(confirmationDialog.getCloseType())) {
			Page.getCurrent().setLocation(applicationProperties.getSecurity().getLogoutUrl());
		}
	}

	@Override
	protected Window getWindow() {
		return confirmationDialog;
	}
}
