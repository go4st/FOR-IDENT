package de.hswt.fi.ui.vaadin.handler.security;

import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import de.hswt.fi.application.properties.ApplicationProperties;
import de.hswt.fi.mail.service.MailService;
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
public class EditUserHandler extends AbstractWindowHandler<ViewEventBus> {

	private static final long serialVersionUID = 3033061802993063270L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EditUserHandler.class);

	@Autowired
	private AdminUserWindow adminUserWindow;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private MailService mailService;

	@Autowired
	private I18N i18n;

	@Autowired
	private ApplicationProperties applicationProperties;

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_USER_EDIT)
	private void handleEditUser(RegisteredUser user) {
		LOGGER.debug("entering event bus listener handleEditUser width payload {} in topic {}",
				user, EventBusTopics.TARGET_HANDLER_USER_EDIT);

		if (user == null) {
			return;
		}

		adminUserWindow.setUser(user);
		adminUserWindow.setCaption(i18n.get(UIMessageKeys.WINDOW_ADMIN_EDIT_USER_CAPTION));
		adminUserWindow.setPasswordComponentVisible(false);
		adminUserWindow.clear();

		UI.getCurrent().addWindow(adminUserWindow);
	}

	@Override
	public void windowClose(CloseEvent e) {
		if (AbstractWindow.CloseType.OK.equals(adminUserWindow.getCloseType())) {
			updateUser(adminUserWindow.getUser());
		}
	}

	private void updateUser(RegisteredUser user) {
		if (user == null) {
			return;
		}

		// Error occured
		if (securityService.updateUser(user) == null) {
			showErrorNotification();
			return;
		} else {

			showSuccessNotification();
			if (adminUserWindow.sendMail()) {
				sendAccountActivatedMailToUser(user.getMail(), user.getUsername());
			}
			LOGGER.debug("publish event inside updateUser with topic {}",
					EventBusTopics.SOURCE_HANDLER_USER_CHANGED);
			eventBus.publish(EventBusTopics.SOURCE_HANDLER_USER_CHANGED, this, user);
		}

	}

	private void sendAccountActivatedMailToUser(String mail, String username) {
		mailService.sendMail(mail, getActivationMailSubject(), getActivationMailText());
		notifyAdmins(username);
	}

	private void notifyAdmins(String username) {
		applicationProperties.getContactAdresses().forEach(
				c -> mailService.sendMail(c, getAdminMailSubject(), getAdminMailText(username)));

	}

	private String getAdminMailText(String username) {
		return "This email is to inform you, that a member of the "
				+ applicationProperties.getUi().getHeader().getCaption() + " admin group"
				+ " has activated the user with username: " + username;

	}

	private String getAdminMailSubject() {
		return applicationProperties.getUi().getHeader().getCaption() + " [ACTIVATION]"
				+ ": A new user account has been created";
	}

	private String getActivationMailText() {

		String headerCaption = applicationProperties.getUi().getHeader().getCaption();
		return i18n.get(UIMessageKeys.ADMIN_EDIT_USER_HANDLER_ACTIVATION_MAIL_TEXT, headerCaption,
				headerCaption);
	}

	private String getActivationMailSubject() {
		return applicationProperties.getUi().getHeader().getCaption() + " Account activation";
	}

	private void showSuccessNotification() {
		new CustomNotification.Builder(
				i18n.get(UIMessageKeys.ADMIN_EDIT_USER_HANDLER_SUCCESS_NOTIFCATION), "",
				Type.HUMANIZED_MESSAGE).styleName(ValoTheme.NOTIFICATION_SUCCESS).build()
						.show(Page.getCurrent());
	}

	private void showErrorNotification() {
		new CustomNotification.Builder(i18n.get(UIMessageKeys.ERROR_NOTIFICATION_CAPTION),
				i18n.get(UIMessageKeys.ADMIN_EDIT_USER_HANDLER_ERROR_NOTIFICATION),
				Type.ERROR_MESSAGE).build().show(Page.getCurrent());
	}

	@Override
	protected Window getWindow() {
		return adminUserWindow;
	}
}
