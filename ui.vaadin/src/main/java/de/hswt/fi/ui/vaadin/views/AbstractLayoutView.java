package de.hswt.fi.ui.vaadin.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;
import de.hswt.fi.application.properties.ApplicationProperties;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.components.ContainerComponent;
import de.hswt.fi.ui.vaadin.components.ContainerContentComponent;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.layouts.ColumnLayout;
import de.hswt.fi.ui.vaadin.layouts.MaxLayout;
import de.hswt.fi.ui.vaadin.layouts.RowLayout;
import de.hswt.fi.ui.vaadin.layouts.ViewLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.teemu.switchui.Switch;

import javax.inject.Provider;

public abstract class AbstractLayoutView extends AbstractView {

	private static final long serialVersionUID = 8465304014768910969L;

	private static final Logger LOG = LoggerFactory.getLogger(AbstractLayoutView.class);

	protected enum Layout {
		COLUMN, ROW, MAX
	}

	private ApplicationProperties applicationProperties;

	private I18N i18n;

	private Provider<ContainerComponent> sectionProvider;

	private CssLayout headerLayout;

	private CssLayout layoutWrapper;

	private ViewLayout currentLayout;

	private ColumnLayout columnLayout;

	private RowLayout rowLayout;

	private MaxLayout maxLayout;

	private ContainerComponent searchContainerComponent;

	private ContainerComponent sourceListsContainerComponent;

	private ContainerComponent searchHistoryContainerComponent;

	private ContainerComponent detailsContainerComponent;

	private ContainerComponent resultsContainerComponent;

	private MenuBar layoutMenuBar;

	private MenuItem rowMenuItem;

	private MenuItem columnMenuItem;

	private MenuItem maxMenuItem;

	private boolean fireHeaderUpdate = true;

	private Switch headerVisibilitySwitchButton;

	protected abstract Layout getDefaultLayout();

	protected abstract String getViewTitle();

	protected abstract void initComponents();

	@Override
	protected void postConstruct() {
		setSizeFull();

		addStyleName(CustomValoTheme.BACKGROUND_COLOR_WHITE);
		addStyleName(CustomValoTheme.RELATIVE);
		addStyleName(CustomValoTheme.BLOCK);

		initHeader();
		initHeaderButtons();

		initContainer();

		initLayouts();

		initComponents();

		handleLayoutSelection(getDefaultLayout());
	}

	private void initLayouts() {
		columnLayout = new ColumnLayout();
		rowLayout = new RowLayout();
		maxLayout = new MaxLayout();
	}

	private void initContainer() {
		layoutWrapper = new CssLayout();
		layoutWrapper.addStyleName(CustomValoTheme.SKIP_HEADER_VIEW);
		layoutWrapper.addStyleName(CustomValoTheme.BLOCK);
		layoutWrapper.addStyleName(CustomValoTheme.RELATIVE);
		layoutWrapper.setSizeFull();
		addComponent(layoutWrapper);

		searchContainerComponent = sectionProvider.get();
		searchContainerComponent.setSizeFull();

		sourceListsContainerComponent = sectionProvider.get();
		sourceListsContainerComponent.setSizeFull();

		searchHistoryContainerComponent = sectionProvider.get();
		searchHistoryContainerComponent.setSizeFull();

		detailsContainerComponent = sectionProvider.get();
		detailsContainerComponent.setSizeFull();

		resultsContainerComponent = sectionProvider.get();
		resultsContainerComponent.setSizeFull();
	}

	private void initHeader() {
		headerLayout = new CssLayout();
		headerLayout.setWidth("100%");
		headerLayout.addStyleName(CustomValoTheme.BACKGROUND_COLOR_DEFAULT);
		headerLayout.addStyleName(CustomValoTheme.PADDING);
		headerLayout.addStyleName(CustomValoTheme.CSS_SHADOW_BORDER);
		headerLayout.setHeight(LayoutConstants.HEADER_HEIGHT_VIEW);
		addComponent(headerLayout);

		Label headerLabel = new Label(getViewTitle());
		headerLabel.addStyleName(CustomValoTheme.COLOR_ALT1);
		headerLabel.addStyleName(ValoTheme.LABEL_LARGE);
		headerLabel.addStyleName(CustomValoTheme.LABEL_VERY_BOLD);
		headerLabel.setWidthUndefined();
		headerLayout.addComponent(headerLabel);
	}

