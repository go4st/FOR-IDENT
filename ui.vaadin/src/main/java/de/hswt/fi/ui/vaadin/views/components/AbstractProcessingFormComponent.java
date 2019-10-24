package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import de.hswt.fi.processing.service.model.ProcessingJob;
import de.hswt.fi.processing.service.model.ProcessingSettings;
import de.hswt.fi.processing.service.model.ScoreSettings;
import de.hswt.fi.ui.vaadin.CustomNotification;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.components.CollapsibleLayout;
import de.hswt.fi.ui.vaadin.components.ContainerContentComponent;
import de.hswt.fi.ui.vaadin.components.CustomList;
import de.hswt.fi.ui.vaadin.components.UploadListItem;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DummyPayload;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import de.hswt.fi.ui.vaadin.views.components.processing.ScoreSettingsComponent;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Optional;

public abstract class AbstractProcessingFormComponent extends ContainerContentComponent {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingFormComponentGC.class);
    protected final ComponentFactory componentFactory;
    protected final I18N i18n;
    private final EventBus.ViewEventBus viewEventBus;
    protected ProcessingJob currentJob;
    protected Binder<ProcessingSettings> binder;
    private ScoreSettingsComponent scoreSettingsComponent;
    private CssLayout headerLayout;
    private CustomList<UploadListItem<ProcessingJob>> fileList;
    private Button searchButton;

    public AbstractProcessingFormComponent(EventBus.ViewEventBus viewEventBus, ComponentFactory componentFactory, I18N i18n) {
        this.viewEventBus = viewEventBus;
        this.componentFactory = componentFactory;
        this.i18n = i18n;
    }

    protected abstract Component getParameterRow();

    protected abstract void reuseSettings(ProcessingJob job);

    protected abstract TextField getIntensityTresholdField();

    @PostConstruct
    private void postConstruct() {

        LOGGER.debug("entering method postConstruct");

        setSizeFull();
        addStyleName(CustomValoTheme.PADDING_HALF);
        addStyleName(CustomValoTheme.CSS_LAYOUT_SCROLLBAR);

        binder = new Binder<>();

        initHeader();
        initFileRow();
        initScoreRow();
        addComponent(getParameterRow());


        binder.bind(scoreSettingsComponent, ProcessingSettings::getScoreSettings, ProcessingSettings::setScoreSettings);

        viewEventBus.subscribe(this);

        initFiles();
    }

    private void initHeader() {

        headerLayout = new CssLayout();
        searchButton = componentFactory.createButton(VaadinIcons.SEARCH,
                i18n.get(UIMessageKeys.SEARCH_BUTTON_CAPTION));
        searchButton.addClickListener(e -> executeSearch());
        headerLayout.addComponent(searchButton);

        Button clearButton = componentFactory.createButton(VaadinIcons.ERASER,
                i18n.get(UIMessageKeys.CLEAR_FIELDS_BUTTON_CAPTION), false);
        clearButton.addClickListener(e -> sendClearEvent());
        headerLayout.addComponent(clearButton);
    }

    @PreDestroy
    private void preDestroy() {
        viewEventBus.unsubscribe(this);
    }

    private void initScoreRow() {

        scoreSettingsComponent = new ScoreSettingsComponent(i18n);

        CollapsibleLayout scoreCollapseLayout = componentFactory.createCollapseableLayout(
                i18n.get(UIMessageKeys.PROCESSING_FORM_SCORING_HEADER), scoreSettingsComponent, false,
                false);

        // Add link to show score window
        scoreCollapseLayout.addHeaderButton(createScoreSettingsButton());

        CssLayout scoreLayout = componentFactory.createRowLayout(scoreCollapseLayout);
        scoreLayout.addStyleName(CustomValoTheme.MARGIN_HALF_RIGHT);
        scoreLayout.setWidth("100%");
        addComponent(scoreLayout);
    }

    private Button createScoreSettingsButton() {
        Button scoreSettings = componentFactory.createButton(
                i18n.get(UIMessageKeys.PROCESSING_FORM_SCORE_SETTINGS_BUTTON_CAPTION), false);
        scoreSettings.addStyleName(CustomValoTheme.MARGIN_NONE_RIGHT);

        scoreSettings.setIcon(VaadinIcons.COG);

        scoreSettings.addClickListener(l -> {

            if (currentJob != null) {
                LOGGER.debug("publish event inside addClickListener with payload {} in topic {}",
                        currentJob, EventBusTopics.OPEN_SCORE_WINDOW);
                viewEventBus.publish(EventBusTopics.OPEN_SCORE_WINDOW, this, SerializationUtils.clone(currentJob.getSettings()));
            } else {
                showNoFileSelectedNotification();
            }

        });
        return scoreSettings;
    }

    private void initFileList() {

        fileList = new CustomList<>("");

        fileList.addSelectionStyleName(CustomValoTheme.BACKGROUND_COLOR_GRADIENT_ALT3,
                CustomValoTheme.COLOR_WHITE);
        fileList.setHeight(LayoutConstants.TINY);
        fileList.setWidth("100%");
        fileList.addSelectionChangeListener(this::handleSelection);
        fileList.addClickListener(this::handleSelection);
        fileList.addDoubleClickListener(c -> executeSearch());
    }

    private void initFileRow() {

        initFileList();

        CssLayout fileLayout = new CssLayout();
        fileLayout.setWidth("100%");
        fileLayout.addStyleName(CustomValoTheme.MARGIN_BOTTOM);
        fileLayout.addStyleName(CustomValoTheme.MARGIN_TOP);
        fileLayout.addComponent(fileList);

        Button addFileButton = componentFactory
                .createButton(i18n.get(UIMessageKeys.PROCESSING_FORM_ADD_FILE_BUTTON_CAPTION));
        addFileButton.addStyleName(CustomValoTheme.FLOAT_RIGHT);
        addFileButton.addStyleName(CustomValoTheme.MARGIN_NONE_RIGHT);
        addFileButton.setIcon(VaadinIcons.UPLOAD);

        addFileButton.addClickListener(l -> {
            LOGGER.debug(
                    "publish event inside button click add file with topic {} and dummy payload",
                    EventBusTopics.OPEN_FILE_UPLOAD_WINDOW);
            viewEventBus.publish(EventBusTopics.OPEN_FILE_UPLOAD_WINDOW, this,
                    DummyPayload.INSTANCE);

            fileList.clear();
        });

        CollapsibleLayout fileCollapseLayout = componentFactory.createCollapseableLayout(
                i18n.get(UIMessageKeys.PROCESSING_FORM_UPLOAD_FILE_HEADER), fileLayout, false,
                false);

        fileCollapseLayout.addHeaderButton(addFileButton);


        fileCollapseLayout.setWidth("100%");
        fileCollapseLayout.addStyleName(CustomValoTheme.PADDING_HALF);


        addComponent(fileCollapseLayout);
    }

    private void initFiles() {
        LOGGER.debug("publish event inside initFiles with dummy payload");
        viewEventBus.publish(EventBusTopics.TARGET_HANDLER_RELOAD_FILE_DATA, this,
                DummyPayload.INSTANCE);
    }

    protected void executeSearch() {

        UploadListItem<ProcessingJob> listItem = fileList.getSelectedItem();

        if (listItem == null) {
            LOGGER.debug("item is null");
            showNoFileSelectedNotification();
            return;
        }

        ProcessingSettings settings = new ProcessingSettings();

        try {
            binder.writeBean(settings);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            return;
        }

        String intensityThreshold = getIntensityTresholdField().getValue();
        if (intensityThreshold != null && !intensityThreshold.isEmpty()) {
            settings.setIntensityThreshold(Double.parseDouble(intensityThreshold));
        }

        ProcessingJob job = new ProcessingJob(settings, listItem.getItemData().getFeatureSet());

        // Remove enter shortcut when search is executed
        searchButton.removeClickShortcut();

        LOGGER.debug("publish event inside executeSearch with job {}", settings);
        viewEventBus.publish(this, job);
    }

    private void showNoFileSelectedNotification() {
        new CustomNotification.Builder(
                i18n.get(UIMessageKeys.FILE_SEARCH_FORM_COMPONENT_NOTIFICATION_NO_FILE_SELECTED),
                "", Notification.Type.ASSISTIVE_NOTIFICATION).position(Position.MIDDLE_CENTER).build()
                .show(Page.getCurrent());
    }

    @SuppressWarnings("unused")
    @EventBusListenerMethod
    @EventBusListenerTopic(topic = EventBusTopics.SOURCE_HANDLER_DELETED_FILES_PROCESSING)
    private void executeClear(DummyPayload payload) {
        LOGGER.debug("entering executeClear with topic {}",
                EventBusTopics.SOURCE_HANDLER_DELETED_FILES_PROCESSING);

        currentJob = null;
        fileList.clear();

        scoreSettingsComponent.clear();
    }

    private void sendClearEvent() {
        LOGGER.debug("publish event inside sendClearEvent with topic {}",
                EventBusTopics.TARGET_HANDLER_DELETE_FILES);
        viewEventBus.publish(EventBusTopics.TARGET_HANDLER_DELETE_FILES, this,
                DummyPayload.INSTANCE);
    }

    @SuppressWarnings("unused")
    @EventBusListenerMethod
    @EventBusListenerTopic(topic = EventBusTopics.SOURCE_HANDLER_FILE_ADDED)
    private void handleFileAdded(ProcessingJob job) {
        LOGGER.debug("entering method handleFileAdded with data {} in topic {}", job,
                EventBusTopics.SOURCE_HANDLER_FILE_ADDED);

        UploadListItem<ProcessingJob> listItem = new UploadListItem<>(job, job.getName(),
                job.getDataSize(),
                i18n.get(UIMessageKeys.FILE_SEARCH_FORM_FILE_ENTRIES_DESCRIPTION), false);

        if (fileList.getAllItems().contains(listItem)) {
            return;
        }

        ProcessingSettings currentSettings = job.getSettings();

        if (currentJob != null) {

            reuseSettings(job);

            ScoreSettings lastScoreSettings = currentJob.getSettings().getScoreSettings();
            ScoreSettings currentScoreSettings = currentSettings.getScoreSettings();

            if (hasSameProcessesActivated(lastScoreSettings, currentScoreSettings)) {
                job.getSettings().setScoreSettings(lastScoreSettings);
            }
        }

        fileList.addItem(listItem);
    }

    private boolean hasSameProcessesActivated(ScoreSettings lastScoreSettings, ScoreSettings currentScoreSettings) {
        return lastScoreSettings.getMassScreeningState().isDataAvailable() == currentScoreSettings.getMassScreeningState().isDataAvailable()
                && lastScoreSettings.getRtiScreeningState().isDataAvailable() == currentScoreSettings.getRtiScreeningState().isDataAvailable()
                && lastScoreSettings.getMsmsState().isDataAvailable() == currentScoreSettings.getMsmsState()
                .isDataAvailable();
    }

    @SuppressWarnings("unused")
    @EventBusListenerMethod
    @EventBusListenerTopic(topic = EventBusTopics.SOURCE_HANDLER_SEARCH_SELECTED)
    private void handleProcessSelection(ProcessingJob data) {
        LOGGER.debug(
                "entering event bus listener handleProcessSelection with payload {} in topic {}",
                data, EventBusTopics.SOURCE_HANDLER_SEARCH_SELECTED);

        Optional<UploadListItem<ProcessingJob>> item = fileList.getAllItems().stream()
                .filter(i -> i.getItemData().getName().equals(data.getName())).findFirst();

        if (item.isPresent()) {
            item.get().setItemData(data);
            handleSelection(item.get());
        }
    }


    @SuppressWarnings("unused")
    @EventBusListenerMethod
    @EventBusListenerTopic(topic = EventBusTopics.PROCESSING_SCORE_SETTINGS_CHANGED)
    private void handleScoreSettingsSelection(ProcessingSettings settings) {
        LOGGER.debug(
                "entering event bus listener handleScoreSettingsSelection with payload {} in topic {}",
                settings, EventBusTopics.PROCESSING_SCORE_SETTINGS_CHANGED);
        fileList.getSelectedItem().getItemData().setSettings(settings);
        binder.readBean(settings);
    }

    private void handleSelection(UploadListItem<ProcessingJob> listItem) {

        ProcessingJob job = listItem.getItemData();

        if (job == null) {
            return;
        }

        binder.readBean(job.getSettings());

        currentJob = job;

        fileList.setSelected(listItem);

        LOGGER.debug("publish event inside handleSelection with payload {} in topic {}", currentJob,
                EventBusTopics.FILE_SELECTED);
        viewEventBus.publish(EventBusTopics.FILE_SELECTED, this, currentJob);
    }

    @Override
    public String getTitle() {
        return i18n.get(UIMessageKeys.PROCESSING_VIEW_SEARCH_TITLE);
    }

    @Override
    public Component getHeaderComponent() {
        return headerLayout;
    }
}
