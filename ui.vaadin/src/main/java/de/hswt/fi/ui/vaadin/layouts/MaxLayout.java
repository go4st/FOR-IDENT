package de.hswt.fi.ui.vaadin.layouts;

import com.vaadin.ui.CssLayout;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.components.ContainerComponent;

public class MaxLayout extends CssLayout implements ViewLayout {

	private static final long serialVersionUID = 1L;

	private CssLayout resultsLayout;

	public MaxLayout() {
		setSizeFull();
		addStyleName(CustomValoTheme.PADDING_HALF);
		addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_ROW);

		initResultsLayout();
	}

	private void initResultsLayout() {
		resultsLayout = new CssLayout();
		resultsLayout.setSizeFull();
		resultsLayout.addStyleName(CustomValoTheme.FLEX_ITEM_EXPAND);
		resultsLayout.addStyleName(CustomValoTheme.PADDING_HALF);
		addComponent(resultsLayout);
	}

	@Override
	public void removeAll() {
		resultsLayout.removeAllComponents();
	}

	@Override
	public void addAll(ContainerComponent searchContainer, ContainerComponent sourceListsContainer, ContainerComponent searchHistoryContainer, ContainerComponent resultsContainer, ContainerComponent detailsContainer) {
		removeAll();
		if (resultsContainer != null) {
			resultsLayout.addComponent(resultsContainer);
		}
	}
}
