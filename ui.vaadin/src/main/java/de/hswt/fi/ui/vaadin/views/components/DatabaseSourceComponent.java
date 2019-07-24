package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import de.hswt.fi.search.service.search.api.CompoundSearchService;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.components.ContainerContentComponent;
import de.hswt.fi.ui.vaadin.configuration.SessionSharedObjects;
import de.hswt.fi.ui.vaadin.configuration.ViewSharedObjects;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.I18N;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@SpringComponent
@ViewScope
public class DatabaseSourceComponent extends ContainerContentComponent {

	private static final long serialVersionUID = 1L;

	private final I18N i18n;

	private final SessionSharedObjects sessionSharedObjects;

	private final ViewSharedObjects viewSharedObjects;

	private final ComponentFactory componentFactory;

	private CheckBoxGroup<CompoundSearchService> compoundDatabaseConfigurationCheckBoxGroup;

	@Autowired
	public DatabaseSourceComponent(SessionSharedObjects sessionSharedObjects, I18N i18n, ViewSharedObjects viewSharedObjects,
								   ComponentFactory componentFactory) {
		this.sessionSharedObjects = sessionSharedObjects;
		this.i18n = i18n;
		this.viewSharedObjects = viewSharedObjects;
		this.componentFactory = componentFactory;
	}

	@PostConstruct
	private void postConstruct() {
		setHeight("100%");
		setWidth("100%");
		addStyleName(CustomValoTheme.CSS_LAYOUT_SCROLLBAR);
		initSourceListsOptionGroup();
	}

	private void initSourceListsOptionGroup() {

		CssLayout layout = new CssLayout();
		layout.setSizeFull();
		layout.addStyleName(CustomValoTheme.PADDING);

		CheckBox selectAllCheckbox = new CheckBox(i18n.get(UIMessageKeys.SELECT_CHECKBOX_CAPTION));
		selectAllCheckbox.setValue(true);
		selectAllCheckbox.addValueChangeListener(event -> selectAll(event.getValue()));

		compoundDatabaseConfigurationCheckBoxGroup = new CheckBoxGroup<>();
		compoundDatabaseConfigurationCheckBoxGroup.setWidth("100%");
		compoundDatabaseConfigurationCheckBoxGroup.addValueChangeListener(e -> updateDatabaseSource());
		compoundDatabaseConfigurationCheckBoxGroup.setItems(sessionSharedObjects.getSearchServices());
		compoundDatabaseConfigurationCheckBoxGroup.setItemCaptionGenerator(CompoundSearchService::getDatasourceName);
		compoundDatabaseConfigurationCheckBoxGroup.setItemEnabledProvider(
				compoundSearchService -> sessionSharedObjects.getAvailableSearchServices().contains(compoundSearchService));

		selectAll(true);

		layout.addComponent(selectAllCheckbox);
		Label line = componentFactory.createHorizontalLine("1px");
		line.addStyleName(CustomValoTheme.MARGIN_HALF_BOTTOM);
		layout.addComponent(line);
		layout.addComponent(compoundDatabaseConfigurationCheckBoxGroup);
		addComponent(layout);
	}

	private void selectAll(boolean select) {
		if (select) {
			compoundDatabaseConfigurationCheckBoxGroup.select(sessionSharedObjects.getAvailableSearchServices().toArray(new CompoundSearchService[0]));
		} else {
			compoundDatabaseConfigurationCheckBoxGroup.deselectAll();
		}
	}

	private void updateDatabaseSource() {
		viewSharedObjects.setSelectedSearchServices(new ArrayList<>(compoundDatabaseConfigurationCheckBoxGroup.getValue()));
	}

	@Override
	public String getTitle() {
		return i18n.get(UIMessageKeys.SOURCE_LIST_COMPONENT_TITLE);
	}

	@Override
	public Component getHeaderComponent() {
		return null;
	}

}
