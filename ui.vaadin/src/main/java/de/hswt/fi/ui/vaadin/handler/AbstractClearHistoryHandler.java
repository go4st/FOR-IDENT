package de.hswt.fi.ui.vaadin.handler;

import de.hswt.fi.ui.vaadin.container.ResultContainer;
import org.vaadin.spring.events.EventBus.ViewEventBus;

import java.util.Set;

public abstract class AbstractClearHistoryHandler<SEARCHPARAMETER, RESULT, ENTRY, CONTAINER extends ResultContainer<SEARCHPARAMETER, RESULT, ENTRY>>
		extends AbstractHandler<ViewEventBus> {

	private static final long serialVersionUID = 4117330449173803861L;

	protected void clear(Set<CONTAINER> historyContainer) {
		if (historyContainer == null) {
			return;
		}
		historyContainer.forEach(ResultContainer::clear);
	}
}
