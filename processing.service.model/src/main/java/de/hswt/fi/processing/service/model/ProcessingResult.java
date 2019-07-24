package de.hswt.fi.processing.service.model;

import java.util.List;
import java.util.Objects;

public class ProcessingResult {

	private List<ProcessCandidate> results;

	private ProcessResultSummary resultSummary;

	public ProcessingResult(List<ProcessCandidate> results,
							ProcessResultSummary resultSummary) {
		Objects.requireNonNull(results, "Results parameter is null");
		Objects.requireNonNull(resultSummary, "Result summary parameter is null");
		this.results = results;
		this.resultSummary = resultSummary;
	}

	public List<ProcessCandidate> getResults() {
		return results;
	}

	public ProcessResultSummary getResultSummary() {
		return resultSummary;
	}
}
