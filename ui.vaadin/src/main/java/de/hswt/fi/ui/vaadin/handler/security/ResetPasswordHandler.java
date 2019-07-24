package de.hswt.fi.ui.vaadin.handler.security;

import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import de.hswt.fi.application.properties.ApplicationProperties;
import de.hswt.fi.common.PasswordGenerator;
import de.hswt.fi.mail.service.MailService;
import de.hswt.fi.security.service.api.SecurityService;
import de.hswt.fi.ui.vaadin.CustomNotification;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DummyPayload;
import de.hswt.fi.ui.vaadin.handler.AbstractWindowHandler;
import de.hswt.fi.ui.vaadin.windows.AbstractWindow;
import de.hswt.fi.ui.vaadin.windows.ResetPasswordWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.UIEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;

@SpringComponent
@UIScope
public class ResetPasswordHandler extends AbstractWindowHandler<UIEventBus> {

	private static final long serialVersionUID = 4116203050965517527L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ResetPasswordHandler.class);

	private final I18N i18n;

	private final ResetPasswordWindow resetPasswordWindow;

	private final SecurityService securityService;

	private final MailService mailService;

	private final ApplicationProperties properties;

	private final PasswordGenerator passwordGenerator;

	@Autowired
	public ResetPasswordHandler(I18N i18n, ResetPasswordWindow resetPasswordWindow, SecurityService securityService,
								MailService mailService, ApplicationProperties properties, PasswordGenerator passwordGenerator) {
		this.i18n = i18n;
		this.resetPasswordWindow = resetPasswordWindow;
		this.securityService = securityService;
		this.mailService = mailService;
		this.properties = properties;
		this.passwordGenerator = passwordGenerator;
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_RESET_PASSWORD)
	private void handleResetPassword(DummyPayload payload) {
		resetPasswordWindow.clear();
		UI.getCurrent().addWindow(resetPasswordWindow);
		resetPasswordWindow.focus();
	}

	@Override
	public void windowClose(CloseEvent e) {
		if (AbstractWindow.CloseType.OK.equals(resetPasswordWindow.getCloseType())) {
			resetPassWord(resetPasswordWindow.getEmail());
		}

	}

	private void resetPassWord(String email) {

		if (securityService.mailExists(email)) {
			String newPassword = passwordGenerator.generatePassword(properties.getSecurity().getPasswordLength());
			boolean success = securityService.resetPasswordTo(email, newPassword);
			if (success) {
				mailService.sendMail(email,
						i18n.get(UIMessageKeys.RESET_PASSWORD_HANDLER_MAIL_HEADER),
						i18n.get(UIMessageKeys.RESET_PASSWORD_HANDLER_MAIL_MESSAGE,
								newPassword));

				LOGGER.info("Password reset for user with mail {} performed successfully", email);
			} else {
				LOGGER.info(
						"Password reset for user with mail {} requested, but could be not performed",
						email);
			}
		} else {
			LOGGER.info("Password reset requested with unknown Email {}", email);
		}

		showMessage();

	}

	private void showMessage() {
		new CustomNotification.Builder(
				i18n.get(UIMessageKeys.RESET_PASSWORD_HANDLER_SUCCESS_NOTIFICATION_HEADER),
				i18n.get(UIMessageKeys.RESET_PASSWORD_HANDLER_SUCCESS_NOTIFICATION_TEXT),
				Type.HUMANIZED_MESSAGE).styleName(ValoTheme.NOTIFICATION_SUCCESS).build()
						.show(Page.getCurrent());
	}

	@Override
	protected Window getWindow() {
		return resetPasswordWindow;
	}

}
