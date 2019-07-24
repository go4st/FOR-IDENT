package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.search.service.mass.search.model.SearchParameter;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.i18n.I18N;

@SpringComponent
@ViewScope
public class QuickSearchFormComponent extends AbstractSearchFormComponent {

	private static final long serialVersionUID = 4361616224870529734L;

	@Autowired
    public QuickSearchFormComponent(ViewEventBus eventBus, ComponentFactory componentFactory, I18N i18n) {
        super(eventBus, componentFactory, i18n);
	}

    @Override
    protected void createFields() {
        initHeader();
        initIonisationRow();
        initMassRow();
        initLogXRow();
        initHalogenRow();
    }

    protected void bindFields() {
		binder.forField(ionisationCombobox)
				.bind(SearchParameter::getIonisation, SearchParameter::setIonisation);
		binder.forField(halogenOptionGroup)
				.bind(SearchParameter::getHalogens, SearchParameter::setHalogens);

		//TODO Add Mass and LogD bindings
		binder.setBean(new SearchParameter());
	}

    protected boolean isInvalidSearchParam() {
        return binder.getBean().getAccurateMass() == null && binder.getBean().getLogP() == null;
	}

	@Override
	public String getTitle() {
		return i18n.get(UIMessageKeys.QUICK_SEARCH_VIEW_SEARCH_TITLE);
	}
}
