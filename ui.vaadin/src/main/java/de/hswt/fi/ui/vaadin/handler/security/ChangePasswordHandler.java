package de.hswt.fi.ui.vaadin.handler.security;

import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import de.hswt.fi.security.service.api.SecurityService;
import de.hswt.fi.security.service.model.RegisteredUser;
import de.hswt.fi.ui.vaadin.CustomNotification;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.handler.AbstractWindowHandler;
import de.hswt.fi.ui.vaadin.windows.AbstractWindow;
import de.hswt.fi.ui.vaadin.windows.ChangePasswordWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;

@SpringComponent
@ViewScope
public class ChangePasswordHandler extends AbstractWindowHandler<ViewEventBus> {

	private static final long serialVersionUID = 3033061802993063270L;

	private static final Logger LOG = LoggerFactory.getLogger(ChangePasswordHandler.class);

	@Autowired
	private ChangePasswordWindow changePasswordWindow;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private I18N i18n;

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_USER_PASSWORD)
	private void handleEditUser(RegisteredUser user) {
		LOG.debug("entering event bus listener handleEditUser width payload {} in topic {}", user,
				EventBusTopics.TARGET_HANDLER_USER_PASSWORD);

		if (user == null) {
			return;
		}

		changePasswordWindow.setUser(user);

		UI.getCurrent().addWindow(changePasswordWindow);
	}

	@Override
	public void windowClose(CloseEvent e) {
		if (AbstractWindow.CloseType.OK.equals(changePasswordWindow.getCloseType())) {
			updateUser(changePasswordWindow.getUser());
		}
	}

	private void updateUser(RegisteredUser user) {
		if (user == null) {
			return;
		}

		securityService.hashPassword(user);

		if (securityService.updateUser(user) == null) {
			showErrorNotification();
			return;
		}
		showSuccessNotification();
	}

	private void showSuccessNotification() {
		new CustomNotification.Builder(
				i18n.get(UIMessageKeys.CHANGE_PASSWORD_SUCCESS_NOTIFICATION_CAPTION), "",
				Type.HUMANIZED_MESSAGE).styleName(ValoTheme.NOTIFICATION_SUCCESS).build()
						.show(Page.getCurrent());
	}

	private void showErrorNotification() {
		new CustomNotification.Builder(
				i18n.get(UIMessageKeys.CHANGE_PASSWORD_ERROR_NOTIFICATION_CAPTION),
				i18n.get(UIMessageKeys.CHANGE_PASSWORD_ERROR_NOTIFICATION_DESCRIPTION),
				Type.ERROR_MESSAGE).build().show(Page.getCurrent());
	}

	@Override
	protected Window getWindow() {
		return changePasswordWindow;
	}
}
