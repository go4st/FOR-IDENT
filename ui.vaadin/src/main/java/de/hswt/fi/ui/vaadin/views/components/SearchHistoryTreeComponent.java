package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.search.service.mass.search.model.Entry;
import de.hswt.fi.search.service.mass.search.model.SearchParameter;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.container.SearchHistoryTreeRootItem;
import de.hswt.fi.ui.vaadin.container.SearchResultContainer;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DummyPayload;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;

@SpringComponent
@ViewScope
public class SearchHistoryTreeComponent extends AbstractSearchHistoryComponent<SearchResultContainer> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchHistoryTreeComponent.class);

	private int searchCounter;

	@Autowired
	public SearchHistoryTreeComponent(EventBus.ViewEventBus eventBus, I18N i18n, ComponentFactory componentFactory) {
		super(eventBus, i18n, componentFactory);
		searchCounter = 1;
	}


	@Override
	public String getTitle() {
		return i18n.get(UIMessageKeys.SEARCH_VIEW_SEARCH_HISTORY_TITLE);
	}

	@Override
	protected SearchHistoryTreeRootItem<SearchResultContainer> createRootItem(SearchResultContainer searchResultContainer) {

		SearchHistoryTreeRootItem<SearchResultContainer> rootItem = new SearchHistoryTreeRootItem<>(getSearchCaption(),
				searchResultContainer,
				i18n.get(UIMessageKeys.SEARCH_HISTORY_TREE_ITEM_PARAMETER),
				i18n.get(UIMessageKeys.SEARCH_HISTORY_TREE_ITEM_SELECTED_RESULTS));

		getSearchParameterCaptions(searchResultContainer.getSearchParameter())
				.forEach(rootItem::addParameter);

		return rootItem;
	}

	private String getSearchCaption() {
		return i18n.get(UIMessageKeys.SEARCH_HISTORY_TREE_ITEM_SEARCH) + searchCounter++;
	}

	private List<String> getSearchParameterCaptions(SearchParameter searchParameter) {

		List<String> parameters = new ArrayList<>();

		// Parse search parameters
		if (searchParameter.getIndexChar() != ' ') {
			parameters.add(i18n.get(UIMessageKeys.SEARCH_HISTORY_TREE_INDEX_CHAR) + ": "
					+ searchParameter.getIndexChar());

			addDatasourceNames(searchParameter, parameters);

			return parameters;
		}

		addSearchParameterIfPresent(searchParameter.getPublicID(), i18n.get(UIMessageKeys.SEARCH_FORM_COMPONENT_PUBLIC_ID_CAPTION), parameters);
		addSearchParameterIfPresent(searchParameter.getInchiKey(), i18n.get(UIMessageKeys.SEARCH_FORM_COMPONENT_INCHI_KEY_CAPTION), parameters);
		addSearchParameterIfPresent(searchParameter.getCas(), i18n.get(UIMessageKeys.SEARCH_FORM_COMPONENT_CAS_CAPTION), parameters);
		addSearchParameterIfPresent(searchParameter.getName(), i18n.get(UIMessageKeys.SEARCH_FORM_COMPONENT_NAME_CAPTION), parameters);

		addRangeParameterIfPresent(searchParameter.getAccurateMass(), i18n.get(UIMessageKeys.SEARCH_FORM_COMPONENT_MASS_CAPTION),
				searchParameter.getPpm(), i18n.get(UIMessageKeys.SEARCH_FORM_PPM_CAPTION), parameters);

		addSearchParameterIfPresent(searchParameter.getElementalFormula(), i18n.get(UIMessageKeys.SEARCH_FORM_COMPONENT_FORMULA_CAPTION), parameters);

		if (!searchParameter.getHalogens().isEmpty()) {
			parameters.add(i18n.get(UIMessageKeys.SEARCH_FORM_HALOGEN_LAYOUT_CAPTION) + ": " + String.join(", ", searchParameter.getHalogens()));
		}

		addSearchParameterIfPresent(searchParameter.getIonisation().toString(), i18n.get(UIMessageKeys.IONISATION_COMBO_BOX_CAPTION), parameters);
		addSearchParameterIfPresent(searchParameter.getIupac(), i18n.get(UIMessageKeys.SEARCH_FORM_COMPONENT_IUPAC_CAPTION), parameters);

		addRangeParameterIfPresent(searchParameter.getLogD(), i18n.get(UIMessageKeys.SEARCH_FORM_COMPONENT_LOG_D_CAPTION),
				searchParameter.getLogDDelta(), "", parameters);

		addRangeParameterIfPresent(searchParameter.getLogP(), i18n.get(UIMessageKeys.SEARCH_FORM_COMPONENT_LOG_P_CAPTION),
				searchParameter.getLogPDelta(), "", parameters);

		addSearchParameterIfPresent(searchParameter.getSmiles(), i18n.get(UIMessageKeys.SEARCH_FORM_COMPONENT_SMILES_CAPTION), parameters);
		addDatasourceNames(searchParameter, parameters);

		return parameters;
	}

	private void addRangeParameterIfPresent(Double value, String valueCaption, Double range, String rangeCaption, List<String> parameters) {

		String resultString = valueCaption + ": " + value;

		if (range != null) {
			resultString += " " + LayoutConstants.UNICODE_PLUS_MINUS_SIGN + rangeCaption + ": " + range;
		}

		parameters.add(resultString);
	}

	private void addSearchParameterIfPresent(String parameter, String caption, List<String> parameters) {
		if (!isNullOrEmpty(parameter)) {
			parameters.add(caption + ": " + parameter);
		}
	}

	private void addDatasourceNames(SearchParameter searchParameter, List<String> parameters) {
		if (!searchParameter.getDatasourceNames().isEmpty()) {
			parameters.add(i18n.get(UIMessageKeys.SEARCH_FORM_COMPONENT_DATABASES_CAPTION) + ": "
					+ String.join(", ", searchParameter.getDatasourceNames()));
		}
	}

	protected String getDisplayedName(Entry entry) {

		String value = entry.getName().getValue();
		if (!value.isEmpty()) {
			return value;
		}

		value = entry.getCas().getValue();
		if (!value.isEmpty()) {
			return value;
		}

		value = entry.getSmiles().getValue();
		if (!value.isEmpty()) {
			return value;
		}

		return i18n.get(UIMessageKeys.SEARCH_HISTORY_TREE_ITEM_UNDECLARED_ENTRY);
	}

	@Override
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.SOURCE_HANDLER_CLEARED_SEARCH_HISTORY)
	protected void handleClearHistory(DummyPayload payload) {
		LOGGER.debug("entering event bus listener handleClearHistory with payload {} in topic {}",
				payload, EventBusTopics.SOURCE_HANDLER_CLEARED_SEARCH_HISTORY);

		searchCounter = 1;
		handleClear();
	}
}
