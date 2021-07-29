package de.hswt.fi.ui.vaadin.views.components.processing;

import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import de.hswt.fi.processing.service.model.ScoreSettings;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.fields.ProcessingUnitStateField;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.i18n.I18N;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by August Gilg on 16.11.2016.
 */

@SpringComponent
@PrototypeScope
public class ScoreSettingsComponent extends CustomField<ScoreSettings> {

	private final I18N i18n;
	private CssLayout layout;
	private ProcessingUnitStateField massScreeningStateField;
	private ProcessingUnitStateField rtiScreeningStateField;
	private ProcessingUnitStateField msmsStateField;
//	private ProcessingUnitStateField tpStateField;
	private ProcessingUnitStateField massBankStateField;
	private List<ProcessingUnitStateField> stateFields;
	private Binder<ScoreSettings> binder;

	public ScoreSettingsComponent(I18N i18n) {
		this.i18n = i18n;
	}

	@Override
	protected Component initContent() {
		initializeLayout();
		initializeFieldGroup();
		return layout;
	}

	private void initializeLayout() {

		layout = new CssLayout();
		layout.setSizeFull();
		layout.addStyleName(CustomValoTheme.MARGIN_TOP);

		CssLayout containerLayout = new CssLayout();
		containerLayout.addStyleName(CustomValoTheme.MARGIN_BOTTOM);
		containerLayout.addStyleName(CustomValoTheme.BORDER_COLOR_ALT1);

		CssLayout scoringContainerLayout = new CssLayout();
		scoringContainerLayout.addStyleName(CustomValoTheme.PADDING_HALF);
		containerLayout.addComponent(scoringContainerLayout);

		addProcessingStateFields(scoringContainerLayout);
		addProcessingStateFieldDescription(scoringContainerLayout);

		layout.addComponent(containerLayout);
	}

	private void initializeFieldGroup() {
		binder = new Binder<>();
		binder.bind(massScreeningStateField, ScoreSettings::getMassScreeningState, ScoreSettings::setMassScreeningState);
		binder.bind(rtiScreeningStateField, ScoreSettings::getRtiScreeningState, ScoreSettings::setRtiScreeningState);
		binder.bind(msmsStateField, ScoreSettings::getMsmsState, ScoreSettings::setMsmsState);
//		binder.bind(tpStateField, ScoreSettings::getTpState, ScoreSettings::setTpState);
		binder.bind(massBankStateField, ScoreSettings::getMassBankSimpleState, ScoreSettings::setMassBankSimpleState);
	}

	private void addProcessingStateFields(CssLayout scoringContainerLayout) {
		stateFields = new ArrayList<>();

		massScreeningStateField = new ProcessingUnitStateField(
				i18n.get(UIMessageKeys.PROCESSING_FORM_MASS_SCREENING_SCORE));
		scoringContainerLayout.addComponent(massScreeningStateField);
		massScreeningStateField.canBeDisabled(false);
		stateFields.add(massScreeningStateField);

		rtiScreeningStateField = new ProcessingUnitStateField(i18n.get(UIMessageKeys.PROCESSING_FORM_RTI_SCREENING_SCORE));
		scoringContainerLayout.addComponent(rtiScreeningStateField);
		stateFields.add(rtiScreeningStateField);

		msmsStateField = new ProcessingUnitStateField(
				i18n.get(UIMessageKeys.PROCESSING_FORM_MSMS_SCORE_WEIGHT));
		scoringContainerLayout.addComponent(msmsStateField);
		stateFields.add(msmsStateField);

//		tpStateField = new ProcessingUnitStateField(i18n.get(UIMessageKeys.
//				PROCESSING_FORM_TP_SCORE));
//		scoringContainerLayout.addComponent(tpStateField);
//		stateFields.add(tpStateField);

		massBankStateField = new ProcessingUnitStateField(
				i18n.get(UIMessageKeys.PROCESSING_FORM_MASSBANK_SIMPLE_SCORE_WEIGHT));
		massBankStateField.addStyleName(CustomValoTheme.PADDING_BOTTOM);
		scoringContainerLayout.addComponent(massBankStateField);
		stateFields.add(massBankStateField);
	}

