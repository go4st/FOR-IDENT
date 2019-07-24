package de.hswt.fi.ui.vaadin.uis;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.hswt.fi.common.spring.SpringProfileUtil;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DummyPayload;
import de.hswt.fi.ui.vaadin.eventbus.payloads.LoginPayload;
import de.hswt.fi.ui.vaadin.handler.UIScopeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.UIEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Locale;

public abstract class AbstractLoginUI extends UI {

	private static final String TEXT_FIELD_WIDTH = "10rem";

	private static final String USER_LOGO_HEIGHT = "8.5rem";

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultLoginUI.class);

	private final SpringProfileUtil springProfileUtil;

	private final I18N i18n;

	private final UIEventBus uiEventBus;

	// Do not remove. In order to be created by Spring, this bean needs to wired
    // here. This is same for all handler classes
	@SuppressWarnings("unused")
	private final UIScopeHandler uiscopeHandler;

	protected abstract void buildLogo();

    protected CssLayout cssLayout;

	private TextField userNameTextField;

	private PasswordField passwordField;

	@Autowired
    public AbstractLoginUI(SpringProfileUtil springProfileUtil, I18N i18n, UIEventBus uiEventBus,
                           UIScopeHandler uiscopeHandler) {
		this.springProfileUtil = springProfileUtil;
		this.i18n = i18n;
		this.uiEventBus = uiEventBus;
		this.uiscopeHandler = uiscopeHandler;
	}

	@PostConstruct
	private void postConstruct() {
		uiEventBus.subscribe(this);
	}

	@PreDestroy
	private void preDestroy() {
		uiEventBus.unsubscribe(this);
	}

	@Override
	protected void init(VaadinRequest request) {

		String activeSpringProfiles = String.join(", ", springProfileUtil.getActiveProfiles());
		LOGGER.debug("Active Spring profiles: {}", activeSpringProfiles);

		initLocale(request);

		setContent(buildLayout());
		userNameTextField.focus();

	}

	private void initLocale(VaadinRequest request) {

		Locale locale = request.getLocale();
		if (locale.equals(Locale.GERMANY) || locale.equals(Locale.GERMAN)) {
			this.setLocale(locale);
			this.getSession().setLocale(locale);
			return;
		}

		this.setLocale(Locale.US);
		this.getSession().setLocale(Locale.US);

	}

	private CssLayout buildLayout() {
		cssLayout = new CssLayout();
		cssLayout.setSizeFull();
		cssLayout.addStyleName("login-panel");

		buildProfile();
		buildLogo();
		buildLoginForm();

		return cssLayout;
	}

	private void buildProfile() {
		String text;
		if (springProfileUtil.isDevelopmentProfile()) {
			text = "<span style='color: #00004D;'>DEVELOPMENT</span>";
		} else {
			return;
		}

		Label profileLabel = new Label(text, ContentMode.HTML);
		profileLabel.setWidthUndefined();
		profileLabel.addStyleName(CustomValoTheme.ABSOLUTE);
		profileLabel.addStyleName(CustomValoTheme.PADDING);
		profileLabel.addStyleName(CustomValoTheme.MARGIN);
		profileLabel.addStyleName(CustomValoTheme.LABEL_HUGE);
		profileLabel.addStyleName(CustomValoTheme.LABEL_VERY_BOLD);
		cssLayout.addComponent(profileLabel);
	}

	private void buildLoginForm() {
		CssLayout tableLayout = new CssLayout();
		tableLayout.setSizeFull();
		tableLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_TABLE_STYLE);
		tableLayout.addStyleName(CustomValoTheme.BACKGROUND_COLOR_ALT1);
		cssLayout.addComponent(tableLayout);

		CssLayout loginLayout = new CssLayout();
		loginLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_TABLE_CELL_MIDDLE);
		loginLayout.setHeight("100%");
		tableLayout.addComponent(loginLayout);

		CssLayout browserLayout = new CssLayout();
		browserLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_TABLE_ROW);
		tableLayout.addComponent(browserLayout);

		Label browserLabel = new Label("Supporting Internet Explorer 11, Firefox, Chrome");
		browserLabel.addStyleName(CustomValoTheme.LABEL_LARGE);
		browserLabel.addStyleName(CustomValoTheme.PADDING_HALF_BOTTOM);
		browserLabel.addStyleName(CustomValoTheme.PADDING_HALF_HORIZONTAL);
		browserLabel.addStyleName(CustomValoTheme.FLOAT_RIGHT);
		browserLayout.addComponent(browserLabel);

		CssLayout columnLayout = new CssLayout();
		loginLayout.addComponent(columnLayout);

		CssLayout upperLayout = new CssLayout();
		upperLayout.addStyleName("login-section");
		upperLayout.addStyleName(CustomValoTheme.PADDING_BOTTOM);
		columnLayout.addComponent(upperLayout);

		Resource resource = new ThemeResource("img/user.svg");
		Embedded embedded = new Embedded(null, resource);
		embedded.setHeight("100%");
		embedded.setHeight(USER_LOGO_HEIGHT);
		embedded.addStyleName(CustomValoTheme.MARGIN_HALF_RIGHT);
		upperLayout.addComponent(embedded);

		CssLayout fieldsLayout = new CssLayout();
		fieldsLayout.addStyleName(CustomValoTheme.PADDING_LEFT);
		upperLayout.addComponent(fieldsLayout);

		userNameTextField = new TextField();
		userNameTextField.setCaption(i18n.get(UIMessageKeys.LOGIN_UI_USER_NAME_TEXTFIELD_CAPTION));
		userNameTextField.addStyleName(CustomValoTheme.MARGIN_HALF_LEFT);
		userNameTextField.setWidth(TEXT_FIELD_WIDTH);
		userNameTextField.focus();
		fieldsLayout.addComponent(userNameTextField);

		passwordField = new PasswordField();
		passwordField.setCaption(i18n.get(UIMessageKeys.LOGIN_UI_PASSWORD_FIELD_CAPTION));
		passwordField.addStyleName(CustomValoTheme.MARGIN_HALF_LEFT);
		passwordField.addStyleName(CustomValoTheme.MARGIN_HALF_BOTTOM);
		passwordField.addStyleName("custom-margin-top");
		passwordField.setWidth(TEXT_FIELD_WIDTH);
		fieldsLayout.addComponent(passwordField);

		CssLayout buttonWrapLayout = new CssLayout();
		buttonWrapLayout.addStyleName(CustomValoTheme.BLOCK);
		fieldsLayout.addComponent(buttonWrapLayout);

		Button loginButton = new Button();
		loginButton.setCaption(i18n.get(UIMessageKeys.LOGIN_UI_LOGIN_BUTTON_CAPTION));
		loginButton.addStyleName(CustomValoTheme.MARGIN_HALF_TOP);
		loginButton.addStyleName(CustomValoTheme.FLOAT_RIGHT);
		loginButton.addStyleName(CustomValoTheme.BACKGROUND_COLOR_ALT3);

		loginButton.addClickListener(event -> handleLoginButtonClicked());
		loginButton.setClickShortcut(KeyCode.ENTER);

		buttonWrapLayout.addComponent(loginButton);

		CssLayout lowerLayout = new CssLayout();
		lowerLayout.addStyleName(CustomValoTheme.BLOCK);
		lowerLayout.addStyleName(CustomValoTheme.MARGIN_HALF_TOP);
		columnLayout.addComponent(lowerLayout);

		Button registerButton = createLinkButton(
				i18n.get(UIMessageKeys.LOGIN_UI_REGISTER_LINK_CAPTION));
		lowerLayout.addComponent(registerButton);
		registerButton.addClickListener(c -> handleRegisterButtonClick());

		Button resetPassWordButton = createLinkButton(
				i18n.get(UIMessageKeys.LOGIN_UI_PASSWORD_LINK_CAPTION));
		resetPassWordButton.addClickListener(c -> handleResetPassWordButtonClick());
		lowerLayout.addComponent(resetPassWordButton);

	}

	private void handleRegisterButtonClick() {
		LOGGER.debug("entering event bus listener handleRegisterButtonClick");
		uiEventBus.publish(EventBusTopics.TARGET_HANDLER_REQUEST_USER_ACCOUNT, this,
				DummyPayload.INSTANCE);
	}

	private void handleResetPassWordButtonClick() {
		LOGGER.debug("entering event bus listener handleResetPassWordButtonClick");
		uiEventBus.publish(EventBusTopics.TARGET_HANDLER_RESET_PASSWORD, this, DummyPayload.INSTANCE);
	}

	private Button createLinkButton(String caption) {
		Button linkButton = new Button(caption);
		linkButton.addStyleName(ValoTheme.BUTTON_LINK);
		linkButton.addStyleName(CustomValoTheme.COLOR_ALT3);
		linkButton.addStyleName(CustomValoTheme.FLOAT_RIGHT);
		linkButton.addStyleName(CustomValoTheme.BLOCK);
		linkButton.addStyleName(CustomValoTheme.PADDING_NONE);

		return linkButton;
	}

	private void handleLoginButtonClicked() {
        LoginPayload loginPayload = getLoginPayload();
		LOGGER.debug("publish event inside handleLoginButtonClicked with topic {}",
				EventBusTopics.TARGET_HANDLER_LOGIN);
		uiEventBus.publish(EventBusTopics.TARGET_HANDLER_LOGIN, this, loginPayload);
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.SOURCE_HANDLER_LOGIN_FAILED)
	private void handleLoginFailed(DummyPayload payload) {
		LOGGER.debug("entering event bus listener handleLoginFailed in topic {}",
				EventBusTopics.SOURCE_HANDLER_LOGIN_FAILED);
		userNameTextField.clear();
		passwordField.clear();
		userNameTextField.focus();
	}

	private LoginPayload getLoginPayload() {
		LoginPayload loginPayload = new LoginPayload();
		loginPayload.setUsername(userNameTextField.getValue());
		loginPayload.setPassword(passwordField.getValue());
		return loginPayload;
	}
}
