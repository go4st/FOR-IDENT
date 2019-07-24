package de.hswt.fi.ui.vaadin.handler.security;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Notification.Type;
import de.hswt.fi.ui.vaadin.CustomNotification;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DummyPayload;
import de.hswt.fi.ui.vaadin.eventbus.payloads.LoginPayload;
import de.hswt.fi.ui.vaadin.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.spring.events.EventBus.UIEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.security.VaadinSecurity;

@SpringComponent
@UIScope
@Transactional(readOnly = true)
public class LoginHandler extends AbstractHandler<UIEventBus> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginHandler.class);

	private final VaadinSecurity vaadinSecurity;

	private final I18N i18n;

	private final UIEventBus uiEventBus;

	@Autowired
	public LoginHandler(VaadinSecurity vaadinSecurity, I18N i18n, UIEventBus uiEventBus) {
		this.vaadinSecurity = vaadinSecurity;
		this.i18n = i18n;
		this.uiEventBus = uiEventBus;
	}

	@SuppressWarnings("unused")
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_LOGIN)
	@EventBusListenerMethod
	private void executeLogin(LoginPayload loginPayload) {

		LOGGER.debug("entering event bus listener executeLogin in topic {}", loginPayload,
				EventBusTopics.TARGET_HANDLER_LOGIN);

		try {
			vaadinSecurity.login(loginPayload.getUsername().toLowerCase(), loginPayload.getPassword());
		} catch (AuthenticationException e) {
			LOGGER.error("An error uccured", e);
			showErrorNotification();
			LOGGER.debug("publish event inside executeLogin with topic {}",
					EventBusTopics.TARGET_HANDLER_LOGIN);
			uiEventBus.publish(EventBusTopics.SOURCE_HANDLER_LOGIN_FAILED, this, DummyPayload.INSTANCE);
		} catch (Exception e) {
			LOGGER.error("An error occured", e);
		}

	}

	private void showErrorNotification() {
		new CustomNotification.Builder(i18n.get(UIMessageKeys.LOGIN_UI_LOGIN_ERROR_NOTIFICATION_CAPTION),
				i18n.get(UIMessageKeys.LOGIN_UI_LOGIN_ERROR_NOTIFICATION_DESCRIPTION), Type.ERROR_MESSAGE)
				.position(Position.MIDDLE_CENTER).build().show(Page.getCurrent());
	}
}
