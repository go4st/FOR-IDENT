package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import de.hswt.fi.ui.vaadin.CustomNotification;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.components.ContainerContentComponent;
import de.hswt.fi.ui.vaadin.container.ResultContainer;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DownloadRequestPayload;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DummyPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.teemu.switchui.Switch;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

public abstract class AbstractResultsComponent<SEARCHPARAMETER, RESULT, ENTRY, RESULTCONTAINER extends ResultContainer<SEARCHPARAMETER, RESULT, ENTRY>>
		extends ContainerContentComponent {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(AbstractResultsComponent.class);

	private static final int MIN_SCREEN_WIDTH_FOR_FROZEN_COLUMN = 1400;

	protected final ViewEventBus eventBus;

	protected final I18N i18n;

	protected List<ENTRY> results;

	protected Layout headerLayout;

	private boolean sendSelectionChanged = true;

	private RESULTCONTAINER resultContainer;

	protected abstract Layout getHeaderLayout();

	protected abstract void initComponents();

	protected abstract void resultsContainerChanged();

	protected abstract void select(ENTRY selection);

	protected abstract void clearFilter();

	protected abstract List<ENTRY> getSelectedResults();

	protected abstract List<Column<ENTRY, ?>> getColumns();

	protected abstract void setFrozenColumns(boolean frozen);

	@Autowired
	public AbstractResultsComponent(ViewEventBus eventBus, I18N i18n) {
		this.eventBus = eventBus;
		this.i18n = i18n;
	}

	@PostConstruct
	private void postConstruct() {
		initHeader();
		initComponents();
		eventBus.subscribe(this);
	}

	@PreDestroy
	private void preDestroy() {
		eventBus.unsubscribe(this);
	}

	protected void initHeader() {
		headerLayout = getHeaderLayout();
	}

	void initFrozenColumnSwitch() {
		Label switchLabel = new Label(i18n.get(UIMessageKeys.ABSTRACT_RESULTS_COMPONENT_FIXED_COLUMN_CAPTION));
		switchLabel.setWidthUndefined();
		switchLabel.setHeight("100%");
		switchLabel.addStyleName(CustomValoTheme.MARGIN_RIGHT);
		switchLabel.addStyleName(ValoTheme.LABEL_LARGE);
		switchLabel.addStyleName(ValoTheme.LABEL_LIGHT);
		switchLabel.addStyleName(CustomValoTheme.COLOR_ALT1);
		headerLayout.addComponent(switchLabel);

		Switch switchButton = new Switch();
		switchButton.setPrimaryStyleName("v-custom-switch");
		switchButton.addStyleName(CustomValoTheme.BORDER_COLOR_WHITE);
		switchButton.addValueChangeListener(e -> setFrozenColumns(switchButton.getValue()));
		switchButton.setValue(Page.getCurrent().getBrowserWindowWidth() > MIN_SCREEN_WIDTH_FOR_FROZEN_COLUMN);
		headerLayout.addComponent(switchButton);
	}

	void handleResultDownload() {
		LOG.debug("Entering method handleResultDownload");

		if (results == null) {
			LOG.debug("Results ResultContainer is null - returning");
			return;
		}

		List<ENTRY> ids = getSelectedResults();
		List<Column<ENTRY, ?>> columns = getColumns();

		LOG.debug("publish event inside handleResultDownload with topic {}",
				EventBusTopics.TARGET_HANDLER_REQUEST_RESULT_DOWNLOAD);
		eventBus.publish(EventBusTopics.TARGET_HANDLER_REQUEST_RESULT_DOWNLOAD, this,
				new DownloadRequestPayload<>(ids, columns));
	}

	void handleNonResultDownload() {
		LOG.debug("Entering method handleNonResultDownload");

		if (results == null) {
			LOG.debug("Non Results ResultContainer is null - returning");
			return;
		}

		LOG.debug("publish event inside handleNonResultDownload with topic {}",
				EventBusTopics.TARGET_HANDLER_REQUEST_RESULT_DOWNLOAD);
		eventBus.publish(EventBusTopics.TARGET_HANDLER_REQUEST_NON_RESULT_DOWNLOAD, this, DummyPayload.INSTANCE);
	
		
	}

	void handleCompleteDownload() {
		LOG.debug("Entering method handleCompleteDownload");

		if (results == null) {
			LOG.debug("Results ResultContainer is null - returning");
			return;
		}

		List<ENTRY> ids = getSelectedResults();
		List<Column<ENTRY, ?>> columns = getColumns();
		
		LOG.debug("publish event inside handleCompleteDownload with topic {}",
				EventBusTopics.TARGET_HANDLER_REQUEST_COMPLETE_DOWNLOAD);
		eventBus.publish(EventBusTopics.TARGET_HANDLER_REQUEST_COMPLETE_DOWNLOAD, this,
				new DownloadRequestPayload<>(ids, columns));
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.SOURCE_HANDLER_ENTRY_SELECTED)
	private void handleEntrySelection(ENTRY entry) {
		LOG.debug("entering event bus listener handleEntrySelection with ENTRY {} in topic {}", entry,
				EventBusTopics.SOURCE_HANDLER_ENTRY_SELECTED);
		sendSelectionChanged = false;
		select(entry);
		sendSelectionChanged = true;
	}

	void handleReportRecords() {
		List<ENTRY> entries = getSelectedResults();
		if (entries == null || entries.isEmpty()) {
			return;
		}

		LOG.debug("publish event inside handleReportRecord with topic {}",
				EventBusTopics.TARGET_HANDLER_REPORT_TO_STAFF);
		eventBus.publish(EventBusTopics.TARGET_HANDLER_REPORT_TO_STAFF, this, getSelectedResults());
	}

	private void resultsContainerChangedInternal() {
		sendSelectionChanged = false;
		resultsContainerChanged();
		sendSelectionChanged = true;
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.SOURCE_HANDLER_CONTAINER_CHANGED)
	private void handleSearchResultSelection(RESULTCONTAINER resultContainer) {
		LOG.debug("entering event bus listener handleSearchResultSelection with RESULTCONTAINER {} in topic {}",
				resultContainer, EventBusTopics.SOURCE_HANDLER_CONTAINER_CHANGED);

		this.resultContainer = resultContainer;

		if (!resultContainer.getResultsContainer().equals(results)) {
			results = resultContainer.getResultsContainer();
			resultsContainerChangedInternal();
		}

		if (results.isEmpty()) {
			new CustomNotification.Builder(
					i18n.get(UIMessageKeys.ABSTRACT_RESULTS_COMPONENT_NO_RESULTS_NOTIFICATION_CAPTION),
					i18n.get(UIMessageKeys.ABSTRACT_RESULTS_COMPONENT_NO_RESULTS_NOTIFICATION_DESCRIPTION),
					Type.HUMANIZED_MESSAGE).build().show(Page.getCurrent());
		}
	}

	void selectionChanged(ENTRY entry) {
		if (entry == null || !sendSelectionChanged) {
			return;
		}

		LOG.debug("publish event inside selectionChanged with topic {}", EventBusTopics.TARGET_HANDLER_SELECT_ENTRY);
		eventBus.publish(EventBusTopics.TARGET_HANDLER_SELECT_ENTRY, this, entry);
	}

	protected int getMinScreenWidthForFrozenColumn() { return MIN_SCREEN_WIDTH_FOR_FROZEN_COLUMN; }

	protected boolean hasFrozenColumns() {
		return false;
	}

	protected boolean isNonResultsPossible() {
		return true;
	}

	RESULTCONTAINER getResultContainer() {
		return resultContainer;
	}

	@Override
	public Component getHeaderComponent() {
		return headerLayout;
	}

	// Do not override hash() and equals() in abstract component classes because of identity issues when attach / remove
}
