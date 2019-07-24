package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Label;
import de.hswt.fi.model.Feature;
import de.hswt.fi.model.Peak;
import de.hswt.fi.processing.service.model.ProcessingJob;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.GridRendererProvider;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.components.ContainerContentComponent;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.layouts.ColumnLayout;
import de.hswt.fi.ui.vaadin.layouts.RowLayout;
import de.hswt.fi.ui.vaadin.layouts.ViewLayout;
import de.hswt.fi.ui.vaadin.mschart.MsChart;
import de.hswt.fi.ui.vaadin.mschart.MsChartConfig;
import de.hswt.fi.ui.vaadin.mschart.MsSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringComponent
@PrototypeScope
public class MsDetailsComponent extends ContainerContentComponent {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(MsDetailsComponent.class);

	private final I18N i18n;

	private final EventBus.ViewEventBus viewEventBus;

	private CssLayout headerLayout;

	private Grid<Peak> detailsGrid;

	private CssLayout layout;

	private Label selectedFeatureLabel;

	private CssLayout chartLayout;

	private MsChart chart;

	@Autowired
	public MsDetailsComponent(I18N i18n, EventBus.ViewEventBus viewEventBus) {
		this.i18n = i18n;
		this.viewEventBus = viewEventBus;
	}

	@PostConstruct
	private void postConstruct() {
		init();
		setSizeFull();
		initHeader();
		viewEventBus.subscribe(this);
	}

	private void initHeader() {
		headerLayout = new CssLayout();
	}

	private void init() {

		layout = new CssLayout();
		layout.setSizeFull();
		layout.addStyleName(CustomValoTheme.PADDING);

		selectedFeatureLabel = new Label();
		selectedFeatureLabel.setHeight("5%");
		layout.addComponent(selectedFeatureLabel);
		
		// Chart
		chartLayout = new CssLayout();
		chartLayout.addStyleName(CustomValoTheme.PADDING_HALF_HORIZONTAL);
		chartLayout.setSizeFull();

		MsChartConfig config = new MsChartConfig()
				.withAxisCaptionX("m/z")
				.withAxisCaptionY("Intensity")
				.withBarStrokeWidth(3)
				.withAxisColor("#474747")
				.withSelectionColor("#f4bc42")
				.withHoverColor("#9c9c9c")
				.withTextHoverColor("#383838");

		chart = new MsChart(new ArrayList<>(), config);
		chart.addStyleName(CustomValoTheme.PADDING_RIGHT);
		chart.setWidth("100%");
		chart.setHeight("30%");

		chartLayout.addComponent(chart);

		detailsGrid = new Grid<>();
		detailsGrid.setPrimaryStyleName(CustomValoTheme.GRID_CUSTOM);
		detailsGrid.setHeightMode(HeightMode.CSS);
		detailsGrid.setSelectionMode(SelectionMode.NONE);
		detailsGrid
				.addColumn(Peak::getMz)
				.setCaption(i18n.get(UIMessageKeys.MSMS_DETAILS_COMPONENT_MZ))
				.setRenderer(GridRendererProvider.getLocalizedRenderer(4));

		detailsGrid
				.addColumn(Peak::getIntensity)
				.setCaption(i18n.get(UIMessageKeys.MSMS_DETAILS_COMPONENT_INTENSITY))
				.setRenderer(GridRendererProvider.getLocalizedRenderer(2));

		detailsGrid.setWidth("100%");
		detailsGrid.setHeight("65%");
		detailsGrid.setVisible(false);
		chartLayout.addComponent(detailsGrid);
		
		chartLayout.setVisible(false);

		layout.addComponent(chartLayout);
		addComponent(layout);
	}

	private void updateGrid(List<Peak> peaks) {
		detailsGrid.setItems(peaks);
		detailsGrid.setVisible(true);
	}

	@Override
	public String getTitle() {
		return i18n.get(UIMessageKeys.MSMS_DETAILS_COMPONENT_CAPTION);
	}

	@Override
	public Component getHeaderComponent() {
		return headerLayout;
	}


	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.PROCESSING_DATA_MSMS_ENTRY_SELECTED)
	private void handleMsmsEntrySelected(Feature feature) {
		LOGGER.debug(
				"entering event bus listener handleMsmsEntrySelected " + "with payload {} and topic {}",
				feature, EventBusTopics.PROCESSING_DATA_MSMS_ENTRY_SELECTED);
		updateChart(feature.getIdentifier(), feature.getPeaks());
	}

	private void updateChart(String caption, List<Peak> peaks) {
		
		if (!chartLayout.isVisible()) {
			chartLayout.setVisible(true);
		}

		selectedFeatureLabel.setValue(i18n.get(UIMessageKeys.MSMS_DETAILS_COMPONENT_SELECTED_FEATURE_LABEL) + " " + caption);

		if (peaks.isEmpty()) {
			chartLayout.setVisible(false);
			selectedFeatureLabel.setValue(i18n.get(UIMessageKeys.MSMS_DETAILS_COMPONENT_SELECTED_FEATURE_LABEL_NO_DATA) + caption);
			return;
		}

		setSeries(peaks, "#0058BC");
		updateGrid(peaks);
	}

	public void setSeries(List<Peak> peaks, String color) {
		MsSeries series = new MsSeries("ms data");
		series.setColor(color);
		peaks.forEach(peak -> series.addPoint(new MsSeries.Point(peak.getMz(), peak.getIntensity())));
		chart.setSeries(series);
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.FILE_SELECTED)
	private void handleFileSelection(ProcessingJob processJob) {
		LOGGER.debug("entering event bus listener handleFileSelection with payload {} in topic {}",
				processJob, EventBusTopics.FILE_SELECTED);
			chartLayout.setVisible(false);
	}

	@Override
	public void layoutChanged(ViewLayout layout) {
		
		if(RowLayout.class.isInstance(layout)) {
			chart.setWidth("50%");
			chart.setHeight("95%");
			detailsGrid.setWidth("50%");
			detailsGrid.setHeight("95%");
		} else if(ColumnLayout.class.isInstance(layout)){
			chart.setWidth("100%");
			chart.setHeight("30%");
			detailsGrid.setHeight("65%");
			detailsGrid.setWidth("100%");
		}
	}
}