	private void addProcessingStateFieldDescription(CssLayout scoringContainerLayout) {
		Label label = new Label(i18n.get(UIMessageKeys.PROCESSING_FORM_DATA_AVAILABLE_DESCRIPTION));
		label.setWidthUndefined();
		label.addStyleName(CustomValoTheme.LABEL_SMALL);
		scoringContainerLayout.addComponent(label);

		label = new Label("", ContentMode.HTML);
		label.setValue("&nbsp;or&nbsp;" + VaadinIcons.BAN.getHtml());
		label.setWidthUndefined();
		label.addStyleName(CustomValoTheme.ICON_COLOR_RED);
		label.addStyleName(CustomValoTheme.FLOAT_RIGHT);
		label.addStyleName(CustomValoTheme.LABEL_SMALL);
		scoringContainerLayout.addComponent(label);

		label = new Label("", ContentMode.HTML);
		label.setValue(VaadinIcons.CHECK.getHtml());
		label.setWidthUndefined();
		label.addStyleName(CustomValoTheme.ICON_COLOR_GREEN);
		label.addStyleName(CustomValoTheme.FLOAT_RIGHT);
		label.addStyleName(CustomValoTheme.LABEL_SMALL);
		scoringContainerLayout.addComponent(label);

		label = new Label(i18n.get(UIMessageKeys.PROCESSING_FORM_PROCESSING_ACTIVATED_DESCRIPTION));
		label.setWidthUndefined();
		label.addStyleName(CustomValoTheme.LABEL_SMALL);
		scoringContainerLayout.addComponent(label);

		label = new Label("", ContentMode.HTML);
		label.setValue("&nbsp;or&nbsp;" + VaadinIcons.CIRCLE_THIN.getHtml());
		label.setWidthUndefined();
		label.addStyleName(CustomValoTheme.ICON_COLOR_RED);
		label.addStyleName(CustomValoTheme.FLOAT_RIGHT);
		label.addStyleName(CustomValoTheme.LABEL_SMALL);
		scoringContainerLayout.addComponent(label);

		label = new Label("", ContentMode.HTML);
		label.setValue(VaadinIcons.CIRCLE.getHtml());
		label.setWidthUndefined();
		label.addStyleName(CustomValoTheme.ICON_COLOR_GREEN);
		label.addStyleName(CustomValoTheme.FLOAT_RIGHT);
		label.addStyleName(CustomValoTheme.LABEL_SMALL);
		scoringContainerLayout.addComponent(label);
	}

	private void toggleStateField() {
		calculateScores();
	}

	private Stream<ProcessingUnitStateField> getAvailableStateFields() {
		return stateFields.stream().filter(Objects::nonNull);
	}

	private void calculateScores() {

		getAvailableStateFields().forEach(field -> field.setScoreWeight(0.0));

		List<ProcessingUnitStateField> availableFields = getAvailableStateFields()
				.filter(field -> field.getValue().isEnabled())
				.filter(field -> field.getValue().isScoreable())
				.filter(field -> field.getValue().isDataAvailable())
				.collect(Collectors.toList());

		if (availableFields.isEmpty()) {
			return;
		}

		double scoreWeight = 1d / availableFields.size();

		availableFields.forEach(processingUnitState -> processingUnitState.setScoreWeight(scoreWeight));
	}

	@Override
	protected void doSetValue(ScoreSettings scoreSettings) {
		binder.setBean(scoreSettings);
		getAvailableStateFields().forEach(field -> field.setEnabledClickListener(layoutClickEvent -> toggleStateField()));
	}

	@Override
	public ScoreSettings getValue() {
		return binder.getBean();
	}
}