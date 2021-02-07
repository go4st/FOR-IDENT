package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.renderers.ImageRenderer;
import de.hswt.fi.common.spring.Profiles;
import de.hswt.fi.processing.service.model.ProcessCandidate;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.GridRendererProvider;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import de.hswt.fi.ui.vaadin.views.components.processing.ProcessingResultDetailsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.i18n.I18N;

import javax.inject.Provider;

@Profile({Profiles.LC, Profiles.DEVELOPMENT_LC, Profiles.TEST})
@SpringComponent
@ViewScope
public class ProcessingResultsComponentLC extends AbstractProcessingResultsComponent {

	private static final long serialVersionUID = 1L;

	@Autowired
	public ProcessingResultsComponentLC(EventBus.ViewEventBus eventBus, I18N i18n, ComponentFactory componentFactory,
										Provider<ProcessingResultDetailsComponent> detailsProvider) {
		super(eventBus, i18n, componentFactory, detailsProvider);
	}

	@Override
	protected void initRtiScreeningColumns() {
		Column moleculeDependencyColumn = grid.addColumn(processCandidate -> processCandidate.getIndexSearchResult().getMoleculePhDependency())
				.setWidth(LayoutConstants.COLUMN_WIDTH_TINY)
				.setCaption(i18n.get(UIMessageKeys.RTI_RESULT_COLUMN_PH))
				.setStyleGenerator(item -> CustomValoTheme.TEXT_CENTER)
				.setRenderer(value -> {
					if (value == null) {
						return null;
					}
					switch (value) {
						case NEUTRAL:
							return new ExternalResource("./img/circle-green.png");
						case NEUTRAL_UPPER:
							return new ExternalResource("./img/circle-purple.png");
						case NEUTRAL_LOWER:
							return new ExternalResource("./img/circle-yellow.png");
						case NEGATIVE_LOADABLE:
							return new ExternalResource("./img/circle-blue.png");
						case POSITIVE_LOADABLE:
							return new ExternalResource("./img/circle-red.png");
						default:
							return null;
					}
				}, new ImageRenderer<ExternalResource>());

		Column<ProcessCandidate, Double> rtiColumn = grid.addFilterColumn(
				processCandidate -> processCandidate.getIndexSearchResult().getRetentionTimeIndex())
				.setFilterType(Double.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM).setCaption(i18n.get(UIMessageKeys.RTI_RESULT_COLUMN_RTI))
				.setRenderer(GridRendererProvider.getLocalizedRenderer(1));

		Column<ProcessCandidate, Double> resultLogDColumn = grid.addFilterColumn(
				processCandidate -> processCandidate.getIndexSearchResult().getResultLogD())
				.setFilterType(Double.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setCaption(i18n.get(UIMessageKeys.RTI_RESULT_COLUMN_RESULT_LOG_D))
				.setRenderer(GridRendererProvider.getLocalizedRenderer(2));

		Column<ProcessCandidate, Double> targetLogDColumn = grid.addFilterColumn(
				processCandidate -> processCandidate.getIndexSearchResult().getTargetLogD())
				.setFilterType(Double.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setCaption(i18n.get(UIMessageKeys.RTI_RESULT_COLUMN_TARGET_LOG_D))
				.setRenderer(GridRendererProvider.getLocalizedRenderer(2));

		Column<ProcessCandidate, Double> deltaLogDRtiDbColumn = grid.addFilterColumn(
				processCandidate -> processCandidate.getIndexSearchResult().getDeltaLogDRtiDb())
				.setFilterType(Double.class)
				.setHidable(true).setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setRenderer(GridRendererProvider.getLocalizedRenderer(2))
				.setCaption(i18n.get(UIMessageKeys.RTI_RESULT_COLUMN_DELTA_RTI_RESULT_LOG_D));

		Column<ProcessCandidate, Double> adjustedLogDColumn = grid.addFilterColumn(
				processCandidate -> processCandidate.getIndexSearchResult().getAdjustedLogD())
				.setFilterType(Double.class)
				.setHidable(true)
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setCaption(i18n.get(UIMessageKeys.RTI_RESULT_COLUMN_RTI_ADJUSTED_RTI_LOGD))
				.setRenderer(GridRendererProvider.getLocalizedRenderer(2));

		Column<ProcessCandidate, Double> deltaLogDAdjustedDbColumn = grid.addFilterColumn(
				processCandidate -> processCandidate.getIndexSearchResult().getDeltaLogDAdjustedDb())
				.setFilterType(Double.class)
				.setHidable(true).setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setRenderer(GridRendererProvider.getLocalizedRenderer(2))
				.setCaption(i18n.get(UIMessageKeys.RTI_RESULT_COLUMN_RTI_DELTA_ADJUSTED_RTI_RESULT_LOGD));

		groupingRow.join(moleculeDependencyColumn, rtiColumn, resultLogDColumn, targetLogDColumn,
				deltaLogDRtiDbColumn, adjustedLogDColumn, deltaLogDAdjustedDbColumn)
				.setText(i18n.get(UIMessageKeys.RTI_SCREENING_COMBINED_COLUMN));
	}
}