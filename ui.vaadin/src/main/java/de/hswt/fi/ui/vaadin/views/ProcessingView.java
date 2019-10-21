package de.hswt.fi.ui.vaadin.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.components.ContainerContentComponent;
import de.hswt.fi.ui.vaadin.container.ProcessingResultContainer;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.handler.processing.ProcessingViewHandler;
import de.hswt.fi.ui.vaadin.views.components.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

@SideBarItem(sectionId = Sections.PROCESSING, captionCode = UIMessageKeys.PROCESSING_VIEW_CAPTION,
		order = 100)
@VaadinFontIcon(VaadinIcons.COG)
@SpringView(name = ProcessingView.VIEW_NAME)
public class ProcessingView extends AbstractLayoutView {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingView.class);
	
	public static final String VIEW_NAME = "processing";

	// Do not remove. In order to be created by Spring, this bean needs to wired
	// here.
	@SuppressWarnings("unused")
	private final ProcessingViewHandler processingViewHandler;

	private final I18N i18n;

	private final ProcessingFormComponent processingFormComponent;

	private final DatabaseSourceComponent databaseSourceComponent;

	private final ProcessingHistoryComponent processingHistoryComponent;

	private final ProcessingResultsComponent processingResultsComponent;

	private final ProcessingDataComponent processingDataComponent;

	private final ProcessingHelpComponent processingHelpComponent;

	private final ProcessingDetailsComponent processingDetailsComponent;

	private final MsDetailsComponent msmsDetailsComponent;

	@Autowired
	public ProcessingView(ProcessingViewHandler processingViewHandler, I18N i18n,
						  ProcessingFormComponent processingFormComponent, DatabaseSourceComponent databaseSourceComponent,
						  ProcessingHistoryComponent processingHistoryComponent,
						  ProcessingResultsComponent processingResultsComponent,
						  ProcessingDataComponent processingDataComponent,
						  ProcessingHelpComponent processingHelpComponent,
						  ProcessingDetailsComponent processingDetailsComponent,
						  MsDetailsComponent msmsDetailsComponent) {
		this.processingViewHandler = processingViewHandler;
		this.i18n = i18n;
		this.processingFormComponent = processingFormComponent;
		this.databaseSourceComponent = databaseSourceComponent;
		this.processingHistoryComponent = processingHistoryComponent;
		this.processingResultsComponent = processingResultsComponent;
		this.processingDataComponent = processingDataComponent;
		this.processingHelpComponent = processingHelpComponent;
		this.processingDetailsComponent = processingDetailsComponent;
		this.msmsDetailsComponent = msmsDetailsComponent;
	}

	@Override
	protected void initComponents() {
		initSearchComponent();
		initSourceListsComponent();
		initSearchHistoryComponent();
		initDetailsComponent();
		initResultsComponent();
	}

	private void initSearchComponent() {
		addSearchComponent(processingFormComponent);
	}

	private void initSourceListsComponent() {
		addSourceListsComponent(databaseSourceComponent);
	}

	private void initSearchHistoryComponent() {
		addSearchHistoryComponent(processingHistoryComponent);
	}

	private void initDetailsComponent() {
		addDetailsComponent(processingDetailsComponent);
	}

	private void initResultsComponent() {
		addResultsComponent(processingResultsComponent);
		addResultsComponent(processingDataComponent);
		addResultsComponent(processingHelpComponent);

		setCurrentResultComponent(processingHelpComponent);
	}

	@Override
	protected Layout getDefaultLayout() {
		return Layout.COLUMN;
	}

	@Override
	protected String getViewTitle() {
		return i18n.get(UIMessageKeys.PROCESSING_VIEW_VIEW_TITLE);
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.SOURCE_HANDLER_CONTAINER_CHANGED)
	private void handleResultsChange(ProcessingResultContainer container) {
		LOGGER.debug(
				"entering event bus listener handleResultsChange with payload {} and topic {}",
				container, EventBusTopics.SOURCE_HANDLER_CONTAINER_CHANGED);

		setCurrentResultComponent(processingResultsComponent);
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.PROCESSING_RESULTS_TAB_CHANGED)
	private void handleTabChange(ContainerContentComponent currentTab) {
		LOGGER.debug(
				"entering event bus listener handleTabChange with payload {} and topic {}",
				currentTab, EventBusTopics.PROCESSING_RESULTS_TAB_CHANGED);
		
		//When Search content tab is selected change details to msmsdetails
		if(ProcessingDataComponent.class.isInstance(currentTab)) {
			changeDetailsComponent(msmsDetailsComponent);
		}
		else {
			changeDetailsComponent(processingDetailsComponent);
		}
	}
}