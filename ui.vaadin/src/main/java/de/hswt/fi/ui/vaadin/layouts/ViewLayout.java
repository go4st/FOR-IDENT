package de.hswt.fi.ui.vaadin.layouts;

import de.hswt.fi.ui.vaadin.components.ContainerComponent;

public interface ViewLayout {

	void removeAll();

	void addAll(ContainerComponent searchContainer, ContainerComponent sourceListsHistoryContainer, ContainerComponent searchHistoryContainer, ContainerComponent detailsContainer, ContainerComponent resultsContainer);
}
