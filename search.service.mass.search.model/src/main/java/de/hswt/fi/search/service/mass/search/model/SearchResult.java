package de.hswt.fi.search.service.mass.search.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchResult {

	private List<Entry> results;

	public SearchResult() {
		this(new ArrayList<>());
	}

	public SearchResult(List<Entry> results) {
		Objects.requireNonNull(results, "Results parameter must not be null.");

		this.results = results;
	}

	public List<Entry> getResults() {
		return results;
	}
}
