package de.hswt.fi.ui.vaadin.container;

import de.hswt.fi.processing.service.model.ProcessCandidate;
import de.hswt.fi.processing.service.model.ProcessingJob;
import de.hswt.fi.processing.service.model.ProcessingResult;

import java.util.List;

public class ProcessingResultContainer
		extends ResultContainer<ProcessingJob, ProcessingResult, ProcessCandidate> {

	private ProcessingJob searchParameter;

	private List<ProcessCandidate> resultsContainer;

	private ProcessCandidate currentSelection;

	@Override
	public ProcessingJob getSearchParameter() {
		return searchParameter;
	}

	@Override
	public void setSearchParameter(ProcessingJob searchParameter) {
		this.searchParameter = searchParameter;
	}

	@Override
	public List<ProcessCandidate> getResultsContainer() {
		return resultsContainer;
	}

	@Override
	public void setResultsContainer(List<ProcessCandidate> resultsContainer) {
		this.resultsContainer = resultsContainer;
	}

	@Override
	public ProcessCandidate getCurrentSelection() {
		return currentSelection;
	}

	@Override
	public void setCurrentSelection(ProcessCandidate currentSelection) {
		this.currentSelection = currentSelection;
	}

	@Override
	public void clear() {
		super.clear();
		searchParameter = null;
		resultsContainer.clear();
		currentSelection = null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (searchParameter == null ? 0 : searchParameter.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ProcessingResultContainer other = (ProcessingResultContainer) obj;
		if (searchParameter == null) {
			if (other.searchParameter != null) {
				return false;
			}
		} else if (!searchParameter.equals(other.searchParameter)) {
			return false;
		}
		return true;
	}
}
