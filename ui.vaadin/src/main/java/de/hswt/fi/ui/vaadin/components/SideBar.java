package de.hswt.fi.ui.vaadin.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import de.hswt.fi.application.properties.ApplicationProperties;
import de.hswt.fi.common.ValueFormatUtil;
import de.hswt.fi.common.spring.SpringProfileUtil;
import de.hswt.fi.search.service.search.api.CompoundSearchService;
import de.hswt.fi.security.service.api.SecurityService;
import de.hswt.fi.security.service.model.Role;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.configuration.SessionSharedObjects;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.UserPropertyPayload;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import de.hswt.fi.userproperties.service.api.UserPropertiesService;
import de.hswt.fi.userproperties.service.api.UserPropertyKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.SessionEventBus;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.sidebar.SideBarUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

@SpringComponent
@UIScope
public class SideBar extends CssLayout {

    private static final long serialVersionUID = 5789490155084386518L;

    private static final Logger LOGGER = LoggerFactory.getLogger(SideBar.class);

    private final SideBarUtils sideBarUtils;

    private final SessionEventBus sessionEventBus;

    private final ComponentFactory componentFactory;

    private final I18N i18n;

    private final UserMenu userMenu;

    private final ApplicationProperties applicationProperties;

    private final UserPropertiesService userPropertiesService;

    private final SecurityService securityService;

    private final SpringProfileUtil profileUtil;

    private final SessionSharedObjects sessionSharedObjects;

    private ViewsSideBarMenu viewSideBar;

    private Label headerLabel;

    private boolean isExpanded;

    private Button collapseButton;

    private Locale locale;

    private CssLayout bottomLayout;

    private String applicationName;

    @Autowired
    public SideBar(UserPropertiesService userPropertiesService, SideBarUtils sideBarUtils,
				   SessionEventBus sessionEventBus, ComponentFactory componentFactory, I18N i18n, UserMenu userMenu,
				   ApplicationProperties applicationProperties, SecurityService securityService,
                   SessionSharedObjects sessionSharedObjects, SpringProfileUtil profileUtil) {
        this.userPropertiesService = userPropertiesService;
        this.sideBarUtils = sideBarUtils;
        this.sessionEventBus = sessionEventBus;
        this.componentFactory = componentFactory;
        this.i18n = i18n;
        this.userMenu = userMenu;
        this.applicationProperties = applicationProperties;
        this.securityService = securityService;
        this.sessionSharedObjects = sessionSharedObjects;
        this.profileUtil = profileUtil;
    }

    @PostConstruct
    private void postConstruct() {

        locale = VaadinSession.getCurrent().getLocale();
        LOGGER.debug("Locale from VaadinSession: {}", locale);
        isExpanded = userPropertiesService.getBooleanValueOrDefault(UserPropertyKeys.SIDEBAR_EXPAND, Boolean.TRUE);
        setHeight("100%");
        setWidth(LayoutConstants.SIDEBAR_WIDTH);

        CssLayout wrapper = new CssLayout();
        wrapper.setHeight("100%");
        wrapper.addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_COLUMN);
        wrapper.addStyleName(CustomValoTheme.BACKGROUND_COLOR_DEFAULT);

        applicationName = applicationProperties.getUi().getHeader().getCaption();
        if (profileUtil.isDevelopmentProfile()) {
            applicationName += "<span style='color: #00004D; font-weight: 900;'>&nbsp;&nbsp;&nbsp;DEV</span>";
        }
        headerLabel = new Label(applicationName, ContentMode.HTML);
        headerLabel.addStyleName(CustomValoTheme.PADDING);
        headerLabel.addStyleName(CustomValoTheme.COLOR_ALT1);
        headerLabel.addStyleName(ValoTheme.LABEL_LARGE);
        headerLabel.addStyleName(CustomValoTheme.LABEL_VERY_BOLD);
        headerLabel.addStyleName(CustomValoTheme.TEXT_CENTER);
        headerLabel.addStyleName(CustomValoTheme.BACKGROUND_COLOR_GRADIENT_ALT3);
        headerLabel.setHeight(LayoutConstants.HEADER_HEIGHT_VIEW);
        wrapper.addComponent(headerLabel);

