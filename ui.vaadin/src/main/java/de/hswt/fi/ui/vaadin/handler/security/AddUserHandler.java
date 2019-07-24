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
import de.hswt.fi.ui.vaadin.windows.AdminUserWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;

@SpringComponent
@ViewScope
public class AddUserHandler extends AbstractWindowHandler<ViewEventBus> {

	private static final long serialVersionUID = 3033061802993063270L;

	private static final Logger LOG = LoggerFactory.getLogger(AddUserHandler.class);

	private final AdminUserWindow adminUserWindow;

	private final SecurityService securityService;

	private final I18N i18n;

	@Autowired
	public AddUserHandler(AdminUserWindow adminUserWindow, SecurityService securityService, I18N i18n) {
		this.adminUserWindow = adminUserWindow;
		this.securityService = securityService;
		this.i18n = i18n;
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_USER_ADD)
	private void handleCreateUser(RegisteredUser user) {
		LOG.debug("entering event bus listener handleCreateUser width payload {} in topic {}", user,
				EventBusTopics.TARGET_HANDLER_USER_ADD);

		if (user.getId() != null) {
			return;
		}

		adminUserWindow.setUser(user);
		adminUserWindow.setCaption(i18n.get(UIMessageKeys.WINDOW_ADMIN_ADD_USER_CAPTION));
		adminUserWindow.setCheckBoxComponentVisible(false);
		adminUserWindow.clear();

		UI.getCurrent().addWindow(adminUserWindow);
	}

	private void createUser(RegisteredUser user) {
		if (user == null) {
			return;
		}

		if (securityService.createUser(user) == null) {

			new CustomNotification.Builder("Error occured", "User not added!", Type.ERROR_MESSAGE)
					.build().show(Page.getCurrent());
		} else {
			new CustomNotification.Builder("User successfully added", "", Type.HUMANIZED_MESSAGE)
					.styleName(ValoTheme.NOTIFICATION_SUCCESS).build().show(Page.getCurrent());

			// User added
			LOG.debug("publish event inside createUser with topic {}",
					EventBusTopics.SOURCE_HANDLER_USER_ADDED);
			eventBus.publish(EventBusTopics.SOURCE_HANDLER_USER_ADDED, this, user);
		}
	}

	@Override
	public void windowClose(CloseEvent e) {
		if (AbstractWindow.CloseType.OK.equals(adminUserWindow.getCloseType())) {
			createUser(adminUserWindow.getUser());
		}
	}

	@Override
	protected Window getWindow() {
		return adminUserWindow;
	}
}
