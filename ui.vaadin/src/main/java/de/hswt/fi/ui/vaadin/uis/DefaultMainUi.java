package de.hswt.fi.ui.vaadin.uis;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import de.hswt.fi.security.service.api.SecurityService;
import de.hswt.fi.security.service.model.Role;
import de.hswt.fi.ui.vaadin.CustomNotification;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.components.SideBar;
import de.hswt.fi.ui.vaadin.configuration.SessionSharedObjects;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.UserPropertyPayload;
import de.hswt.fi.ui.vaadin.handler.SessionScopeHandler;
import de.hswt.fi.ui.vaadin.handler.UIScopeHandler;
import de.hswt.fi.ui.vaadin.views.ErrorView;
import de.hswt.fi.ui.vaadin.views.HomeView;
import de.hswt.fi.ui.vaadin.views.ProcessingView;
import de.hswt.fi.userproperties.service.api.UserPropertiesService;
import de.hswt.fi.userproperties.service.api.UserPropertyKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.i18n.I18N;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SpringUI(path = "/")
@Title("FOR-IDENT")
@Theme("fi-valo")
@Push(transport = Transport.LONG_POLLING)
@Widgetset(value = "de.hswt.fi.ui.vaadin.widgetset")
public class DefaultMainUi extends UI {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMainUi.class);

	@SuppressWarnings("unused")
	private final SessionScopeHandler sessionHandler;

	@SuppressWarnings("unused")
	private final UIScopeHandler uiHandler;

	private final I18N i18n;

	private final SecurityService securityService;

	private final SpringViewProvider viewProvider;

	private final SideBar sideBar;

	private final SessionSharedObjects sessionObjects;

	private final UserPropertiesService userPropertiesService;

	private final EventBus.SessionEventBus sessionEventBus;

	private CssLayout viewLayout;

	@Autowired
	public DefaultMainUi(I18N i18n, SecurityService securityService, SpringViewProvider viewProvider,
						 SideBar sideBar, SessionSharedObjects sessionObjects, SessionScopeHandler sessionHandler,
						 UIScopeHandler uiHandler, UserPropertiesService userPropertiesService, EventBus.SessionEventBus sessionEventBus) {
		this.sessionHandler = sessionHandler;
		this.uiHandler = uiHandler;
		this.i18n = i18n;
		this.securityService = securityService;
		this.viewProvider = viewProvider;
		this.sideBar = sideBar;
		this.sessionObjects = sessionObjects;
		this.userPropertiesService = userPropertiesService;
		this.sessionEventBus = sessionEventBus;

		setLocale(VaadinSession.getCurrent().getLocale());
		LocaleContextHolder.setLocale(getLocale());
        setErrorHandler(new DefaultErrorHandler() {
            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                new CustomNotification.Builder("", i18n.get(UIMessageKeys.DEFAULT_ERROR_MESSAGE), Notification.Type.ERROR_MESSAGE);
            }
        });
	}

	@Override
	protected void init(VaadinRequest request) {

		if (securityService.getCurrentUser() != null && securityService.getCurrentUser().isEnabled()) {
			securityService.getCurrentUser().getAccessibleDatabases().forEach(db -> sessionObjects.enableSearchService(db.getName()));
		}

		CssLayout rootWrapper = new CssLayout();
		rootWrapper.setSizeFull();
		rootWrapper.addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_COLUMN);

		CssLayout rootLayout = new CssLayout();
		rootLayout.setSizeFull();
		rootLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX);
		rootLayout.addComponent(sideBar);

		viewLayout = new CssLayout();
		viewLayout.setSizeFull();
		rootLayout.addComponent(viewLayout);

		initNavigator();

		rootWrapper.addComponent(rootLayout);

		if (!(sessionObjects.isCookiesAccepted() ||
				userPropertiesService.getBooleanValueOrDefault(UserPropertyKeys.ACCEPT_COOKIES, false))) {
			rootWrapper.addComponent(createCookieConsentLayout(rootWrapper));
		}

		setContent(rootWrapper);

		Optional<String> redirectViewName = sessionObjects.getRedirectViewName();
		if (redirectViewName.isPresent()) {
			getNavigator().navigateTo(redirectViewName.get());
			sessionObjects.setRedirectViewName(null);
		}
		
		else if (getNavigator().getState().isEmpty()) {
			getNavigator().navigateTo(HomeView.VIEW_NAME);
		}
	}

	private CssLayout createCookieConsentLayout(CssLayout parent) {
		CssLayout cookieLayout = new CssLayout();
		cookieLayout.addStyleName("cookie-consent");

		Button acceptButton = new Button(i18n.get(UIMessageKeys.COOKIE_CONSENT_BUTTON_CAPTION));
		acceptButton.addClickListener(e -> {
			parent.removeComponent(cookieLayout);
			acceptCookieConsent();
		});
		Label consentMessage = new Label(i18n.get(UIMessageKeys.COOKIE_CONSENT_MESSAGE));
		consentMessage.addStyleName(CustomValoTheme.LABEL_LARGE);
		consentMessage.addStyleName(CustomValoTheme.MARGIN_RIGHT);
		cookieLayout.addComponents(consentMessage, acceptButton);

		cookieLayout.addComponents(consentMessage, acceptButton);

		return cookieLayout;
	}

	private void acceptCookieConsent() {

		sessionObjects.setCookiesAccepted(true);

		LOGGER.debug("publish event inside acceptCookieConsent with topic {}",
				EventBusTopics.TARGET_HANDLER_USER_PROPERTIES_BOOLEAN);
		sessionEventBus.publish(EventBusTopics.TARGET_HANDLER_USER_PROPERTIES_BOOLEAN, this,
				new UserPropertyPayload<>(UserPropertyKeys.ACCEPT_COOKIES, true));
	}

	private void initNavigator() {
		final Navigator navigator = new CustomNavigator(this, new ViewDisplay() {

			private static final long serialVersionUID = 1L;

			@Override
			public void showView(View view) {
				viewLayout.removeAllComponents();
				viewLayout.addComponent((com.vaadin.ui.Component) view);
			}
		});
		navigator.setErrorView(new ErrorView());
		navigator.addProvider(viewProvider);
		setNavigator(navigator);
	}

	private class CustomNavigator extends Navigator {

		private List<Class> restrictedViews = Collections.singletonList(ProcessingView.class);

		CustomNavigator(DefaultMainUi components, ViewDisplay viewDisplay) {
			super(components, viewDisplay);
		}

		@Override
		protected void navigateTo(View view, String viewName, String parameters) {
			// Manually secure processing view (redirect to login)
			// to provide processing button in side bar
			if(!securityService.currentUserHasRole(Role.ROLE_USER.name()) && restrictedViews.stream().anyMatch(view.getClass()::isAssignableFrom)) {
				showAccessDeniedNotification();
				if (getCurrentView() == null) {
					super.navigateTo(HomeView.VIEW_NAME);
				}
				return;
			}
			super.navigateTo(view, viewName, parameters);
		}

		private void showAccessDeniedNotification() {
			new CustomNotification.Builder(i18n.get(UIMessageKeys.USER_NEED_LOG_IN_NOTIFICATION_CAPTION),
                    i18n.get(UIMessageKeys.USER_NEED_LOG_IN_NOTIFICATION_MESSAGE),
                    Notification.Type.HUMANIZED_MESSAGE).build().show(Page.getCurrent());
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		DefaultMainUi that = (DefaultMainUi) o;
		return Objects.equals(sessionHandler, that.sessionHandler) &&
				Objects.equals(uiHandler, that.uiHandler) &&
				Objects.equals(i18n, that.i18n) &&
				Objects.equals(securityService, that.securityService) &&
				Objects.equals(viewProvider, that.viewProvider) &&
				Objects.equals(sideBar, that.sideBar) &&
				Objects.equals(sessionObjects, that.sessionObjects) &&
				Objects.equals(viewLayout, that.viewLayout);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), sessionHandler, uiHandler, i18n, securityService, viewProvider, sideBar, sessionObjects, viewLayout);
	}
}