        CssLayout switchLayout = new CssLayout();
        switchLayout.addStyleName(CustomValoTheme.MARGIN);
        wrapper.addComponent(switchLayout);

        collapseButton = componentFactory.createButton(
                i18n.get(UIMessageKeys.SIDE_BAR_COLLAPSE_BUTTON_CAPTION, locale),
                VaadinIcons.CARET_LEFT,
                i18n.get(UIMessageKeys.SIDE_BAR_COLLAPSE_BUTTON_CAPTION, locale), false);
        collapseButton.addStyleName(CustomValoTheme.MARGIN_BOTTOM);
        collapseButton.addStyleName(CustomValoTheme.FLOAT_RIGHT);
        collapseButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
        collapseButton.addStyleName(CustomValoTheme.SIDE_BAR_COLLAPSE_BUTTON);
        collapseButton.addClickListener(e -> toggleSideBarWidth());

        switchLayout.addComponent(collapseButton);

        switchLayout.addComponent(createLocaleButtons());

        wrapper.addComponent(componentFactory.createHorizontalLine());

        userMenu.addStyleName(CustomValoTheme.MARGIN_TOP);
        wrapper.addComponent(userMenu);

        wrapper.addComponent(componentFactory.createHorizontalLine());

        SecurityItemFilter securityItemFilter = new SecurityItemFilter(securityService);
        viewSideBar = new ViewsSideBarMenu(sideBarUtils, securityItemFilter);
        viewSideBar.addStyleName(CustomValoTheme.MARGIN_TOP);
        viewSideBar.addStyleName(CustomValoTheme.FLEX_ITEM_EXPAND);
        wrapper.addComponent(viewSideBar);

        bottomLayout = new CssLayout();
        bottomLayout.addStyleName(CustomValoTheme.PADDING_HALF);
        wrapper.addComponent(bottomLayout);

        if (securityService.currentUserHasRole(Role.ROLE_ADMIN.name())) {
            bottomLayout.addComponent(createLabel("Version " + getCurrentSoftwareVersion()));
        }
        bottomLayout.addComponent(createLabel(i18n.get(UIMessageKeys.SIDE_BAR_LAST_UPDATE_CAPTION)));
		sessionSharedObjects.getSearchServices().forEach(this::addCompoundDatabaseUpdateTimeLabel);

        addComponent(wrapper);

