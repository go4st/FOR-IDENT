package de.hswt.fi.ui.vaadin.handler.security;

import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
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
import de.hswt.fi.ui.vaadin.windows.UserSettingsWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.UIEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;

@SpringComponent
@UIScope
public class EditUserSettingsHandler extends AbstractWindowHandler<UIEventBus> {

	private static final long serialVersionUID = 3033061802993063270L;

	private static final Logger LOG = LoggerFactory.getLogger(EditUserSettingsHandler.class);

	@Autowired
	private UserSettingsWindow userWindow;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private I18N i18n;

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_EDIT_USER_SETTINGS)
	private void handleEditUser(RegisteredUser user) {
		LOG.debug("entering event bus listener handleEditUser width payload {} in topic {}", user,
				EventBusTopics.TARGET_HANDLER_USER_EDIT);

		if (user == null) {
			return;
		}

		userWindow.setWindowCaption(i18n.get(UIMessageKeys.WINDOW_EDIT_USER_CAPTION));
		userWindow.clear();
		userWindow.setUser(user);
		UI.getCurrent().addWindow(userWindow);
	}

	private void updateUser(RegisteredUser user) {

		if (user == null) {
			return;
		}

		// Only hash password if password was changed in the dialog
		if (userWindow.isPasswordChanged()) {
			securityService.hashPassword(user);
		}

		if (securityService.updateUser(user) == null) {
			showErrorNotification();
			return;
		}
		showSuccessNotification();

		// User details changed
		LOG.debug("publish event inside updateUser with topic {}",
				EventBusTopics.SOURCE_HANDLER_USER_CHANGED);
		eventBus.publish(EventBusTopics.SOURCE_HANDLER_USER_CHANGED, this, user);

	}

	private void showSuccessNotification() {
		new CustomNotification.Builder("User details successfully changed", "",
				Type.HUMANIZED_MESSAGE).styleName(ValoTheme.NOTIFICATION_SUCCESS).build()
						.show(Page.getCurrent());
	}

	private void showErrorNotification() {
		new CustomNotification.Builder("Error occured", "User details not changed!",
				Type.ERROR_MESSAGE).build().show(Page.getCurrent());
	}

	@Override
	public void windowClose(CloseEvent e) {
		if (AbstractWindow.CloseType.OK.equals(userWindow.getCloseType())) {
			updateUser(userWindow.getUser());
		}
	}

	@Override
	protected Window getWindow() {
		return userWindow;
	}
}
