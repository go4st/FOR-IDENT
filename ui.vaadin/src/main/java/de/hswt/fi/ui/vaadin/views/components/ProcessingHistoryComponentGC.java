package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.common.spring.Profiles;
import de.hswt.fi.processing.service.model.ProcessingJob;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.i18n.I18N;

import java.util.ArrayList;
import java.util.List;

@Profile({Profiles.GC, Profiles.DEVELOPMENT_GC})
@SpringComponent
@ViewScope
public class ProcessingHistoryComponentGC extends AbstractProcessingSearchHistoryComponent {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(ProcessingHistoryComponentGC.class);

	@Autowired
	public ProcessingHistoryComponentGC(EventBus.ViewEventBus eventBus, I18N i18n, ComponentFactory componentFactory) {
		super(eventBus, i18n, componentFactory);
	}

	@Override
	protected List<String> getSearchParameterCaptions(ProcessingJob job) {

		List<String> parameters = new ArrayList<>();

		parameters.add(i18n.get(UIMessageKeys.PROCESSING_FORM_PRECURSOR_PPM) + ": "
				+ job.getSettings().getPrecursorPpm());

		parameters.add(i18n.get(UIMessageKeys.PROCESSING_FORM_FRAGMENTS_PPM) + ": "
				+ job.getSettings().getPpmFragments());

		parameters.add(i18n.get(UIMessageKeys.IONISATION_COMBO_BOX_CAPTION) + ": "
				+ job.getSettings().getIonisation());

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
}
