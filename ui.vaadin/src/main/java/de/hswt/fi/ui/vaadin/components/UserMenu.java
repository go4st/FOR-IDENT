package de.hswt.fi.ui.vaadin.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.hswt.fi.security.service.api.SecurityService;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DummyPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.SessionEventBus;
import org.vaadin.spring.events.EventBus.UIEventBus;
import org.vaadin.spring.i18n.I18N;

import javax.annotation.PostConstruct;
import java.util.Locale;
import java.util.Objects;

@SpringComponent
@UIScope
public class UserMenu extends CssLayout {

	private static final long serialVersionUID = -7573548309005558127L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserMenu.class);

	private final SecurityService securityService;

	private final I18N i18n;

	private final SessionEventBus sessionEventBus;

	private final UIEventBus uiEventBus;

	private CssLayout menuItemsLayout;

	private CssLayout menuItemsCollapsedLayout;

	private Locale locale;

	@Autowired
	public UserMenu(SecurityService securityService, I18N i18n, SessionEventBus sessionEventBus, UIEventBus uiEventBus) {
		this.securityService = securityService;
		this.i18n = i18n;
		this.sessionEventBus = sessionEventBus;
		this.uiEventBus = uiEventBus;
	}

	@PostConstruct
	public void postConstruct() {

		locale = VaadinSession.getCurrent().getLocale();
		LOGGER.debug("Locale from VaadinSession: {}", locale);

		setPrimaryStyleName("valo-menu");

		initMenu();
		initMenuItems();

	}

	private void initMenu() {
		menuItemsLayout = new CssLayout();
		menuItemsLayout.setPrimaryStyleName("valo-menuitems");

		menuItemsCollapsedLayout = new CssLayout();
		menuItemsCollapsedLayout.setPrimaryStyleName("valo-menuitems");
		menuItemsCollapsedLayout.setVisible(false);

		CssLayout menu = new CssLayout();
		menu.setWidth("100%");
		menu.addComponent(menuItemsLayout);
		menu.addComponent(menuItemsCollapsedLayout);
		menu.addStyleName("valo-menu-part");
		addComponent(menu);
	}

	private void initMenuItems() {
		// currentUser == null equals not authenticaed
		if (securityService.getCurrentUser() == null) {
			addAnonymousButtons();
			return;
		}

		Button button = buildItemButton(
				i18n.get(UIMessageKeys.USER_MENU_SETTINGS_CAPTION, locale),
				VaadinIcons.USER);
		button.addClickListener(e -> handleUserSettingsClicked());
		menuItemsLayout.addComponent(button);
		button = buildItemButton(
				i18n.get(UIMessageKeys.USER_MENU_SETTINGS_CAPTION, locale),
				VaadinIcons.USER, true);
		button.addClickListener(e -> handleUserSettingsClicked());
		menuItemsCollapsedLayout.addComponent(button);

		button = buildItemButton(i18n.get(UIMessageKeys.USER_MENU_LOGOUT_CAPTION, locale),
				VaadinIcons.SIGN_OUT);
		button.addClickListener(e -> handleLogoutButtonClicked());
		menuItemsLayout.addComponent(button);
		button = buildItemButton(i18n.get(UIMessageKeys.USER_MENU_LOGOUT_CAPTION, locale),
				VaadinIcons.SIGN_OUT, true);
		button.addClickListener(e -> handleLogoutButtonClicked());
		menuItemsCollapsedLayout.addComponent(button);
	}

	private void addAnonymousButtons() {
		Button button = buildItemButton(
				i18n.get(UIMessageKeys.USER_MENU_REGISTER_CAPTION, locale),
				VaadinIcons.EDIT);
		menuItemsLayout.addComponent(button);
		button.addClickListener(e -> handleRegisterButtonClicked());
		button = buildItemButton(
				i18n.get(UIMessageKeys.USER_MENU_REGISTER_CAPTION, locale),
				VaadinIcons.EDIT, true);
		button.addClickListener(e -> handleRegisterButtonClicked());
		menuItemsCollapsedLayout.addComponent(button);

		button = buildItemButton(i18n.get(UIMessageKeys.USER_MENU_LOGIN_CAPTION, locale),
				VaadinIcons.SIGN_IN);
		button.addClickListener(e -> handleLoginButtonClick());
		menuItemsLayout.addComponent(button);
		button = buildItemButton(i18n.get(UIMessageKeys.USER_MENU_LOGIN_CAPTION, locale),
				VaadinIcons.SIGN_IN, true);
		button.addClickListener(e -> handleLoginButtonClick());
		menuItemsCollapsedLayout.addComponent(button);
	}

	private void handleRegisterButtonClicked() {
		LOGGER.debug("entering method handleRegisterButtonClicked");
		uiEventBus.publish(EventBusTopics.TARGET_HANDLER_REQUEST_USER_ACCOUNT, this,
				DummyPayload.INSTANCE);
	}

	private void handleLoginButtonClick() {
		Page.getCurrent().setLocation("/login");
	}

	private void handleUserSettingsClicked() {
		LOGGER.debug("entering method handleUserSettingsClicked");

		if (securityService.getCurrentUser() == null) {
			LOGGER.debug("current user is null - returning");
			return;
		}
		uiEventBus.publish(EventBusTopics.TARGET_HANDLER_EDIT_USER_SETTINGS, this,
				securityService.getCurrentUser());
	}

	private void handleLogoutButtonClicked() {
		sessionEventBus.publish(EventBusTopics.TARGET_HANDLER_LOGOUT, this, DummyPayload.INSTANCE);
	}

	private Button buildItemButton(String caption, Resource icon) {
		return buildItemButton(caption, icon, false);
	}

	private Button buildItemButton(String caption, Resource icon, boolean smallButton) {
		final Button button = new Button(caption, icon);
		button.setDescription(button.getCaption());
		button.setCaptionAsHtml(true);
		button.setPrimaryStyleName("valo-menu-item");

		if (smallButton) {
			button.setCaption(null);
			button.addStyleName(ValoTheme.BUTTON_HUGE);
			button.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		}

		return button;
	}

	void setCollapsed(boolean collapsed) {
		if (collapsed && menuItemsLayout.isVisible()) {
			menuItemsLayout.setVisible(false);
			menuItemsCollapsedLayout.setVisible(true);
		} else if (!collapsed && menuItemsCollapsedLayout.isVisible()) {
			menuItemsLayout.setVisible(true);
			menuItemsCollapsedLayout.setVisible(false);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		UserMenu that = (UserMenu) o;
		return Objects.equals(securityService, that.securityService) &&
				Objects.equals(i18n, that.i18n) &&
				Objects.equals(sessionEventBus, that.sessionEventBus) &&
				Objects.equals(uiEventBus, that.uiEventBus) &&
				Objects.equals(menuItemsLayout, that.menuItemsLayout) &&
				Objects.equals(menuItemsCollapsedLayout, that.menuItemsCollapsedLayout) &&
				Objects.equals(locale, that.locale);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), securityService, i18n, sessionEventBus, uiEventBus, menuItemsLayout,
				menuItemsCollapsedLayout, locale);
	}
}
