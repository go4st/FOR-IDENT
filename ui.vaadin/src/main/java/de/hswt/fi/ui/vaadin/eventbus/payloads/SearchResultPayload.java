package de.hswt.fi.ui.vaadin.eventbus.payloads;

import de.hswt.fi.search.service.mass.search.model.Entry;
import de.hswt.fi.search.service.mass.search.model.SearchParameter;

import java.util.List;

public class SearchResultPayload {

	private List<Entry> results;

	private SearchParameter searchParameter;

	public SearchResultPayload(SearchParameter searchParameter, List<Entry> results) {
		this.searchParameter = searchParameter;
		this.results = results;
	}

	public SearchParameter getSearchParameter() {
		return searchParameter;
	}

	public List<Entry> getResults() {
		return results;
	}

}
