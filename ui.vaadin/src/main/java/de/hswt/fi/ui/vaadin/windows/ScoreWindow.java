package de.hswt.fi.ui.vaadin.windows;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import de.hswt.fi.processing.service.model.ProcessingSettings;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import de.hswt.fi.ui.vaadin.fields.ProcessingUnitStateFieldEditable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;

@SpringComponent
@PrototypeScope
public class ScoreWindow extends AbstractWindow {

    private static final long serialVersionUID = 8450943763099692018L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreWindow.class);

    private final ProcessingUnitStateFieldEditable massScreeningStateField;

    private final ProcessingUnitStateFieldEditable rtiScreeningStateField;

    private final ProcessingUnitStateFieldEditable msmsField;

//    private final ProcessingUnitStateFieldEditable tpField;

    private final ProcessingUnitStateFieldEditable massBankStateField;

    private ProcessingSettings settings;

    private Binder<ProcessingSettings> binder;

    private CssLayout scoringContainerLayout;

    private Label sumLabel;

    @Autowired
    protected ScoreWindow(ComponentFactory componentFactory, I18N i18N, ViewEventBus viewEventBus,
                          ProcessingUnitStateFieldEditable massScreeningStateField,
                          ProcessingUnitStateFieldEditable rtiScreeningStateField,
                          ProcessingUnitStateFieldEditable msmsField,
                          ProcessingUnitStateFieldEditable tpField,
                          ProcessingUnitStateFieldEditable massBankStateField) {
        super(componentFactory, i18N, false);
        setWidth(LayoutConstants.REALLY_HUGE);

        viewEventBus.subscribe(this);
        this.massScreeningStateField = massScreeningStateField;
        this.rtiScreeningStateField = rtiScreeningStateField;
        this.msmsField = msmsField;
//        this.tpField = tpField;
        this.massBankStateField = massBankStateField;
    }

    @Override
    protected String getWindowCaption() {
        return i18n.get(UIMessageKeys.SCORE_WINDOW_CAPTION);
    }

    @Override
    protected Component getContentComponent() {

        CssLayout contentLayout = new CssLayout();
        contentLayout.setSizeFull();
        contentLayout.addStyleName(CustomValoTheme.PADDING_HALF);

        Label label = new Label(i18n.get(UIMessageKeys.SCORE_WINDOW_DESCRIPTION));
        label.addStyleName(CustomValoTheme.MARGIN_BOTTOM);
        contentLayout.addComponent(label);

        contentLayout.addComponent(createScoreLayout());
        return contentLayout;
    }

    private CssLayout createScoreLayout() {

        CssLayout containerLayout = new CssLayout();
        containerLayout.addStyleName(CustomValoTheme.MARGIN_BOTTOM);
        containerLayout.addStyleName(CustomValoTheme.PADDING_HALF);

        CssLayout headerLayout = new CssLayout();
        headerLayout.setWidth("100%");
        headerLayout.addStyleName(CustomValoTheme.BORDER_BOTTOM_COLOR_BLACK);
        headerLayout.addStyleName(CustomValoTheme.MARGIN_BOTTOM);
        containerLayout.addComponent(headerLayout);

        scoringContainerLayout = new CssLayout();
        containerLayout.addComponent(scoringContainerLayout);

        // Header
        createHeader(headerLayout);

        // State Fields
        createStateFields();

        // Summation Row
        createFooterLayout(containerLayout);

        // Bind Fields
        binder = new Binder<>();
        binder.forField(massScreeningStateField)
                .bind(processingSettings -> processingSettings.getScoreSettings().getMassScreeningState(),
                        (processingSettings, processingUnitState) -> processingSettings.getScoreSettings().setMassScreeningState(processingUnitState));
        binder.forField(rtiScreeningStateField)
                .bind(processingSettings -> processingSettings.getScoreSettings().getRtiScreeningState(),
                        (processingSettings, processingUnitState) -> processingSettings.getScoreSettings().setRtiScreeningState(processingUnitState));
        binder.forField(msmsField)
                .bind(processingSettings -> processingSettings.getScoreSettings().getMsmsState(),
                        (processingSettings, processingUnitState) -> processingSettings.getScoreSettings().setMsmsState(processingUnitState));
//        binder.forField(tpField)
//                .bind(processingSettings -> processingSettings.getScoreSettings().getTpState(),
//                        (processingSettings, processingUnitState) -> processingSettings.getScoreSettings().setTpState(processingUnitState));
        binder.forField(massBankStateField)
                .bind(processingSettings -> processingSettings.getScoreSettings().getMassBankSimpleState(),
                        (processingSettings, processingUnitState) -> processingSettings.getScoreSettings().setMassBankSimpleState(processingUnitState));

        return containerLayout;
    }

    private void createFooterLayout(CssLayout containerLayout) {
        CssLayout bottomLayout = new CssLayout();
        bottomLayout.setSizeFull();
        CssLayout descriptionLayout = new CssLayout();
        descriptionLayout.setWidth("30%");
        CssLayout sumLayout = new CssLayout();
        sumLayout.setWidth("70%");

        sumLabel = new Label();
        sumLabel.addStyleName(CustomValoTheme.FLOAT_RIGHT);
        sumLabel.addStyleName(CustomValoTheme.LABEL_VERY_BOLD);
        sumLayout.addComponent(sumLabel);

        Label label = new Label(i18n.get(UIMessageKeys.PROCESSING_FORM_DATA_AVAILABLE_DESCRIPTION));
        label.setWidthUndefined();
        label.addStyleName(CustomValoTheme.LABEL_SMALL);
        descriptionLayout.addComponent(label);

        label = new Label("", ContentMode.HTML);
        label.setValue("&nbsp;or&nbsp;" + VaadinIcons.BAN.getHtml());
        label.setWidthUndefined();
        label.addStyleName(CustomValoTheme.ICON_COLOR_RED);
        label.addStyleName(CustomValoTheme.FLOAT_RIGHT);
        label.addStyleName(CustomValoTheme.LABEL_SMALL);
        descriptionLayout.addComponent(label);

        label = new Label("", ContentMode.HTML);
        label.setValue(VaadinIcons.CHECK.getHtml());
        label.setWidthUndefined();
        label.addStyleName(CustomValoTheme.ICON_COLOR_GREEN);
        label.addStyleName(CustomValoTheme.FLOAT_RIGHT);
        label.addStyleName(CustomValoTheme.LABEL_SMALL);
        descriptionLayout.addComponent(label);

        bottomLayout.addComponent(descriptionLayout);
        bottomLayout.addComponent(sumLayout);
        containerLayout.addComponent(bottomLayout);
    }

    private void createStateFields() {
        massScreeningStateField
                .setCaptionLabel(i18n.get(UIMessageKeys.PROCESSING_FORM_MASS_SCREENING_SCORE));
        massScreeningStateField.addStyleName(CustomValoTheme.MARGIN_HALF_BOTTOM);
        scoringContainerLayout.addComponent(massScreeningStateField);


        rtiScreeningStateField
                .setCaptionLabel("RTI / Screening");
        rtiScreeningStateField.addStyleName(CustomValoTheme.MARGIN_HALF_BOTTOM);
        scoringContainerLayout.addComponent(rtiScreeningStateField);

        msmsField.setCaptionLabel(i18n.get(UIMessageKeys.PROCESSING_FORM_MSMS_SCORE_WEIGHT));
        msmsField.addStyleName(CustomValoTheme.MARGIN_HALF_BOTTOM);
        scoringContainerLayout.addComponent(msmsField);


//        tpField.setCaptionLabel(i18n.get(UIMessageKeys.PROCESSING_FORM_TP_SCORE));
//        tpField.addStyleName(CustomValoTheme.MARGIN_HALF_BOTTOM);

        //TODO Change if TP score is avialable
//        scoringContainerLayout.addComponent(tpField);
//        tpField.setScorable(false);

        massBankStateField.setCaptionLabel(
                i18n.get(UIMessageKeys.PROCESSING_FORM_MASSBANK_SIMPLE_SCORE_WEIGHT));
        massBankStateField.addStyleName(CustomValoTheme.MARGIN_HALF_BOTTOM);
        scoringContainerLayout.addComponent(massBankStateField);
        scoringContainerLayout.setWidth("100%");
        scoringContainerLayout.addStyleName(CustomValoTheme.BORDER_BOTTOM_COLOR_BLACK);
    }

    private void createHeader(CssLayout headerLayout) {
        Label processLabel = new Label(i18n.get(UIMessageKeys.SCORE_WINDOW_COLUMN_PROCESS));
        processLabel.setWidth("30%");
        processLabel.addStyleName(CustomValoTheme.LABEL_VERY_BOLD);
        headerLayout.addComponent(processLabel);

        Label availableLabel = new Label(i18n.get(UIMessageKeys.SCORE_WINDOW_COLUMN_DATA_AVAILABLE));
        availableLabel.addStyleName(CustomValoTheme.LABEL_VERY_BOLD);
        availableLabel.addStyleName(CustomValoTheme.FLOAT_CENTER);
        availableLabel.setWidth("21%");
        headerLayout.addComponent(availableLabel);

        Label enabledLabel = new Label(i18n.get(UIMessageKeys.SCORE_WINDOW_COLUMN_ACTIVATED));
        enabledLabel.addStyleName(CustomValoTheme.LABEL_VERY_BOLD);
        enabledLabel.addStyleName(CustomValoTheme.FLOAT_CENTER);
        enabledLabel.setWidth("21%");
        headerLayout.addComponent(enabledLabel);

        Label scoreLabel = new Label(i18n.get(UIMessageKeys.SCORE_WINDOW_COLUMN_WEIGHT));
        scoreLabel.setWidth("28%");
        scoreLabel.addStyleName(CustomValoTheme.LABEL_VERY_BOLD);
        headerLayout.addComponent(scoreLabel);
    }

    public void setProcessingJob(ProcessingSettings settings) {
        this.settings = settings;
        binder.readBean(settings);
        massScreeningStateField.canBeUnchecked(false);

        initScoreWeights();
    }

    private void initScoreWeights() {
        massScreeningStateField.setWeight(0);
        rtiScreeningStateField.setWeight(0);
        msmsField.setWeight(0);
//        tpField.setWeight(0);
        massBankStateField.setWeight(0);
    }

    @SuppressWarnings("unused")
    @EventBusListenerMethod
    @EventBusListenerTopic(topic = EventBusTopics.SCORE_SETTINGS_WEIGHT_CHANGED)
    private void weightChanged(ProcessingUnitStateFieldEditable field) {
        LOGGER.debug("entering weightChanged with topic {}",
                EventBusTopics.SCORE_SETTINGS_WEIGHT_CHANGED);

        double combinedScore = 0;

        // Sum scores
        for (Component next : scoringContainerLayout) {
            if (ProcessingUnitStateFieldEditable.class.isInstance(next) && !next.equals(field)) {
                ProcessingUnitStateFieldEditable stateField = (ProcessingUnitStateFieldEditable) next;
                if (stateField.isScoreEnabled() && stateField.isScorable()) {
                    combinedScore += stateField.getWeight();
                }
            }
        }

        double roundCombinedScore = roundFollowingDigits(combinedScore);
        double roundCurrentWeight = roundFollowingDigits(field.getWeight());

        if ((roundCombinedScore + roundCurrentWeight) > 1) {
            double score = roundFollowingDigits(1 - roundCombinedScore);
            if (score >= 0 && score < 1) {
                field.setWeight(score);
            }
        }

        double sum = combinedScore + field.getWeight();

        sumLabel.setValue(i18n.get(UIMessageKeys.SCORE_WINDOW_SUM) + " " + (int) (sum * 100) + "%");
        if (sum >= 0.995) {
            sum = Math.ceil(sum);
            sumLabel.setValue(i18n.get(UIMessageKeys.SCORE_WINDOW_SUM) + " " + (int) (sum * 100) + "%");
        }
        if (sum != 1.0) {
            sumLabel.addStyleName(CustomValoTheme.COLOR_RED);
            setCanFinish(false);
        } else {
            sumLabel.removeStyleName(CustomValoTheme.COLOR_RED);
            setCanFinish(true);
        }
    }

    private double roundFollowingDigits(double value) {
        double potency = Math.pow(10, 2);
        return Math.round(value * potency) / potency;
    }

    @Override
    protected void handleOk() {
        try {
            binder.writeBean(settings);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public ProcessingSettings getSettings() {
        return settings;
    }
}