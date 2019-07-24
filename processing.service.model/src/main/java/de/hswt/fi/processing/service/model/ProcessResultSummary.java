package de.hswt.fi.processing.service.model;

import de.hswt.fi.model.ResultSummary;

import java.util.ArrayList;
import java.util.List;

public class ProcessResultSummary {

	private List<ResultSummary> resultSummaries;

	public ProcessResultSummary() {
		resultSummaries = new ArrayList<>();
	}

	public List<ResultSummary> getResultSummaries() {
		return resultSummaries;
	}

	public void addResultSummary(ResultSummary resultSummary) {
		if (resultSummary == null || resultSummaries.contains(resultSummary)) {
			return;
		}
		resultSummaries.add(resultSummary);
	}
}
