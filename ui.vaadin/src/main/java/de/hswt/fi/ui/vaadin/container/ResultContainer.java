package de.hswt.fi.ui.vaadin.container;

import java.util.List;

public abstract class ResultContainer<SEARCHPARAMETER, RESULT, ENTRY> {

	private RESULT result;

	public abstract SEARCHPARAMETER getSearchParameter();

	public abstract void setSearchParameter(SEARCHPARAMETER searchParameter);

	public abstract List<ENTRY> getResultsContainer();

	public abstract void setResultsContainer(List<ENTRY> resultsContainer);

	public abstract ENTRY getCurrentSelection();

	public abstract void setCurrentSelection(ENTRY currentSelection);

	public RESULT getResult() {
		return result;
	}

	public void setResult(RESULT result) {
		this.result = result;
	}

	public void clear() {
		result = null;
	}
}
