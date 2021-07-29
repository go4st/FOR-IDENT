package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.NumberRenderer;
import de.hswt.fi.common.spring.Profiles;
import de.hswt.fi.model.Feature;
import de.hswt.fi.model.RTICalibrationData;
import de.hswt.fi.processing.service.model.ProcessingJob;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.GridRendererProvider;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.components.ContainerContentComponent;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.i18n.LocaleContextHolder;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Profile({Profiles.GC, Profiles.DEVELOPMENT_GC})
@SpringComponent
@ViewScope
public class ProcessingDataComponentGC extends AbstractProcessingDataComponent {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingDataComponentGC.class);

	private static final int FOLLOWING_DIGITS_RTI = 1;

	private static final int FOLLOWING_DIGITS_RT = 2;

	private static final int FOLLOWING_DIGITS_LOGD = 2;

	private final ViewEventBus eventBus;

	private final I18N i18n;

	private Grid<RTICalibrationData> calibrationDataGrid;

	private Grid<Feature> featureGrid;

	private ProcessingJob processJob;

	private CssLayout headerLayout;

	private CssLayout upperGridLayoutWrapper;

	private CssLayout lowerGridLayoutWrapper;

	private boolean isRtiAvailable;

	@Autowired
	public ProcessingDataComponentGC(ViewEventBus eventBus, I18N i18n) {
		this.eventBus = eventBus;
		this.i18n = i18n;
	}

	@PostConstruct
	private void postConstruct() {
		setSizeFull();
		initHeader();
		initGrids();
		initGridColumns();
		eventBus.subscribe(this);
		isRtiAvailable = false;
	}

	@PreDestroy
	private void preDestroy() {
		eventBus.unsubscribe(this);
	}

	protected void initHeader() {
		headerLayout = new CssLayout();
	}

	private void initGrids() {
		initCalibrationDataGrid();
		initTargetDataGrid();
	}

	private void initCalibrationDataGrid() {
		upperGridLayoutWrapper = new CssLayout();
		upperGridLayoutWrapper.setWidth("100%");
		upperGridLayoutWrapper.setHeight("50%");
		upperGridLayoutWrapper.addStyleName(CustomValoTheme.PADDING);
		addComponent(upperGridLayoutWrapper);

		calibrationDataGrid = new Grid<>();
		calibrationDataGrid.setPrimaryStyleName(CustomValoTheme.GRID_CUSTOM);
		calibrationDataGrid.setSizeFull();
		calibrationDataGrid.setHeightMode(HeightMode.CSS);
		calibrationDataGrid.setSelectionMode(SelectionMode.NONE);
		calibrationDataGrid.addStyleName(CustomValoTheme.GRID_CUSTOM);

		calibrationDataGrid.setStyleGenerator(item -> {
			if (!item.isValid()) {
				return CustomValoTheme.COLOR_RED;
			}
			return "";
		});

		upperGridLayoutWrapper.addComponent(calibrationDataGrid);
		upperGridLayoutWrapper.setVisible(false);
	}

	private void initTargetDataGrid() {
		lowerGridLayoutWrapper = new CssLayout();
		lowerGridLayoutWrapper.setWidth("100%");
		lowerGridLayoutWrapper.setHeight("50%");
		lowerGridLayoutWrapper.addStyleName(CustomValoTheme.PADDING);
		addComponent(lowerGridLayoutWrapper);

		featureGrid = new Grid<>();
		featureGrid.setPrimaryStyleName(CustomValoTheme.GRID_CUSTOM);
		featureGrid.setSizeFull();
		featureGrid.setHeightMode(HeightMode.CSS);
		featureGrid.setSelectionMode(SelectionMode.SINGLE);
		lowerGridLayoutWrapper.addComponent(featureGrid);
		lowerGridLayoutWrapper.setVisible(false);

		featureGrid.addSelectionListener(event -> {
			if (!featureGrid.getSelectedItems().isEmpty()) {
				Feature selectedItem = featureGrid.getSelectedItems().iterator().next();
				LOGGER.debug(
						"entering event bus listener msmsEntrySelected "
								+ "with payload {} and topic {}",
						selectedItem, EventBusTopics.PROCESSING_DATA_MSMS_ENTRY_SELECTED);
				eventBus.publish(EventBusTopics.PROCESSING_DATA_MSMS_ENTRY_SELECTED, this,
						selectedItem);
			}
		});
	}

	private void initGridColumns() {
		initCalibrationDataGridColumns();
		initFeatureGridColumns();
	}

	private void initFeatureGridColumns() {

		featureGrid.addColumn(Feature::getIdentifier)
				.setWidth(LayoutConstants.COLUMN_WIDTH_SMALL)
				.setCaption(i18n.get(UIMessageKeys.RTI_DATA_TARGET_COLUMN_TARGET));

		featureGrid.addColumn(Feature::getPrecursorMass)
				.setWidth(LayoutConstants.COLUMN_WIDTH_SMALL)
				.setCaption(i18n.get(UIMessageKeys.RTI_DATA_TARGET_COLUMN_ACCURATE_MASS))
				.setRenderer(new NumberRenderer("%.4f", LocaleContextHolder.getLocale()));

		featureGrid.addColumn(Feature::getNeutralFormula)
				.setWidth(LayoutConstants.COLUMN_WIDTH_SMALL)
				.setCaption(i18n.get(UIMessageKeys.RTI_DATA_TARGET_COLUMN_ELEMENTAL));

		featureGrid.addColumn(feature -> getHtmlCheckbox(feature.isMassCalculated()), new HtmlRenderer())
				.setWidth(LayoutConstants.COLUMN_WIDTH_MEDIUM)
				.setCaption(i18n.get(UIMessageKeys.RTI_DATA_TARGET_COLUMN_MASS_CALCULATED));

		featureGrid.addColumn(Feature::getRetentionTime)
				.setWidth(LayoutConstants.COLUMN_WIDTH_SMALL)
				.setCaption(i18n.get(UIMessageKeys.RTI_DATA_TARGET_COLUMN_RT))
				.setRenderer(GridRendererProvider.getLocalizedRenderer(FOLLOWING_DIGITS_RT));

		featureGrid.addColumn(Feature::getRetentionTimeIndex)
				.setWidth(LayoutConstants.COLUMN_WIDTH_SMALL)
				.setCaption(i18n.get(UIMessageKeys.RTI_DATA_TARGET_COLUMN_RTI))
				.setRenderer(GridRendererProvider.getLocalizedRenderer(FOLLOWING_DIGITS_RTI));

		featureGrid.addColumn(Feature::getRetentionTimeSignal)
				.setCaption(i18n.get(UIMessageKeys.RTI_DATA_TARGET_COLUMN_LOG_D))
				.setRenderer(GridRendererProvider.getLocalizedRenderer(2))
				.setExpandRatio(FOLLOWING_DIGITS_LOGD);
	}

	private void initCalibrationDataGridColumns() {

		calibrationDataGrid.addColumn(RTICalibrationData::getIdentifier)
				.setWidth(LayoutConstants.COLUMN_WIDTH_SMALL).setCaption(
				i18n.get(UIMessageKeys.RTI_DATA_CALIBRATION_COLUMN_SUBSTANCE_NAME));

		//TODO I18N
		calibrationDataGrid.addColumn(rtiCalibrationData -> getHtmlCheckbox(rtiCalibrationData.isValid()), new HtmlRenderer())
				.setCaption("Valid RT");

		calibrationDataGrid.addColumn(RTICalibrationData::getRti)
				.setWidth(LayoutConstants.COLUMN_WIDTH_SMALL)
				.setCaption(i18n.get(UIMessageKeys.RTI_DATA_CALIBRATION_COLUMN_RTI))
				.setRenderer(GridRendererProvider.getLocalizedRenderer(FOLLOWING_DIGITS_RTI));

		calibrationDataGrid.addColumn(RTICalibrationData::getSignal)
				.setWidth(LayoutConstants.COLUMN_WIDTH_SMALL)
				.setCaption(i18n.get(UIMessageKeys.RTI_DATA_CALIBRATION_COLUMN_SIGNAL_HENRY))
				.setRenderer(GridRendererProvider.getLocalizedRenderer(FOLLOWING_DIGITS_LOGD));

		calibrationDataGrid.addColumn(RTICalibrationData::getMeanRt)
				.setExpandRatio(1)
				.setCaption(i18n.get(UIMessageKeys.RTI_DATA_CALIBRATION_COLUMN_MEAN_RT))
				.setRenderer(GridRendererProvider.getLocalizedRenderer(FOLLOWING_DIGITS_RT));
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.FILE_SELECTED)
	private void handleFileSelection(ProcessingJob processJob) {
		LOGGER.debug("entering event bus listener handleFileSelection with payload {} in topic {}",
				processJob, EventBusTopics.FILE_SELECTED);

		this.processJob = processJob;
		updateGridContent();
	}

	private void updateGridContent() {
		updateIsRtiAvailable();
		updateTargetDataGrid();
		updateCalibrationDataGrid();
	}

	private void updateIsRtiAvailable() {
		isRtiAvailable = processJob.getFeatureSet().getRtiCalibrationData().isEmpty();
	}

	private void updateCalibrationDataGrid() {

		upperGridLayoutWrapper.setVisible(true);
		upperGridLayoutWrapper.removeAllComponents();

		if (isRtiAvailable) {
			upperGridLayoutWrapper.addComponent(featureGrid);
			upperGridLayoutWrapper.setHeight("100%");
			return;
		}

		upperGridLayoutWrapper.setHeight("50%");
		upperGridLayoutWrapper.addComponent(calibrationDataGrid);

		calibrationDataGrid.setItems(processJob.getFeatureSet().getRtiCalibrationData());
		calibrationDataGrid.recalculateColumnWidths();
	}

	private void updateTargetDataGrid() {

		lowerGridLayoutWrapper.setVisible(true);
		lowerGridLayoutWrapper.removeAllComponents();
		lowerGridLayoutWrapper.addComponent(featureGrid);

		featureGrid.setItems(processJob.getFeatureSet().getFeatures());
		featureGrid.recalculateColumnWidths();
	}

	@Override
	public String getTitle() {
		return i18n.get(UIMessageKeys.RTI_DATA_COMPONENT_HEADER_CAPTION);
	}

	@Override
	public Component getHeaderComponent() {
		return headerLayout;
	}

	private String getHtmlCheckbox(boolean checked) {
		return checked ? "&#9745;" : "&#9744;";
	}
}