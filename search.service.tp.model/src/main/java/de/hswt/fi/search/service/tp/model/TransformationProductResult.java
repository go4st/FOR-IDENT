package de.hswt.fi.search.service.tp.model;

import java.util.List;

public class TransformationProductResult {

	private List<PathwayCandidate> inChiKeyPathwayCandidates;

	private List<PathwayCandidate> featurePathwayCandidates;

	private TransformationProductResultSummary resultSummary;

	public TransformationProductResult(List<PathwayCandidate> inChiKeyPathwayCandidates,
			List<PathwayCandidate> featurePathwayCandidates,
			TransformationProductResultSummary resultSummary) {
		this.inChiKeyPathwayCandidates = inChiKeyPathwayCandidates;
		this.featurePathwayCandidates = featurePathwayCandidates;
		this.resultSummary = resultSummary;
	}

	public List<PathwayCandidate> getInChiKeyPathwayCandidates() {
		return inChiKeyPathwayCandidates;
	}

	public List<PathwayCandidate> getFeaturePathwayCandidates() {
		return featurePathwayCandidates;
	}

	public TransformationProductResultSummary getResultSummary() {
		return resultSummary;
	}
}
