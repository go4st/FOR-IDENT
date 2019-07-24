package de.hswt.fi.ui.vaadin.handler;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import de.hswt.fi.ui.vaadin.CustomNotification;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.container.ResultContainer;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.i18n.I18N;

import java.util.List;
import java.util.Set;

public abstract class AbstractSearchHandler<SEARCHPARAMETER, RESULT, ENTRY, CONTAINER extends ResultContainer<SEARCHPARAMETER, RESULT, ENTRY>>
		extends AbstractHandler<ViewEventBus> {

	private static final long serialVersionUID = 1850511335809022574L;

	private static final Logger LOG = LoggerFactory.getLogger(AbstractSearchHandler.class);

	private final I18N i18n;

    private Class<CONTAINER> containerClass;

	public AbstractSearchHandler(I18N i18n, Class<CONTAINER> containerClass) {
		this.i18n = i18n;
	    this.containerClass = containerClass;
	}

	protected CONTAINER getExistingContainer(SEARCHPARAMETER searchParameter, Set<CONTAINER> searchHistoryContainer) {
		LOG.debug("entering method search with searchParameter: {}", searchParameter);
		if (searchParameter == null) {
			LOG.debug("searchParameter is null - leaving method");
			return null;
		}

		for (CONTAINER container : searchHistoryContainer) {
			if (searchParameter.equals(container.getSearchParameter())) {
				LOG.debug("searchParameter are equal {} : {}", searchParameter,
						container.getSearchParameter());
				return container;
			}
		}
		return null;
	}

	protected CONTAINER createContainer(SEARCHPARAMETER searchParameter, RESULT result, List<ENTRY> entries) {

		CONTAINER searchContainer = getContainerInstance();

		if (searchContainer != null) {
			searchContainer.setSearchParameter(searchParameter);
			searchContainer.setResultsContainer(entries);
			searchContainer.setResult(result);

			if (!entries.isEmpty()) {
				searchContainer.setCurrentSelection(entries.iterator().next());
			}
		}

		return searchContainer;
	}

	private CONTAINER getContainerInstance() {
		CONTAINER container = null;
		try {
			container = this.containerClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			LOG.error(e.getMessage());
		}
		return container;
	}

	protected void fireUpdateEvents(CONTAINER container) {
		LOG.debug("publish event in fireUpdateEvents with palyoad {} and topic {}", container,
				EventBusTopics.SOURCE_HANDLER_CONTAINER_CHANGED);
		eventBus.publish(EventBusTopics.SOURCE_HANDLER_CONTAINER_CHANGED, this, container);

		LOG.debug("publish event in fireUpdateEvents with palyoad {} and topic {}", container,
				EventBusTopics.SOURCE_HANDLER_SEARCH_SELECTED);
		eventBus.publish(EventBusTopics.SOURCE_HANDLER_SEARCH_SELECTED, this,
				container.getSearchParameter());

		if (container.getCurrentSelection() != null) {
			LOG.debug("publish event fireUpdateEvents with payload {} and topic {}", container,
					EventBusTopics.SOURCE_HANDLER_ENTRY_SELECTED);
			eventBus.publish(EventBusTopics.SOURCE_HANDLER_ENTRY_SELECTED, this,
					container.getCurrentSelection());
		}
	}

	protected void showErrorNotification() {
		new CustomNotification.Builder(i18n.get(UIMessageKeys.SEARCH_HANDLER_ERROR_NO_DATASOURCE_SELECTED_CAPTION),
                i18n.get(UIMessageKeys.SEARCH_HANDLER_ERROR_NO_DATASOURCE_SELECTED_CAPTION),
				Notification.Type.HUMANIZED_MESSAGE).position(Position.MIDDLE_CENTER).build().show(Page.getCurrent());
	}
}
