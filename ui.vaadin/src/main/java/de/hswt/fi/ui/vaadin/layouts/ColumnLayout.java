package de.hswt.fi.ui.vaadin.layouts;

import com.vaadin.ui.CssLayout;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.components.ContainerComponent;

import java.util.Objects;

public class ColumnLayout extends CssLayout implements ViewLayout {

	private static final long serialVersionUID = -829363209906325906L;

	private CssLayout searchLayout;

	private CssLayout sourceListsLayout;

	private CssLayout searchHistoryLayout;

	private CssLayout detailsLayout;

	private CssLayout resultsLayout;

	public ColumnLayout() {
		setSizeFull();
		addStyleName(CustomValoTheme.PADDING_HALF);
		addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_ROW);

		initSearchLayout();
		initResultsLayout();
		initDetailsLayout();
	}

	private void initSearchLayout() {
		CssLayout wrapper = new CssLayout();
		wrapper.setHeight("100%");
		wrapper.addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_COLUMN);
		wrapper.setWidth(LayoutConstants.SEARCH_LAYOUT_WIDTH);
		addComponent(wrapper);

		searchLayout = new CssLayout();
		searchLayout.setHeight(LayoutConstants.UPPER_SEARCH_LAYOUT_HEIGHT);
		searchLayout.addStyleName(CustomValoTheme.PADDING_HALF);
		wrapper.addComponent(this.searchLayout);

		sourceListsLayout = new CssLayout();
		sourceListsLayout.setHeight(LayoutConstants.CENTER_SEARCH_LAYOUT_HEIGHT);
		sourceListsLayout.addStyleName(CustomValoTheme.PADDING_HALF);
		wrapper.addComponent(sourceListsLayout);

		searchHistoryLayout = new CssLayout();
		searchHistoryLayout.setHeight(LayoutConstants.LOWER_SEARCH_LAYOUT_HEIGHT);
		searchHistoryLayout.addStyleName(CustomValoTheme.PADDING_HALF);
		wrapper.addComponent(searchHistoryLayout);

		sourceListsLayout.setWidth("100%");
		searchHistoryLayout.setWidth("100%");
	}

	private void initResultsLayout() {
		resultsLayout = new CssLayout();
		resultsLayout.setSizeFull();
		resultsLayout.addStyleName(CustomValoTheme.FLEX_ITEM_EXPAND);
		resultsLayout.addStyleName(CustomValoTheme.PADDING_HALF);
		addComponent(resultsLayout);
	}

	private void initDetailsLayout() {
		CssLayout containerLayout = new CssLayout();
		containerLayout.setHeight("100%");
		containerLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_COLUMN);
		containerLayout.setWidth(LayoutConstants.DETAILS_LAYOUT_WIDTH);
		addComponent(containerLayout);

		detailsLayout = new CssLayout();
		detailsLayout.setSizeFull();
		detailsLayout.addStyleName(CustomValoTheme.PADDING_HALF);
		containerLayout.addComponent(detailsLayout);
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		ColumnLayout that = (ColumnLayout) o;
		return Objects.equals(searchLayout, that.searchLayout) &&
				Objects.equals(sourceListsLayout, that.sourceListsLayout) &&
				Objects.equals(searchHistoryLayout, that.searchHistoryLayout) &&
				Objects.equals(detailsLayout, that.detailsLayout) &&
				Objects.equals(resultsLayout, that.resultsLayout);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), searchLayout, sourceListsLayout, searchHistoryLayout, detailsLayout, resultsLayout);
	}
}
