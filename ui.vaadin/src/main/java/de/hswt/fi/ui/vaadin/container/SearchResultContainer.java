package de.hswt.fi.ui.vaadin.container;


import de.hswt.fi.search.service.mass.search.model.Entry;
import de.hswt.fi.search.service.mass.search.model.SearchParameter;
import de.hswt.fi.search.service.mass.search.model.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class SearchResultContainer extends ResultContainer<SearchParameter, SearchResult, Entry> {

	private SearchParameter searchParameter;

	private SearchResult result;

	private List<Entry> resultsContainer;

	private Entry currentSelection;

	public SearchResultContainer() {
		resultsContainer = new ArrayList<>();
	}

	@Override
	public SearchParameter getSearchParameter() {
		return searchParameter;
	}

	@Override
	public void setSearchParameter(SearchParameter searchParameter) {
		this.searchParameter = searchParameter;
	}

	@Override
	public List<Entry> getResultsContainer() {
		return resultsContainer;
	}

	@Override
	public void setResultsContainer(List<Entry> resultsContainer) {
		this.resultsContainer = resultsContainer;
	}

	@Override
	public SearchResult getResult() {
		return result;
	}

	@Override
	public void setResult(SearchResult result) {
		this.result = result;
	}

	@Override
	public Entry getCurrentSelection() {
		return currentSelection;
	}

	@Override
	public void setCurrentSelection(Entry currentSelection) {
		this.currentSelection = currentSelection;
	}

	@Override
	public void clear() {
		super.clear();
		searchParameter = null;
		currentSelection = null;
	}
}
