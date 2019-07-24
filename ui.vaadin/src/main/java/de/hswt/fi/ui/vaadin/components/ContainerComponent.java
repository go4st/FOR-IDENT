package de.hswt.fi.ui.vaadin.components;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.layouts.ViewLayout;
import de.hswt.fi.ui.vaadin.views.ProcessingView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.events.EventBus.ViewEventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringComponent
@PrototypeScope
public class ContainerComponent extends CssLayout {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingView.class);

	@Autowired
	private ViewEventBus eventBus;
	
	private Label titleLabel;

	private CssLayout headerContentLayout;

	private CssLayout contentLayout;

	private List<ContainerContentComponent> contentComponents;

	private CssLayout headerLayout;

	private Map<ContainerContentComponent, Button> headerButtons;

	private TabSheet tabSheet = new TabSheet();

	public ContainerComponent() {
		addStyleName(CustomValoTheme.CSS_SHADOW_BORDER);
		addStyleName(CustomValoTheme.RELATIVE);
		addStyleName(CustomValoTheme.BLOCK);

		contentComponents = new ArrayList<>();
		headerButtons = new HashMap<>();

		// must be in this order, to prevent the content
		// container to overlay the header
		// if this is the case, the buttons will not
		// react on user actions
		initContentContainer();

		initHeader();
	}

	private void initHeader() {
		headerLayout = new CssLayout();
		headerLayout.setWidth("100%");
		headerLayout.setHeight(LayoutConstants.HEADER_HEIGHT_CONTENT_COMPONENT);
		headerLayout.addStyleName(CustomValoTheme.PADDING_HORIZONTAL);
		headerLayout.addStyleName(CustomValoTheme.BACKGROUND_COLOR_ALT3);
		headerLayout.addStyleName(CustomValoTheme.HIDDEN_OVERFLOW);
		addComponent(headerLayout);

		titleLabel = new Label();
		headerLayout.addComponent(titleLabel);

		headerContentLayout = new CssLayout();
		headerContentLayout.addStyleName(CustomValoTheme.PADDING_HALF_VERTICAL);
		headerContentLayout.addStyleName(CustomValoTheme.FLOAT_RIGHT);
		headerLayout.addComponent(headerContentLayout);
	}

	private void initContentContainer() {
		contentLayout = new CssLayout();
		contentLayout.setSizeFull();
		contentLayout.addStyleName(CustomValoTheme.SKIP_HEADER_CONTAINER);
		contentLayout.addStyleName(CustomValoTheme.BLOCK);
		addComponent(contentLayout);
	}

	public void addContentComponent(ContainerContentComponent content) {
		if (content == null || contentComponents.contains(content)) {
			return;
		}

		contentComponents.add(content);

		if (contentComponents.size() == 1) {

			Label tempLabel = initTitleLabel(content.getTitleLabel());
			headerLayout.replaceComponent(titleLabel, tempLabel);
			titleLabel = tempLabel;
			if (content.getHeaderComponent() != null) {
				headerContentLayout.removeAllComponents();
				headerContentLayout.addComponent(content.getHeaderComponent());
			}
			contentLayout.addComponent(content);
		} else if (contentComponents.size() == 2) {
			replaceSingleContent();
			addComponent(content);
		} else {
			addComponent(content);
		}
	}

	private Label initTitleLabel(Label titleLabel) {
		titleLabel.setWidthUndefined();
		titleLabel.addStyleName(CustomValoTheme.MARGIN_HALF_VERTICAL);
		titleLabel.addStyleName(CustomValoTheme.COLOR_ALT1);
		titleLabel.addStyleName(CustomValoTheme.LABEL_BOLD);
		titleLabel.addStyleName(CustomValoTheme.LABEL_LARGE);
		return titleLabel;
	}

	public void setCurrentComponent(ContainerContentComponent component) {
		if (tabSheet == null || component == null || component.equals(tabSheet.getSelectedTab())) {
			return;
		}

		tabSheet.setSelectedTab(component);
	}

	public void clearContentComponents() {
		contentLayout.removeAllComponents();
		contentComponents.clear();
		headerButtons.clear();
		if (tabSheet != null) {
			tabSheet.removeAllComponents();
		}
	}

	public String getTitle() {
		return "";
	}

	public boolean hasContent() {
		return contentLayout.getComponentCount() > 0;
	}

	public void layoutChanged(ViewLayout currentLayout) {
		contentComponents.forEach(component -> component.layoutChanged(currentLayout));
	}

	private void replaceSingleContent() {

		titleLabel.setValue("");

		contentLayout.removeAllComponents();
		initTabSheet();

		contentLayout.addComponent(tabSheet);

		addComponent(contentComponents.get(0));
	}

	private void initTabSheet() {
		tabSheet = new TabSheet();
		tabSheet.setSizeFull();
		tabSheet.addStyleName(CustomValoTheme.TABSHEET_PADDED_TABBAR);
		tabSheet.addStyleName("content-component");
		tabSheet.addSelectedTabChangeListener(event -> updateHeaderContent(
				(ContainerContentComponent) event.getTabSheet().getSelectedTab()));
	}

	private void addComponent(ContainerContentComponent component) {
		tabSheet.addTab(component, component.getTitle());
		
		setCurrentComponent(component);
	}

	private void updateHeaderContent(ContainerContentComponent content) {
		if (content == null) {
			return;
		}
		if (headerContentLayout.getComponentCount() > 0) {
			headerContentLayout.removeAllComponents();
		}
		if (content.getHeaderComponent() != null) {
			headerContentLayout.addComponent(content.getHeaderComponent());
		}
		LOGGER.debug(
				"entering event bus listener setCurrentComponent with payload {} and topic {}",
				content, EventBusTopics.PROCESSING_RESULTS_TAB_CHANGED);
		eventBus.publish(EventBusTopics.PROCESSING_RESULTS_TAB_CHANGED, this, content);
	}
}