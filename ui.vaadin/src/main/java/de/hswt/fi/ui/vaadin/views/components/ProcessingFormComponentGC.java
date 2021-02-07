package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import de.hswt.fi.common.Ionisation;
import de.hswt.fi.common.spring.Profiles;
import de.hswt.fi.processing.service.model.ProcessingJob;
import de.hswt.fi.processing.service.model.ProcessingSettings;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.components.CollapsibleLayout;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.i18n.I18N;

@Profile({Profiles.GC, Profiles.DEVELOPMENT_GC})
@SpringComponent
@ViewScope
public class ProcessingFormComponentGC extends AbstractProcessingFormComponent {

	private static final long serialVersionUID = 1L;

	@Autowired
	public ProcessingFormComponentGC(ViewEventBus viewEventBus, ComponentFactory componentFactory, I18N i18n) {
		super(viewEventBus, componentFactory, i18n);
	}

	@Override
	protected Component getParameterRow() {

		CssLayout container = new CssLayout();
		container.addStyleName(CustomValoTheme.MARGIN_TOP);
		container.addStyleName(CustomValoTheme.MARGIN_BOTTOM);

		CssLayout fieldsLayout = new CssLayout();
		fieldsLayout.addStyleName(CustomValoTheme.MARGIN_HALF_TOP);

		ComboBox<Double> ppmComboBox = componentFactory.createPpmComboBox();
		ppmComboBox.setCaption(i18n.get(UIMessageKeys.PROCESSING_FORM_PRECURSOR_PPM));
		ppmComboBox.addStyleName(CustomValoTheme.MARGIN_HALF_BOTTOM);
		fieldsLayout.addComponent(ppmComboBox);

		ComboBox<Double> ppmFragmentsComboBox = componentFactory.createPpmComboBox();
		ppmFragmentsComboBox.setCaption(i18n.get(UIMessageKeys.PROCESSING_FORM_FRAGMENTS_PPM));
		ppmFragmentsComboBox.addStyleName(CustomValoTheme.MARGIN_HALF_BOTTOM);
		fieldsLayout.addComponent(ppmFragmentsComboBox);

		TextField intensityTresholdField = componentFactory.createTextField("Intensity threshold");
		fieldsLayout.addComponent(intensityTresholdField);

		ComboBox<Ionisation> ionisationComboBox = componentFactory.createIonisationComboBox();
		ionisationComboBox.addStyleName(CustomValoTheme.MARGIN_HALF_BOTTOM);
		fieldsLayout.addComponent(ionisationComboBox);

		container.addComponent(fieldsLayout);

		Button startProcessing = componentFactory.createButton(i18n.get(UIMessageKeys.PROCESSING_FORM_START_SCREENING_BUTTON));
		startProcessing.addStyleName(CustomValoTheme.FLOAT_RIGHT);
		startProcessing.addStyleName(CustomValoTheme.MARGIN_NONE_RIGHT);
		startProcessing.setIcon(VaadinIcons.SEARCH);
		startProcessing.addClickListener((Button.ClickListener) event -> executeSearch());

		CollapsibleLayout parameterCollapseLayout = componentFactory.createCollapseableLayout(
				i18n.get(UIMessageKeys.PROCESSING_FORM_PARAMETERS_HEADER), container, false,
				false);
		parameterCollapseLayout.addHeaderButton(startProcessing);

		CssLayout parameterLayout = componentFactory.createRowLayout(parameterCollapseLayout);
		parameterLayout.addStyleName(CustomValoTheme.MARGIN_HALF_RIGHT);
		parameterLayout.addStyleName(CustomValoTheme.PADDING_HALF_TOP);
		parameterLayout.setWidth("100%");


		binder.bind(ppmComboBox, ProcessingSettings::getRequestedPrecursorPpm, ProcessingSettings::setRequestedPrecursorPpm);
		binder.bind(ppmFragmentsComboBox, ProcessingSettings::getPpmFragments, ProcessingSettings::setPpmFragments);
		binder.bind(ionisationComboBox, ProcessingSettings::getIonisation, ProcessingSettings::setIonisation);
		binder.forField(intensityTresholdField)
				.withConverter(new StringToDoubleConverter("No valid number input"))
				.bind(ProcessingSettings::getIntensityThreshold, ProcessingSettings::setIntensityThreshold);

		return parameterLayout;
	}

	@Override
	protected void reuseSettings(ProcessingJob job) {
		ProcessingSettings lastUsedSettings = currentJob.getSettings();
		job.getSettings().setPrecursorPpm(lastUsedSettings.getPrecursorPpm());
		job.getSettings().setPpmFragments(lastUsedSettings.getPpmFragments());
		job.getSettings().setIntensityThreshold(lastUsedSettings.getIntensityThreshold());
	}
}