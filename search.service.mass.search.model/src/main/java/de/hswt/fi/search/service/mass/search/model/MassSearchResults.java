package de.hswt.fi.search.service.mass.search.model;

import java.util.List;
import java.util.Objects;

public class MassSearchResults {

	private List<MassSearchResult> results;

	private FileSearchResultSummary resultSummary;

	public MassSearchResults(List<MassSearchResult> results,
			FileSearchResultSummary resultSummary) {
		Objects.requireNonNull(results, "Results parameter must not be null");
		Objects.requireNonNull(resultSummary, "Result summary parameter must not be null");

		this.results = results;
		this.resultSummary = resultSummary;
	}

	public List<MassSearchResult> getResults() {
		return results;
	}

	public FileSearchResultSummary getResultSummary() {
		return resultSummary;
	}
}