	private void initHeaderButtons() {
		CssLayout buttonLayout = new CssLayout();
		buttonLayout.addStyleName(CustomValoTheme.FLOAT_RIGHT);
		headerLayout.addComponent(buttonLayout);

		Label layoutLabel = new Label(
				i18n.get(UIMessageKeys.ABSTRACT_LAYOUT_VIEW_CHANGE_LAYOUT_CAPTION));
		layoutLabel.addStyleName(CustomValoTheme.COLOR_ALT1);
		layoutLabel.addStyleName(ValoTheme.LABEL_LARGE);
		layoutLabel.addStyleName(ValoTheme.LABEL_BOLD);
		layoutLabel.addStyleName(CustomValoTheme.MARGIN_RIGHT);
		layoutLabel.setWidthUndefined();
		buttonLayout.addComponent(layoutLabel);

		layoutMenuBar = new MenuBar();
		layoutMenuBar.addStyleName(CustomValoTheme.MENU_BAR_HEADER);
		layoutMenuBar.setHtmlContentAllowed(true);

		columnMenuItem = layoutMenuBar.addItem("<span class=\"v-menubar-menuitem-caption\">"
						+ VaadinIcons.MENU.getHtml().replace("v-icon", "v-icon rotate-90") + "</span>",
				e -> handleLayoutSelection(Layout.COLUMN));
		columnMenuItem.setCheckable(true);

		rowMenuItem = layoutMenuBar.addItem("", e -> handleLayoutSelection(Layout.ROW));
		rowMenuItem.setCheckable(true);
		rowMenuItem.setIcon(VaadinIcons.MENU);

		maxMenuItem = layoutMenuBar.addItem("", e -> handleLayoutSelection(Layout.MAX));
		maxMenuItem.setCheckable(true);
		maxMenuItem.setIcon(VaadinIcons.BROWSER);

		buttonLayout.addComponent(layoutMenuBar);

		if (!applicationProperties.getUi().getHeader().isVisible()) {
			return;
		}

		layoutMenuBar.addStyleName(CustomValoTheme.MARGIN_RIGHT);

		Label headerLabel = new Label("Maximize");
		headerLabel.addStyleName(CustomValoTheme.COLOR_ALT1);
		headerLabel.addStyleName(ValoTheme.LABEL_LARGE);
		headerLabel.addStyleName(ValoTheme.LABEL_BOLD);
		headerLabel.addStyleName(CustomValoTheme.MARGIN_RIGHT);
		headerLabel.addStyleName(CustomValoTheme.MARGIN_LEFT);
		headerLabel.setWidthUndefined();
		buttonLayout.addComponent(headerLabel);

		headerVisibilitySwitchButton = new Switch();
		headerVisibilitySwitchButton.setPrimaryStyleName("v-custom-switch");
		headerVisibilitySwitchButton.addStyleName("dark");
		headerVisibilitySwitchButton.addStyleName(CustomValoTheme.BORDER_COLOR_WHITE);
		headerVisibilitySwitchButton.addValueChangeListener(e -> switchHeaderVisibility(headerVisibilitySwitchButton.getValue()));
		buttonLayout.addComponent(headerVisibilitySwitchButton);
	}

	private void handleLayoutSelection(Layout layout) {
		setLayout(layout);
		for (MenuItem item : layoutMenuBar.getItems()) {
			item.setChecked(false);
		}

		switch (layout) {
			case COLUMN:
				columnMenuItem.setChecked(true);
				break;
			case ROW:
				rowMenuItem.setChecked(true);
				break;
			case MAX:
				maxMenuItem.setChecked(true);
				break;
			default:
				break;
		}
	}

