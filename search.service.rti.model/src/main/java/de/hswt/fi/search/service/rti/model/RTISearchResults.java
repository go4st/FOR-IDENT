package de.hswt.fi.search.service.rti.model;

import java.util.List;
import java.util.Objects;

public class RTISearchResults {

	private List<RtiSearchResult> results;

	public RTISearchResults(List<RtiSearchResult> results) {
		Objects.requireNonNull(results, "Results parameter must not be null.");
		this.results = results;
	}

	public List<RtiSearchResult> getResults() {
		return results;
	}
}
