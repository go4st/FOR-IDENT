package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Grid.Column;
import de.hswt.fi.common.spring.Profiles;
import de.hswt.fi.processing.service.model.ProcessCandidate;
import de.hswt.fi.ui.vaadin.GridRendererProvider;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import de.hswt.fi.ui.vaadin.views.components.processing.ProcessingResultDetailsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.i18n.I18N;

import javax.inject.Provider;

@Profile({Profiles.GC, Profiles.DEVELOPMENT_GC})
@SpringComponent
@ViewScope
public class ProcessingResultsComponentGC extends AbstractProcessingResultsComponent {

	private static final long serialVersionUID = 1L;

	@Autowired
	public ProcessingResultsComponentGC(EventBus.ViewEventBus eventBus, I18N i18n, ComponentFactory componentFactory,
                                        Provider<ProcessingResultDetailsComponent> detailsProvider) {
		super(eventBus, i18n, componentFactory, detailsProvider);
	}

	@Override
	protected void initRtiScreeningColumns() {

		Column<ProcessCandidate, Double> rtiColumn = grid.addFilterColumn(
				processCandidate -> (processCandidate.getIndexSearchResult()).getRetentionTimeIndex())
				.setFilterType(Double.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM).setCaption("RTI")
				.setRenderer(GridRendererProvider.getLocalizedRenderer(1));

		Column<ProcessCandidate, Double> resultSignalColumn = grid.addFilterColumn(
				processCandidate -> (processCandidate.getEntry().getHenryBond().getValue()))
				.setFilterType(Double.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setCaption("Result Henry Konstante")
//				.setCaption(i18n.get(UIMessageKeys.RTI_RESULT_COLUMN_RESULT_LOG_D))
				.setRenderer(GridRendererProvider.getLocalizedRenderer(2));

		Column<ProcessCandidate, Double> targetSignalColumn = grid.addFilterColumn(
				processCandidate -> (processCandidate.getIndexSearchResult().getRetentionTimeSignal()))
				.setFilterType(Double.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setCaption("Target Henry Konstante")
//				.setCaption(i18n.get(UIMessageKeys.RTI_RESULT_COLUMN_TARGET_LOG_D))
				.setRenderer(GridRendererProvider.getLocalizedRenderer(2));

		Column<ProcessCandidate, Double> deltaLogDRtiDbColumn = grid.addFilterColumn(
				processCandidate -> (processCandidate.getIndexSearchResult().getDeltaRetentionTimeSignal()))
				.setFilterType(Double.class)
				.setHidable(true).setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setRenderer(GridRendererProvider.getLocalizedRenderer(2))
				.setCaption("Delta Henry");

		groupingRow.join(rtiColumn, resultSignalColumn, targetSignalColumn, deltaLogDRtiDbColumn).setText("RTI Screening");
	}
}