	private void setLayout(Layout layout) {
		if (currentLayout != null) {
			currentLayout.removeAll();
			layoutWrapper.removeComponent((CssLayout) currentLayout);
		}

		if (layout == null) {
			layout = Layout.COLUMN;
		}

		currentLayout = getLayout(layout);

		layoutWrapper.addComponent((CssLayout) currentLayout);

		currentLayout.addAll(searchContainerComponent, sourceListsContainerComponent,
				searchHistoryContainerComponent, resultsContainerComponent,
				detailsContainerComponent);

		fireLayoutChanged(currentLayout);
	}

	private void switchHeaderVisibility(boolean visible) {
		if (!fireHeaderUpdate) {
			return;
		}

		LOG.debug("publish session event inside switchHeaderVisibility with value {} and topic {}",
				visible, EventBusTopics.TARGET_HANDLER_HEADER_SWITCH);
		eventBus.publish(EventScope.SESSION, EventBusTopics.TARGET_HANDLER_HEADER_SWITCH, this,
				!visible);
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_HEADER_SWITCH_UPDATE)
	private void handleHeaderVisibilityChanged(Boolean visible) {
		LOG.debug(
				"entering event bus listener handleHeaderVisibilityChanged with payload {} in topic {}",
				visible, EventBusTopics.TARGET_HANDLER_HEADER_SWITCH_UPDATE);

		if (headerVisibilitySwitchButton == null || headerVisibilitySwitchButton.getValue() == visible) {
			return;
		}

		fireHeaderUpdate = false;
		headerVisibilitySwitchButton.setValue(!visible);
		fireHeaderUpdate = true;
	}

	private void fireLayoutChanged(ViewLayout currentLayout) {
		if (searchContainerComponent != null) {
			searchContainerComponent.layoutChanged(currentLayout);
		}
		if (sourceListsContainerComponent != null) {
			sourceListsContainerComponent.layoutChanged(currentLayout);
		}
		if (searchHistoryContainerComponent != null) {
			searchHistoryContainerComponent.layoutChanged(currentLayout);
		}
		if (detailsContainerComponent != null) {
			detailsContainerComponent.layoutChanged(currentLayout);
		}
		if (resultsContainerComponent != null) {
			resultsContainerComponent.layoutChanged(currentLayout);
		}
	}

	private ViewLayout getLayout(Layout layout) {
		switch (layout) {
			case ROW:
				return rowLayout;
			case MAX:
				return maxLayout;
			default:
				return columnLayout;
		}
	}

	protected void addSearchComponent(ContainerContentComponent content) {
		searchContainerComponent.addContentComponent(content);
	}

	protected void clearSearchComponents() {
		searchContainerComponent.clearContentComponents();
	}

	protected void addSourceListsComponent(ContainerContentComponent content) {
		sourceListsContainerComponent.addContentComponent(content);
	}

	protected void clearSourceListsComponents() {
		sourceListsContainerComponent.clearContentComponents();
	}

	protected void addDetailsComponent(ContainerContentComponent content) {
		detailsContainerComponent.addContentComponent(content);
	}

	protected void clearDetailsComponents() {
		detailsContainerComponent.clearContentComponents();
	}

	protected void changeDetailsComponent(ContainerContentComponent details) {
		detailsContainerComponent.clearContentComponents();
		detailsContainerComponent.addContentComponent(details);
	}

	protected void addResultsComponent(ContainerContentComponent content) {
		resultsContainerComponent.addContentComponent(content);
	}

	protected void setCurrentResultComponent(ContainerContentComponent content) {
		resultsContainerComponent.setCurrentComponent(content);
	}

	protected void clearResultsComponents() {
		resultsContainerComponent.clearContentComponents();
	}

	protected void addSearchHistoryComponent(ContainerContentComponent content) {
		searchHistoryContainerComponent.addContentComponent(content);
	}

	protected void clearSearchHistoryComponents() {
		searchHistoryContainerComponent.clearContentComponents();
	}

	@Autowired
	public void setApplicationProperties(ApplicationProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	@Autowired
	public void setI18n(I18N i18n) {
		this.i18n = i18n;
	}

	@Autowired
	public void setSectionProvider(Provider<ContainerComponent> sectionProvider) {
		this.sectionProvider = sectionProvider;
	}

	// Do not override hash() and equals() in abstract component classes because of identity issues when attach / remove
}