package de.hswt.fi.ui.vaadin.handler.security;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import de.hswt.fi.application.properties.ApplicationProperties;
import de.hswt.fi.mail.service.MailService;
import de.hswt.fi.mail.service.model.MailTemplate;
import de.hswt.fi.mail.service.model.RequestUserAccountNotifyAdminTemplate;
import de.hswt.fi.security.service.api.RegistrationService;
import de.hswt.fi.security.service.model.RegisteredUser;
import de.hswt.fi.ui.vaadin.CustomNotification;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DummyPayload;
import de.hswt.fi.ui.vaadin.handler.AbstractWindowHandler;
import de.hswt.fi.ui.vaadin.windows.AbstractWindow;
import de.hswt.fi.ui.vaadin.windows.RegisterUserWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.UIEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;

import java.util.HashMap;
import java.util.Map;

@SpringComponent
@UIScope
public class RequestUserAccountHandler extends AbstractWindowHandler<UIEventBus> {

	private static final long serialVersionUID = 1L;

	private final RegisterUserWindow registerUserWindow;

	private final RegistrationService registrationService;

	private final ApplicationProperties applicationProperties;

	private final MailService mailService;

	private final I18N i18n;

	@Autowired
	public RequestUserAccountHandler(RegisterUserWindow registerUserWindow, RegistrationService registrationService, ApplicationProperties applicationProperties, MailService mailService, I18N i18n) {
		this.registerUserWindow = registerUserWindow;
		this.registrationService = registrationService;
		this.applicationProperties = applicationProperties;
		this.mailService = mailService;
		this.i18n = i18n;
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_REQUEST_USER_ACCOUNT)
	private void handleResetPasword(DummyPayload payload) {
		registerUserWindow.clear();
		UI.getCurrent().addWindow(registerUserWindow);
	}

	@Override
	public void windowClose(CloseEvent e) {
		if (AbstractWindow.CloseType.OK.equals(registerUserWindow.getCloseType())) {
			try {
				registerUser(registerUserWindow.getUser());
			} catch (IllegalArgumentException error) {
				showErrorNotification();
			}
		}
	}

	private void registerUser(RegisteredUser user) {
		if (registrationService.registerUser(user)) {
			notifyAdmins(user);
		}
	}

	private void notifyAdmins(RegisteredUser user) {
		applicationProperties.getContactAdresses()
				.forEach(c -> sendAdminNotificationMail(c, user.getUsername()));
		sendUserNotificationMail(user.getMail());
		showSuccessNotification();
	}

	private void sendAdminNotificationMail(String to, String userName) {
		RequestUserAccountNotifyAdminTemplate template = new RequestUserAccountNotifyAdminTemplate(
				userName, applicationProperties.getUi().getHeader().getCaption());

		Map<String, Object> model = new HashMap<>();
		model.put("accountRequest", template);

		mailService.sendTemplateMail(to, getAdminNotificationMailSubject(),
				MailTemplate.USER_ACCOUNT_REQUEST_NOTIFY_ADMIN.getLocation(), model);
	}

	private String getAdminNotificationMailSubject() {
		return applicationProperties.getUi().getHeader().getCaption() + " [REQUEST]"
				+ " : New User Account Request";
	}

	private String getUserNotificationMailSubject() {
		return applicationProperties.getUi().getHeader().getCaption() + " Account request";
	}

	private void sendUserNotificationMail(String to) {
		Map<String, Object> model = new HashMap<>();
		model.put("caption", applicationProperties.getUi().getHeader().getCaption());
		mailService.sendTemplateMail(to, getUserNotificationMailSubject(),
				MailTemplate.USER_ACCOUNT_REQUEST_NOTIFY_USER.getLocation(), model);
	}

	private void showSuccessNotification() {
		new CustomNotification.Builder(
				i18n.get(UIMessageKeys.REQUEST_USER_ACCOUNT_SUCCESS_NOTIFICATION_HEADER),
				i18n.get(UIMessageKeys.REQUEST_USER_ACCOUNT_SUCCESS_NOTIFICATION_TEXT),
				Type.HUMANIZED_MESSAGE).delay(3000).htmlAllowd(false)
						.position(Position.MIDDLE_CENTER).styleName(ValoTheme.NOTIFICATION_SUCCESS)
						.build().show(Page.getCurrent());
	}

	private void showErrorNotification() {
		new CustomNotification.Builder(i18n.get(UIMessageKeys.ERROR_NOTIFICATION_CAPTION),
				i18n.get(UIMessageKeys.REQUEST_USER_ACCOUNT_ERROR_NOTIFICATION_TEXT),
				Type.ERROR_MESSAGE).delay(Notification.DELAY_FOREVER).htmlAllowd(false)
						.position(Position.MIDDLE_CENTER).build().show(Page.getCurrent());
	}

	@Override
	protected Window getWindow() {
		return registerUserWindow;
	}

}
