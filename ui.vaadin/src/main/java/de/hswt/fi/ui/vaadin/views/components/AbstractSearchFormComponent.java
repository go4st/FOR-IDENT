package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.data.Binder;
import com.vaadin.data.provider.Query;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.hswt.fi.common.Ionisation;
import de.hswt.fi.search.service.mass.search.model.SearchParameter;
import de.hswt.fi.ui.vaadin.CustomNotification;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.components.ContainerContentComponent;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import de.hswt.fi.ui.vaadin.utils.TextFieldUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public abstract class AbstractSearchFormComponent extends ContainerContentComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSearchFormComponent.class);

    protected final EventBus.ViewEventBus eventBus;

    protected final ComponentFactory componentFactory;

    protected final I18N i18n;

    protected ComboBox<Ionisation> ionisationCombobox;

    protected CheckBoxGroup<String> halogenOptionGroup;

    protected Binder<SearchParameter> binder;

    private CssLayout headerLayout;

    private TextField massTextField;

    private ComboBox<Double> ppmCombobox;

    private TextField logPTextField;

    private ComboBox<Double> logPRangeCombobox;

    protected abstract void createFields();

    protected abstract void bindFields();

    protected abstract boolean isInvalidSearchParam();

    public AbstractSearchFormComponent(EventBus.ViewEventBus eventBus, ComponentFactory componentFactory, I18N i18n) {
        this.eventBus = eventBus;
        this.componentFactory = componentFactory;
        this.i18n = i18n;
    }

    @PostConstruct
    private void postConstruct() {
        setSizeFull();

        addStyleName(CustomValoTheme.PADDING_HALF);
        addStyleName(CustomValoTheme.CSS_LAYOUT_SCROLLBAR);

        binder = new Binder<>();
        binder.setBean(new SearchParameter());

        createFields();
        bindFields();

        eventBus.subscribe(this);
    }

    @PreDestroy
    private void preDestroy() {
        eventBus.unsubscribe(this);
    }

    protected void initHeader() {
        headerLayout = new CssLayout();

        Button searchButton = componentFactory.createButton(VaadinIcons.SEARCH,
                i18n.get(UIMessageKeys.SEARCH_BUTTON_CAPTION));
        searchButton.addClickListener(e -> handleSearch());
        searchButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        headerLayout.addComponent(searchButton);

        Button clearButton = componentFactory.createButton(VaadinIcons.ERASER,
                i18n.get(UIMessageKeys.CLEAR_FIELDS_BUTTON_CAPTION), false);
        clearButton.addClickListener(c -> clear());
        headerLayout.addComponent(clearButton);
    }

    protected void initIonisationRow() {
        ionisationCombobox = componentFactory.createIonisationComboBox();
        addComponent(componentFactory.createRowLayout(ionisationCombobox));
    }

    protected void initMassRow() {
        massTextField = componentFactory.createMassTextField();
        ppmCombobox = componentFactory.createPpmComboBox();
        ppmCombobox.setWidth(LayoutConstants.RANGE_FIELD_WITDH);
        addComponent(createNumberWithRangeRow(massTextField, ppmCombobox));
    }

    protected void initLogXRow() {
        logPTextField = componentFactory.createLogPTextField();
        logPRangeCombobox = componentFactory.createLogXRangeComboBox();
        logPRangeCombobox.setWidth(LayoutConstants.RANGE_FIELD_WITDH);
        addComponent(createNumberWithRangeRow(logPTextField, logPRangeCombobox));
    }

    private Component createNumberWithRangeRow(TextField numberTextfield, ComboBox<?> rangeComboBox) {
        Label betweenLabel = new Label(LayoutConstants.UNICODE_PLUS_MINUS_SIGN);
        // needed to get row layout working
        betweenLabel.setCaption("");
        return componentFactory.createRowLayout(numberTextfield, betweenLabel, rangeComboBox);
    }

    protected void initHalogenRow() {
        halogenOptionGroup = componentFactory.createHalogenOptionGroup();
        halogenOptionGroup.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        halogenOptionGroup.addStyleName(CustomValoTheme.MARGIN_HALF_HORIZONTAL);
        addComponent(componentFactory.createHalogenRow(halogenOptionGroup));
    }

    private boolean parseUnmanagedFields() {
        if (!parseMassTextField()) {
            showErrorNotification(massTextField.getValue(), massTextField.getCaption());
            return false;
        }
        if (!parseLogPTextField()) {
            showErrorNotification(logPTextField.getValue(), logPTextField.getCaption());
            return false;
        }
        return true;
    }

    private boolean parseMassTextField() {
        binder.getBean().setAccurateMass(null);
        binder.getBean().setAccurateMassRangeMin(Double.MIN_VALUE);
        binder.getBean().setAccurateMassRangeMax(Double.MAX_VALUE);
        binder.getBean().setPpm(0.0);
        if (massTextField.getValue().isEmpty()) {
            return true;
        }
        return TextFieldUtils.parseMassSearchValue(binder.getBean(),
                massTextField, ppmCombobox);
    }

    private boolean parseLogPTextField() {
        binder.getBean().setLogP(null);
        binder.getBean().setLogPRangeMin(Double.MIN_VALUE);
        binder.getBean().setLogPRangeMax(Double.MAX_VALUE);
        binder.getBean().setLogPDelta(0.0);
        if (logPTextField.getValue().isEmpty()) {
            return true;
        }
        return TextFieldUtils.parseLogSearchValue(binder.getBean(),
                logPTextField, logPRangeCombobox);
    }

    protected void handleSearch() {
        LOGGER.debug("entering method handleSearch");

        boolean areUnmanagedFieldsValid = parseUnmanagedFields();

        if (!areUnmanagedFieldsValid) {
            LOGGER.debug("Fields not managed by FieldGroup are invalid - returning");
            return;
        }
        if (isInvalidSearchParam()) {
            LOGGER.error("Search Param with values {} is invalid - returning", binder.getBean());
            buildInvalidParameterNotification();
            return;
        }

        LOGGER.debug("publish event inside handleSearch");
        eventBus.publish(this, copySearchParameter(binder.getBean()));
    }

    private void buildInvalidParameterNotification() {
        new CustomNotification.Builder(i18n.get(UIMessageKeys.INVALID_SEARCH_NOTIFICATION_TITLE),
                i18n.get(UIMessageKeys.INVALID_SEARCH_NOTIFICATION_DESCRIPTION),
                Notification.Type.WARNING_MESSAGE).position(Position.MIDDLE_CENTER).build()
                .show(Page.getCurrent());
    }

    private void clear() {
        binder.setBean(new SearchParameter());
        ppmCombobox.setValue(getFirstComboBoxValue(ppmCombobox));
        logPRangeCombobox.setValue(getFirstComboBoxValue(logPRangeCombobox));
        massTextField.clear();
        logPTextField.clear();
    }

    protected void setFormFields(SearchParameter searchParameter) {
        clear();
        binder.setBean(copySearchParameter(searchParameter));
        setUnboundMassFields(searchParameter);
        setUnboundLogPFields(searchParameter);
    }

    private void setUnboundMassFields(SearchParameter searchParameter) {
        if (isValidRange(searchParameter.getAccurateMassRangeMin(), searchParameter.getAccurateMassRangeMax())) {
            massTextField.setValue(searchParameter.getAccurateMassRangeMin() + " - "
                    + searchParameter.getAccurateMassRangeMax());
        } else {
            massTextField.setValue(getValueOf(searchParameter.getAccurateMass()));
            ppmCombobox.setSelectedItem(getValueOf(searchParameter.getPpm(), ppmCombobox));
        }
    }

    private void setUnboundLogPFields(SearchParameter searchParameter) {
        if (isValidRange(searchParameter.getLogPRangeMin(), searchParameter.getLogPRangeMax())) {
            logPTextField.setValue(searchParameter.getLogPRangeMin() + " - " + searchParameter.getLogPRangeMax());
        } else {
            logPTextField.setValue(getValueOf(searchParameter.getLogP()));
            logPRangeCombobox.setValue(getValueOf(searchParameter.getLogPDelta(), logPRangeCombobox));
        }
    }

    private boolean isValidRange(Double rangeMin, Double rangeMax) {
        return Double.compare(rangeMin, Double.MIN_VALUE) != 0 || Double.compare(rangeMax, Double.MAX_VALUE) != 0;
    }

    private String getValueOf(Double doubleValue) {
        return doubleValue != null ? doubleValue.toString() : "";
    }

    private Double getValueOf(Double delta, ComboBox<Double> rangeCombobox) {
        return delta != null && delta != 0 ? delta : getFirstComboBoxValue(rangeCombobox);
    }

    private Double getFirstComboBoxValue(ComboBox<Double> comboBox) {
        return comboBox.getDataProvider().fetch(new Query<>()).findFirst().orElse(null);
    }

    @SuppressWarnings("unused")
    @EventBusListenerMethod
    @EventBusListenerTopic(topic = EventBusTopics.SOURCE_HANDLER_SEARCH_SELECTED)
    private void handleSearchParameterChange(SearchParameter searchParameter) {
        LOGGER.debug("entering event bus listener fillSearchForm with payload {} in topic {}", searchParameter,
                EventBusTopics.SOURCE_HANDLER_SEARCH_SELECTED);
        setFormFields(searchParameter);
    }

    private SearchParameter copySearchParameter(SearchParameter searchParameter) {
        SearchParameter copyOfSearchParameter = SerializationUtils.clone(searchParameter);
        LOGGER.info("Search parameter: {}", copyOfSearchParameter);
        return copyOfSearchParameter;
    }

    @Override
    public Component getHeaderComponent() {
        return headerLayout;
    }

    private void showErrorNotification(String value, String caption) {
        new CustomNotification.Builder(
                i18n.get(UIMessageKeys.SEARCH_FORM_INVALID_FIELD_INPUT_CAPTION),
                i18n.get(UIMessageKeys.SEARCH_FORM_INVALID_FIELD_INPUT_MESSAGE, value, caption),
                Notification.Type.ERROR_MESSAGE).build().show(Page.getCurrent());
    }
}
