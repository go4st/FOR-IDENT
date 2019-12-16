package de.hswt.fi.search.service.index.model;

import java.util.List;
import java.util.Objects;

public class IndexSearchResults<T extends IndexSearchResult> {

	private List<T> results;

	public IndexSearchResults(List<T> results) {
		Objects.requireNonNull(results, "Results parameter must not be null.");
		this.results = results;
	}

	public List<T> getResults() {
		return results;
	}
}
