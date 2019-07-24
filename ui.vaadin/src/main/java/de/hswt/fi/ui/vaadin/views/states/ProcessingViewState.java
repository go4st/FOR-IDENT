package de.hswt.fi.ui.vaadin.views.states;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.ui.vaadin.container.ProcessingResultContainer;

import java.util.HashSet;
import java.util.Set;

@SpringComponent
@ViewScope
public class ProcessingViewState {

	private Set<ProcessingResultContainer> searchHistoryContainer;

	private ProcessingResultContainer currentSearch;

	public ProcessingViewState() {
		searchHistoryContainer = new HashSet<>();
	}

	public Set<ProcessingResultContainer> getSearchHistoryContainer() {
		return searchHistoryContainer;
	}

	public ProcessingResultContainer getCurrentSearch() {
		return currentSearch;
	}

	public void setCurrentSearch(ProcessingResultContainer currentSearch) {
		this.currentSearch = currentSearch;
	}
}
