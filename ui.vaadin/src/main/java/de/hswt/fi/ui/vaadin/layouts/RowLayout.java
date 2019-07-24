package de.hswt.fi.ui.vaadin.layouts;

import com.vaadin.ui.CssLayout;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.components.ContainerComponent;

public class RowLayout extends CssLayout implements ViewLayout {

	private static final long serialVersionUID = 2543476312119265122L;

	private CssLayout searchLayout;

	private CssLayout sourceListsLayout;

	private CssLayout searchHistoryLayout;

	private CssLayout detailsLayout;

	private CssLayout resultsLayout;

	public RowLayout() {
		setSizeFull();

		addStyleName(CustomValoTheme.PADDING_HALF);
		addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_ROW);

		initSearchLayout();
		initResultsLayout();
	}

	private void initSearchLayout() {
		CssLayout searchLayout = new CssLayout();
		searchLayout.setHeight("100%");
		searchLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_COLUMN);
		searchLayout.setWidth(LayoutConstants.SEARCH_LAYOUT_WIDTH);
		addComponent(searchLayout);

		this.searchLayout = new CssLayout();
		this.searchLayout.setHeight(LayoutConstants.UPPER_SEARCH_LAYOUT_HEIGHT);
		this.searchLayout.addStyleName(CustomValoTheme.PADDING_HALF);
		searchLayout.addComponent(this.searchLayout);

		sourceListsLayout = new CssLayout();
		sourceListsLayout.setHeight(LayoutConstants.CENTER_SEARCH_LAYOUT_HEIGHT);
		sourceListsLayout.addStyleName(CustomValoTheme.PADDING_HALF);
		searchLayout.addComponent(sourceListsLayout);

		searchHistoryLayout = new CssLayout();
		searchHistoryLayout.setHeight(LayoutConstants.LOWER_SEARCH_LAYOUT_HEIGHT);
		searchHistoryLayout.addStyleName(CustomValoTheme.PADDING_HALF);
		searchLayout.addComponent(searchHistoryLayout);
	}

	private void initResultsLayout() {
		CssLayout containerLayout = new CssLayout();
		containerLayout.setSizeFull();
		containerLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_COLUMN);
		containerLayout.addStyleName(CustomValoTheme.FLEX_ITEM_EXPAND);
		addComponent(containerLayout);

		resultsLayout = new CssLayout();
		resultsLayout.setWidth("100%");
		resultsLayout.setHeight(LayoutConstants.UPPER_RESULTS_LAYOUT_HEIGHT);
		resultsLayout.addStyleName(CustomValoTheme.PADDING_HALF);
		containerLayout.addComponent(resultsLayout);

		CssLayout lowerLayout = new CssLayout();
		lowerLayout.setWidth("100%");
		lowerLayout.setHeight(LayoutConstants.LOWER_RESULTS_LAYOUT_HEIGHT);
		lowerLayout.addStyleName(CustomValoTheme.PADDING_HALF);
		lowerLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_ROW);
		containerLayout.addComponent(lowerLayout);

		detailsLayout = new CssLayout();
		detailsLayout.setHeight("100%");
		detailsLayout.addStyleName(CustomValoTheme.FLEX_ITEM_EXPAND);
		detailsLayout.addStyleName(CustomValoTheme.PADDING_HALF_RIGHT);
		lowerLayout.addComponent(detailsLayout);
	}

	@Override
	public void removeAll() {
		searchLayout.removeAllComponents();
		sourceListsLayout.removeAllComponents();
		searchHistoryLayout.removeAllComponents();
		detailsLayout.removeAllComponents();
		resultsLayout.removeAllComponents();
	}

	@Override
	public void addAll(ContainerComponent searchContainer, ContainerComponent sourceListsContainer, ContainerComponent searchHistoryContainer, ContainerComponent resultsContainer, ContainerComponent detailsContainer) {
		removeAll();
		if (searchContainer != null) {
			searchLayout.addComponent(searchContainer);
		}
		if (sourceListsContainer != null) {
			sourceListsLayout.addComponent(sourceListsContainer);
		}
		if (searchHistoryContainer != null) {
			searchHistoryLayout.addComponent(searchHistoryContainer);
		}
		if (detailsContainer != null) {
			detailsLayout.addComponent(detailsContainer);
		}
		if (resultsContainer != null) {
			resultsLayout.addComponent(resultsContainer);
		}
	}
}
