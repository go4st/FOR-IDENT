package de.hswt.fi.ui.vaadin.handler.search;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.search.service.mass.search.model.Entry;
import de.hswt.fi.search.service.mass.search.model.SearchParameter;
import de.hswt.fi.search.service.mass.search.model.SearchResult;
import de.hswt.fi.search.service.search.api.CompoundSearchService;
import de.hswt.fi.ui.vaadin.configuration.ViewSharedObjects;
import de.hswt.fi.ui.vaadin.container.SearchResultContainer;
import de.hswt.fi.ui.vaadin.handler.AbstractSearchHandler;
import de.hswt.fi.ui.vaadin.views.states.SearchViewState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.i18n.I18N;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringComponent
@ViewScope
public class SearchHandler
		extends AbstractSearchHandler<SearchParameter, SearchResult, Entry, SearchResultContainer> {

	private static final long serialVersionUID = 7266351399544389091L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchHandler.class);

	private final SearchViewState viewState;

	private final ViewSharedObjects viewSharedObjects;

	private List<CompoundSearchService> selectedSearchServices;

	@Autowired
	public SearchHandler(I18N i18n, SearchViewState viewState, ViewSharedObjects viewSharedObjects) {
		super(i18n, SearchResultContainer.class);
		this.viewState = viewState;
		this.viewSharedObjects = viewSharedObjects;
	}

	@EventBusListenerMethod
	protected void handleEvent(SearchParameter searchParameter) {
		LOGGER.debug("entering event bus listener handleEvent with payload {}", searchParameter);

		if (searchParameter == null) {
			LOGGER.debug("searchParameter is null - leaving method");
			return;
		}

		selectedSearchServices = viewSharedObjects.getSelectedSearchServices();
		LOGGER.debug("Selected search services: {}", selectedSearchServices);

		if (selectedSearchServices.isEmpty()) {
			LOGGER.debug("Selected search services are empty - returning");
			showErrorNotification();
			return;
		}

		Set<SearchResultContainer> historyContainer = viewState.getSearchHistoryContainer();
		SearchResultContainer container = getExistingContainer(searchParameter, historyContainer);

		if (container == null) {
            searchParameter.setDatasourceNames(selectedSearchServices.stream()
                    .map(CompoundSearchService::getDatasourceName)
                    .collect(Collectors.toList()));
			container = performSearch(searchParameter);
		} else if (container.equals(viewState.getCurrentSearch())) {
			LOGGER.debug("equal current search - leaving method");
			return;
		}

		viewState.setCurrentSearch(container);

		// send also an empty result to get the ui notified
		fireUpdateEvents(container);
	}

	private SearchResultContainer performSearch(SearchParameter searchParameter) {

		SearchResult result;

		if (searchParameter.getIndexChar() != ' ') {
			result = performIndexSearch(searchParameter);
		} else {
			result = performParameterizedSearch(searchParameter);
		}

		SearchResultContainer container = createContainer(searchParameter, result, result.getResults());

		if (!container.getResultsContainer().isEmpty()) {
			viewState.getSearchHistoryContainer().add(container);
		}

		return container;
	}

	private SearchResult performParameterizedSearch(SearchParameter searchParameter) {

		List<Entry> results = selectedSearchServices.stream()
				.map(searchService -> searchService.searchDynamic(searchParameter))
				.flatMap(List::stream)
				.collect(Collectors.toList());

		LOGGER.info("found {} entries", results.size());

		return new SearchResult(results);
	}

	private SearchResult performIndexSearch(SearchParameter searchParameter) {

		List<Entry> results = selectedSearchServices.stream()
				.map(searchService -> searchService.searchByNameFirstCharacter(searchParameter.getIndexChar()))
				.flatMap(List::stream)
				.collect(Collectors.toList());

		LOGGER.info("found {} entries", results.size());

		return new SearchResult(results);
	}
}
