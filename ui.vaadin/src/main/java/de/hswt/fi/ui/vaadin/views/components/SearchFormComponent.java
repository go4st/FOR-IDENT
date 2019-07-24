package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.TextField;
import de.hswt.fi.search.service.mass.search.model.SearchParameter;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.configuration.SessionSharedObjects;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.i18n.I18N;

import java.util.Optional;

@SpringComponent
@ViewScope
public class SearchFormComponent extends AbstractSearchFormComponent {

    private static final long serialVersionUID = 1L;

    private final SessionSharedObjects sessionSharedObjects;

    private TextField publicIdTextField;

    private TextField inchiKeyTextField;

    private TextField casTextField;

    private TextField nameTextField;

    private TextField elementalFormulaTextField;

    private TextField iupacTextField;

    private TextField smilesTextField;

    @Autowired
    public SearchFormComponent(ViewEventBus eventBus, ComponentFactory componentFactory, I18N i18n,
                               SessionSharedObjects sessionSharedObjects) {
        super(eventBus, componentFactory, i18n);
        this.sessionSharedObjects = sessionSharedObjects;
    }

    @Override
    protected void createFields() {
        initHeader();
        initAdditionalFields();
        initIonisationRow();
        initMassRow();
        initLogXRow();
        initHalogenRow();
    }

    protected void bindFields() {
        binder.bind(publicIdTextField, SearchParameter::getPublicID, SearchParameter::setPublicID);
        binder.bind(inchiKeyTextField, SearchParameter::getInchiKey, SearchParameter::setInchiKey);
        binder.bind(casTextField, SearchParameter::getCas, SearchParameter::setCas);
        binder.bind(nameTextField, SearchParameter::getName, SearchParameter::setName);
        binder.bind(elementalFormulaTextField, SearchParameter::getElementalFormula, SearchParameter::setElementalFormula);
        binder.bind(iupacTextField, SearchParameter::getIupac, SearchParameter::setIupac);
        binder.bind(smilesTextField, SearchParameter::getSmiles, SearchParameter::setSmiles);
        binder.bind(ionisationCombobox, SearchParameter::getIonisation, SearchParameter::setIonisation);
        binder.bind(halogenOptionGroup, SearchParameter::getHalogens, SearchParameter::setHalogens);

        initFieldGroup();
    }

    private void initFieldGroup() {

        SearchParameter searchParameter = binder.getBean();

        boolean executeSearch = false;

        Optional<String> stoffidentId = sessionSharedObjects.getStoffidentId();
        Optional<String> inchiKey = sessionSharedObjects.getInchiKey();

        if (stoffidentId.isPresent()) {
            searchParameter.setPublicID(stoffidentId.get());
            sessionSharedObjects.setStoffidentId(null);
            executeSearch = true;
        } else if (inchiKey.isPresent()) {
            searchParameter.setInchiKey(inchiKey.get());
            sessionSharedObjects.setInchiKey(null);
            executeSearch = true;
        }

        if (executeSearch) {
            handleSearch();
        }
    }

    private void initAdditionalFields() {

        nameTextField = componentFactory.createTextField(i18n.get(UIMessageKeys.SEARCH_FORM_COMPONENT_NAME_CAPTION));
        addComponent(componentFactory.createRowLayout(nameTextField));

        casTextField = componentFactory.createTextField(i18n.get(UIMessageKeys.SEARCH_FORM_COMPONENT_CAS_CAPTION));
        addComponent(componentFactory.createRowLayout(casTextField));

        elementalFormulaTextField = componentFactory
                .createTextField(i18n.get(UIMessageKeys.SEARCH_FORM_COMPONENT_FORMULA_CAPTION));
        addComponent(componentFactory.createRowLayout(elementalFormulaTextField));

        inchiKeyTextField = componentFactory
                .createTextField(i18n.get(UIMessageKeys.SEARCH_FORM_COMPONENT_INCHI_KEY_CAPTION));
        addComponent(componentFactory.createRowLayout(inchiKeyTextField));

        iupacTextField = componentFactory.createTextField(i18n.get(UIMessageKeys.SEARCH_FORM_COMPONENT_IUPAC_CAPTION));
        addComponent(componentFactory.createRowLayout(iupacTextField));

        smilesTextField = componentFactory
                .createTextField(i18n.get(UIMessageKeys.SEARCH_FORM_COMPONENT_SMILES_CAPTION));
        addComponent(componentFactory.createRowLayout(smilesTextField));

        publicIdTextField = componentFactory
                .createTextField(i18n.get(UIMessageKeys.SEARCH_FORM_COMPONENT_PUBLIC_ID_CAPTION));
        addComponent(componentFactory.createRowLayout(publicIdTextField));
    }

    protected boolean isInvalidSearchParam() {

        SearchParameter searchParameter = binder.getBean();

        return searchParameter.getAccurateMass() == null
                && searchParameter.getAccurateMassRangeMin() == Double.MIN_VALUE
                && searchParameter.getAccurateMassRangeMax() == Double.MAX_VALUE
                && searchParameter.getLogP() == null
                && searchParameter.getLogPRangeMin() == Double.MIN_VALUE
                && searchParameter.getLogPRangeMax() == Double.MAX_VALUE
                && (searchParameter.getName() == null || searchParameter.getName().isEmpty())
                && (searchParameter.getCas() == null || searchParameter.getCas().isEmpty())
                && (searchParameter.getElementalFormula() == null || searchParameter.getElementalFormula().isEmpty())
                && (searchParameter.getIupac() == null || searchParameter.getIupac().isEmpty())
                && (searchParameter.getSmiles() == null || searchParameter.getSmiles().isEmpty())
                && (searchParameter.getInchiKey() == null || searchParameter.getInchiKey().isEmpty())
                && (searchParameter.getPublicID() == null || searchParameter.getPublicID().isEmpty());
    }

    @Override
    public String getTitle() {
        return i18n.get(UIMessageKeys.SEARCH_VIEW_SEARCH_TITLE);
    }
}
