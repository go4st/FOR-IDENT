package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.application.properties.ApplicationProperties;
import de.hswt.fi.calculation.service.api.CalculationService;
import de.hswt.fi.processing.service.model.ProcessCandidate;
import de.hswt.fi.search.service.mass.search.model.Entry;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.components.DetailsSectionComponent;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.i18n.I18N;

import javax.inject.Provider;

@SpringComponent
@ViewScope
public class ProcessingDetailsComponent extends AbstractDetailsComponent<ProcessCandidate> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public ProcessingDetailsComponent(I18N i18n, ApplicationProperties applicationProperties,
									  ComponentFactory componentFactory,
									  Provider<DetailsSectionComponent> detailsSectionProvider,
									  CalculationService calculationService, EventBus.ViewEventBus eventBus) {
		super(i18n, applicationProperties, componentFactory, detailsSectionProvider, calculationService, eventBus);
	}

	@Override
	public String getTitle() {
		return i18n.get(UIMessageKeys.SEARCH_VIEW_DETAILS_TITLE);
	}

	@Override
	protected Entry getEntry() {
		return currentEntryContainer.getEntry();
	}

}