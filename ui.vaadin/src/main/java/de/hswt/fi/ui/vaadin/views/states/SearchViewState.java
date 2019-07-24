package de.hswt.fi.ui.vaadin.views.states;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.ui.vaadin.container.SearchResultContainer;

import java.util.HashSet;
import java.util.Set;

@SpringComponent
@ViewScope
public class SearchViewState {

	private Set<SearchResultContainer> searchHistoryContainer;

	private SearchResultContainer currentSearch;

	public SearchViewState() {
		searchHistoryContainer = new HashSet<>();
	}

	public Set<SearchResultContainer> getSearchHistoryContainer() {
		return searchHistoryContainer;
	}

	public void setCurrentSearch(SearchResultContainer currentSearch) {
		this.currentSearch = currentSearch;
	}

	public SearchResultContainer getCurrentSearch() {
		return currentSearch;
	}
}