        toggleSideBarWidth();
    }

    private Label createLabel(String value) {
        Label label = new Label(value);
        label.addStyleName(CustomValoTheme.FONT_COLOR_MENU);
        label.addStyleName(CustomValoTheme.PADDING_HALF_HORIZONTAL);
        label.addStyleName(CustomValoTheme.PADDING_HALF_TOP);
        label.addStyleName(CustomValoTheme.LABEL_LARGE);
        label.addStyleName(CustomValoTheme.TEXT_CENTER);

        return label;
    }

	private void addCompoundDatabaseUpdateTimeLabel(CompoundSearchService compoundSearchService) {
		Label label = new Label(compoundSearchService.getDatasourceName() + ": " + ValueFormatUtil.getDateAsString(compoundSearchService.getLastUpdateTime()));
		label.addStyleName(CustomValoTheme.FONT_COLOR_MENU);
		label.addStyleName(CustomValoTheme.PADDING_HALF_HORIZONTAL);
		label.addStyleName(CustomValoTheme.PADDING_HALF_BOTTOM);
		label.addStyleName(CustomValoTheme.LABEL_LARGE);
		label.addStyleName(CustomValoTheme.LABEL_VERY_BOLD);
		label.addStyleName(CustomValoTheme.TEXT_CENTER);
		bottomLayout.addComponent(label);
	}

	private String getCurrentSoftwareVersion() {
        Class clazz = SideBar.class;
        String className = clazz.getSimpleName() + ".class";
        String classPath = clazz.getResource(className).toString();
        if (classPath.startsWith("jar")) {

            String manifestPath = classPath.substring(0, classPath.lastIndexOf('!') + 1) +
                    "/META-INF/MANIFEST.MF";
            Manifest manifest = null;
            try {
                manifest = new Manifest(new URL(manifestPath).openStream());
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }

            assert manifest != null;
            return manifest.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION);
        }
        return "-1";
    }

    private CssLayout createLocaleButtons() {
        CssLayout languageLayout = new CssLayout();

        Button germanButton = new Button();
        germanButton.addStyleName(CustomValoTheme.BORDER_NONE);
        germanButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        germanButton.addStyleName(ValoTheme.BUTTON_QUIET);
        germanButton.addStyleName(CustomValoTheme.HEIGHT_AUTO);
        germanButton.setIcon(new ThemeResource("../img/de.png"));
        germanButton.addClickListener(e -> handleLocaleSwitch(Locale.GERMAN));
        languageLayout.addComponent(germanButton);

        Button englishButton = new Button();
        englishButton.setSizeUndefined();
        englishButton.addStyleName(CustomValoTheme.BORDER_NONE);
        englishButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        englishButton.addStyleName(ValoTheme.BUTTON_QUIET);
        englishButton.addStyleName(CustomValoTheme.BLOCK);
        englishButton.addStyleName(CustomValoTheme.HEIGHT_AUTO);
        englishButton.setIcon(new ThemeResource("../img/gb.png"));
        englishButton.addClickListener(e -> handleLocaleSwitch(Locale.US));
        languageLayout.addComponent(englishButton);

        return languageLayout;
    }

    private void toggleSideBarWidth() {
        if (isExpanded) {
            expandSideBar();
        } else {
            collapseSideBar();
        }
        LOGGER.debug("publish event inside toggleSideBarWidth with topic {}",
                EventBusTopics.TARGET_HANDLER_USER_PROPERTIES_BOOLEAN);
        sessionEventBus.publish(EventBusTopics.TARGET_HANDLER_USER_PROPERTIES_BOOLEAN, this,
                new UserPropertyPayload<>(UserPropertyKeys.SIDEBAR_EXPAND, !isExpanded));
    }

    private void collapseSideBar() {
        setState(applicationProperties.getUi().getHeader().getCollapsedCaption(),
                LayoutConstants.SIDEBAR_WIDTH_COLLAPSED, true, VaadinIcons.CARET_RIGHT);
    }

    private void expandSideBar() {
        setState(applicationName, LayoutConstants.SIDEBAR_WIDTH, false, VaadinIcons.CARET_LEFT);
    }

    private void setState(String caption, String width, boolean collapsed, Resource icon) {
        headerLabel.setValue(caption);

        collapseButton.setIcon(icon);
        if (!collapsed) {
            collapseButton.setCaption(
                    i18n.get(UIMessageKeys.SIDE_BAR_COLLAPSE_BUTTON_CAPTION, locale));
            collapseButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
            collapseButton.addStyleName(CustomValoTheme.FLOAT_RIGHT);
            collapseButton.removeStyleName(CustomValoTheme.FLOAT_CENTER);
        } else {
            collapseButton.setCaption(null);
            collapseButton.removeStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
            collapseButton.removeStyleName(CustomValoTheme.FLOAT_RIGHT);
            collapseButton.addStyleName(CustomValoTheme.FLOAT_CENTER);
        }

        userMenu.setCollapsed(collapsed);
        viewSideBar.setCollapsed(collapsed);

        setWidth(width);

        bottomLayout.setVisible(!collapsed);

        isExpanded = collapsed;
    }

    private void handleLocaleSwitch(Locale locale) {
        sessionEventBus.publish(this, locale);
    }

    // Do not implement custom hash() or equals() method here, because of reattach bug when sidebar expands

}
