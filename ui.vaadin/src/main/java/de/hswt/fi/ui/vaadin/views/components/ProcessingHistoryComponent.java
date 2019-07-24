package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.processing.service.model.ProcessingJob;
import de.hswt.fi.search.service.mass.search.model.Entry;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.container.ProcessingResultContainer;
import de.hswt.fi.ui.vaadin.container.SearchHistoryTreeItem;
import de.hswt.fi.ui.vaadin.container.SearchHistoryTreeRootItem;
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

@SpringComponent
@ViewScope
public class ProcessingHistoryComponent extends AbstractSearchHistoryComponent<ProcessingResultContainer> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(ProcessingHistoryComponent.class);

	@Autowired
	public ProcessingHistoryComponent(EventBus.ViewEventBus eventBus, I18N i18n, ComponentFactory componentFactory) {
		super(eventBus, i18n, componentFactory);
	}

	@Override
	public String getTitle() {
		return i18n.get(UIMessageKeys.PROCESSING_VIEW_SEARCH_HISTORY_TITLE);
	}

	@Override
	protected SearchHistoryTreeRootItem<ProcessingResultContainer> createRootItem(ProcessingResultContainer searchResultContainer) {

		SearchHistoryTreeRootItem<ProcessingResultContainer> rootItem = new SearchHistoryTreeRootItem<>(getSearchCaption(searchResultContainer),
				searchResultContainer,
				i18n.get(UIMessageKeys.SEARCH_HISTORY_TREE_ITEM_PARAMETER),
				i18n.get(UIMessageKeys.SEARCH_HISTORY_TREE_ITEM_SELECTED_RESULTS));

		getSearchParameterCaptions(searchResultContainer.getSearchParameter())
				.forEach(searchParameter -> rootItem.addChild(new SearchHistoryTreeItem<>(searchParameter)));

		return rootItem;
	}

	private List<String> getSearchParameterCaptions(ProcessingJob job) {

		List<String> parameters = new ArrayList<>();

		parameters.add(
				i18n.get(UIMessageKeys.SEARCH_HISTORY_TREE_PH) + ": " + job.getSettings().getPh());

		parameters.add(i18n.get(UIMessageKeys.PROCESSING_FORM_PRECURSOR_PPM) + ": "
				+ job.getSettings().getPrecursorPpm());

		parameters.add(i18n.get(UIMessageKeys.PROCESSING_FORM_FRAGMENTS_PPM) + ": "
				+ job.getSettings().getPpmFragments());

		parameters.add(i18n.get(UIMessageKeys.IONISATION_COMBO_BOX_CAPTION) + ": "
				+ job.getSettings().getIonisation());

		parameters.add(i18n.get(UIMessageKeys.STATIONARY_PHASE_COMBO_BOX_CAPTION) + ": "
				+ job.getSettings().getStationaryPhase());

		parameters.add(i18n.get(UIMessageKeys.PROCESSING_FORM_MASS_SCREENING_SCORE) + ": "
				+ job.getSettings().getScoreSettings().getMassScreeningState().getScoreWeight());
		
		//TODO I18N
		parameters.add(i18n.get("RTI Weigth") + ": "
				+ job.getSettings().getScoreSettings().getRtiScreeningState().getScoreWeight());

		parameters.add(i18n.get(UIMessageKeys.PROCESSING_FORM_MSMS_SCORE_WEIGHT) + ": "
				+ job.getSettings().getScoreSettings().getMsmsState().getScoreWeight());

		parameters.add(i18n.get(UIMessageKeys.PROCESSING_FORM_MASSBANK_SIMPLE_SCORE_WEIGHT) + ": "
				+ job.getSettings().getScoreSettings().getMassBankSimpleState().getScoreWeight());

		return parameters;
	}

	private String getSearchCaption(ProcessingResultContainer container) {
		return container.getSearchParameter().getFeatureSet().getName();
	}

	protected String getDisplayedName(Entry entry) {

		String name = entry.getName().getValue();

		return name != null ? name
				: i18n.get(UIMessageKeys.SEARCH_HISTORY_TREE_ITEM_UNDECLARED_ENTRY);
	}

	@Override
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.SOURCE_HANDLER_CLEARED_PROCESSING_SEARCH_HISTORY)
	protected void handleClearHistory(DummyPayload payload) {
		LOG.debug("entering event bus listener handleClearHistory with payload {} in topic {}",
				payload, EventBusTopics.SOURCE_HANDLER_CLEARED_PROCESSING_SEARCH_HISTORY);

		handleClear();
	}

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.SOURCE_HANDLER_DELETED_FILES_PROCESSING)
	protected void handleDeletedFiles(DummyPayload payload) {
		LOG.debug("entering event bus listener handleDeletedFiles with payload {} in topic {}",
				payload, EventBusTopics.SOURCE_HANDLER_DELETED_FILES_PROCESSING);

		handleClear();
	}
}
