package de.hswt.fi.ui.vaadin.windows;

import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.hswt.fi.ui.vaadin.*;
import de.hswt.fi.ui.vaadin.components.CustomFileUpload;
import de.hswt.fi.ui.vaadin.components.CustomList;
import de.hswt.fi.ui.vaadin.components.UploadListItem;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.i18n.I18N;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringComponent
@PrototypeScope
public class FileUploadWindow extends AbstractWindow {

    private static final long serialVersionUID = -7335967376753331255L;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadWindow.class);

    private static final int MAX_FILE_SIZE = 15 * 1024 * 1000; //15Mb

    private static final int MAX_FILE_COUNT = 15;

    private final Path processingTempDirectory;

    private final Path calibrationTempDirectory;

    private CssLayout contentLayout;

    private CustomList<UploadListItem<Path>> fileList;

    private CustomList<UploadListItem<Path>> rtiList;

    private ProgressBar fileProgressBar;

    private ProgressBar calibrationProgressBar;

    private CustomFileUpload fileUpload;

    private CustomFileUpload rtiFileUpload;

    private CssLayout errorLayout;

    @Autowired
    protected FileUploadWindow(ComponentFactory componentFactory, I18N i18n, ViewEventBus viewEventBus,
                               @Qualifier(UIConstants.TEMP_DIRECTORY_PROCESSING) Path processingTempDirectory,
                               @Qualifier(UIConstants.TEMP_DIRECTORY_CALIBRATION) Path calibrationTempDirectory) {
        super(componentFactory, i18n, false);
        this.processingTempDirectory = processingTempDirectory;
        this.calibrationTempDirectory = calibrationTempDirectory;

        setWidth(LayoutConstants.HUGE);
        viewEventBus.subscribe(this);
    }

    @Override
    protected String getWindowCaption() {
        return i18n.get(UIMessageKeys.WINDOW_UPLOAD_CAPTION);
    }

    @Override
    protected Component getContentComponent() {

        contentLayout = new CssLayout();
        contentLayout.setSizeFull();
        contentLayout.addStyleName(CustomValoTheme.PADDING_HALF);

        setCancelButtonVisible(false);

        initFileUploadRow();

        setCanFinish(false);

        return contentLayout;
    }

    private void initFileUploadRow() {

        fileList = createCustomList("7rem");

        CssLayout fileUploadLayout = new CssLayout();
        fileUploadLayout.setWidth("100%");
        fileUploadLayout.addStyleName(CustomValoTheme.MARGIN_RIGHT);

        Label uploadCaption = new Label(
                i18n.get(UIMessageKeys.PROCESSING_FORM_UPLOAD_WINDOW_UPLOAD_FIELD_CAPTION, MAX_FILE_COUNT));
        fileUploadLayout.addComponent(uploadCaption);

        fileUploadLayout.addComponent(fileList);

        fileProgressBar = createProgressBar();
        fileUploadLayout.addComponent(fileProgressBar);

        fileUpload = createCustomFileUpload(processingTempDirectory,
                i18n.get(UIMessageKeys.PROCESSING_FORM_COMPONENT_UPLOAD_BUTTON_CAPTION),
                fileProgressBar,
                this::handleFileUpload);
        fileUploadLayout.addComponent(fileUpload);

        Label rtiCalibrationMessage = new Label(
                i18n.get(UIMessageKeys.PROCESSING_FORM_COMPONENT_RTI_CALIBRATION_UPLOAD_MESSAGE));
        rtiCalibrationMessage.setWidth("100%");
        rtiCalibrationMessage.addStyleName(CustomValoTheme.MARGIN_VERTICAL);
        fileUploadLayout.addComponent(rtiCalibrationMessage);

        rtiList = new CustomList<>("");
        rtiList.addSelectionStyleName(CustomValoTheme.BACKGROUND_COLOR_GRADIENT_ALT3,
                CustomValoTheme.COLOR_WHITE);
        rtiList.setHeight("3rem");
        rtiList.setSelectItemsOnClick(false);

        fileUploadLayout.addComponent(rtiList);

        calibrationProgressBar = createProgressBar();
        fileUploadLayout.addComponent(calibrationProgressBar);

        errorLayout = new CssLayout();
        errorLayout.setWidth("100%");
        fileUploadLayout.addComponent(errorLayout);

        rtiFileUpload = createCustomFileUpload(calibrationTempDirectory,
                i18n.get(UIMessageKeys.PROCESSING_FORM_UPLOAD_WINDOW_UPLOAD_RTI_BUTTON_CAPTION),
                calibrationProgressBar,
                this::handleRtiCalibrationFileUpload);
        fileUploadLayout.addComponent(rtiFileUpload);

        contentLayout.addComponent(fileUploadLayout);
    }

    private CustomList<UploadListItem<Path>> createCustomList(String height) {
        CustomList<UploadListItem<Path>> customList = new CustomList<>("");
        customList.addSelectionStyleName(CustomValoTheme.BACKGROUND_COLOR_GRADIENT_ALT3,
                CustomValoTheme.COLOR_WHITE);
        customList.setHeight(height);
        customList.setSelectItemsOnClick(false);
        return customList;
    }

    private ProgressBar createProgressBar() {
        ProgressBar progressBar = new ProgressBar();
        progressBar.setVisible(false);
        progressBar.setWidth("100%");
        return progressBar;
    }

    private CustomFileUpload createCustomFileUpload(Path directory, String caption, ProgressBar progressBar, Upload.SucceededListener listener) {
        CustomFileUpload customFileUpload = new CustomFileUpload(directory);
        customFileUpload.addStyleName(CustomValoTheme.MARGIN_HALF_TOP);
        customFileUpload.addStyleName(CustomValoTheme.FLOAT_RIGHT);
        customFileUpload.setButtonCaption(caption);
        customFileUpload.addStartedListener(this::handleFileUploadStart);
        customFileUpload.addProgressListener((bytesLoaded, totalBytes) -> updateProgressBar(progressBar, totalBytes / bytesLoaded));
        customFileUpload.addFailedListener(this::showFileUploadError);
        customFileUpload.addSucceededListener(listener);
        return customFileUpload;
    }

    private void handleFileUploadStart(Upload.StartedEvent event) {
        if (event.getContentLength() > MAX_FILE_SIZE) {
            event.getUpload().interruptUpload();
            new CustomNotification.Builder(
                    i18n.get(UIMessageKeys.PROCESSING_FORM_UPLOAD_FILE_ERROR_CAPTION),
                    i18n.get(UIMessageKeys.PROCESSING_FORM_UPLOAD_FILE_ERROR_FILE_SIZE_EXCEEDED, String.valueOf(Math.round((float) MAX_FILE_SIZE / 1024 / 1000))),
                    Notification.Type.ERROR_MESSAGE).build().show(Page.getCurrent());
        }
    }

    private void updateProgressBar(ProgressBar bytesLoaded, long percentLoaded) {
        fileProgressBar.setValue(percentLoaded);
    }

    private void handleFileUpload(Upload.SucceededEvent event) {
        Path path = ((CustomFileUpload) event.getUpload()).getReceiver().getPath();
        addProcessingFile(path, event.getFilename());
    }

    private void addProcessingFile(Path path, String fileName) {
        UploadListItem<Path> listItem = new UploadListItem<>(path, fileName, true);
        setCanFinish(true);
        listItem.addDeletionsListener(i -> {
            try {
                fileList.removeItem(i);
                Files.delete(i.getItemData());
                if (fileList.getAllItems().isEmpty()) {
                    setCanFinish(false);
                }
                fileUpload.setVisible(fileList.getAllItems().size() < MAX_FILE_COUNT);
            } catch (IOException e) {
                LOGGER.error("An error occured: ", e);
                showFileUploadError(null);
            }
        });
        fileList.addItem(listItem);


        fileUpload.setVisible(fileList.getAllItems().size() < MAX_FILE_COUNT);
    }

    @SuppressWarnings("unused")
    private void handleRtiCalibrationFileUpload(Upload.SucceededEvent event) {

        if (!rtiList.getAllItems().isEmpty()) {
            new CustomNotification.Builder(
                    i18n.get(UIMessageKeys.PROCESSING_FORM_UPLOAD_FILE_ERROR_CAPTION),
                    i18n.get(UIMessageKeys.PROCESSING_FORM_UPLOAD_FILE_ERROR_CALIBRATION_FILE_EXISTS),
                    Notification.Type.ERROR_MESSAGE).build().show(Page.getCurrent());
            return;
        }

        Path path = ((CustomFileUpload) event.getUpload()).getReceiver().getPath();
        addCalibrationFile(path, event.getFilename());
    }

    private void addCalibrationFile(Path path, String filename) {
        UploadListItem<Path> listItem = new UploadListItem<>(path, filename, true);
        listItem.addDeletionsListener(i -> {
            try {
                Files.delete(i.getItemData());
                rtiList.removeItem(i);
            } catch (IOException e) {
                LOGGER.error("An error occured: ", e);
                showFileUploadError(null);
                rtiList.clear();
            } finally {
                rtiFileUpload.setVisible(rtiList.getAllItems().size() < 1);
            }
        });
        rtiList.addItem(listItem);

        rtiFileUpload.setVisible(rtiList.getAllItems().size() < 1);
    }

    @SuppressWarnings("unused")
    private void showFileUploadError(Upload.FailedEvent event) {
        new CustomNotification.Builder(
                i18n.get(UIMessageKeys.PROCESSING_FORM_UPLOAD_FILE_ERROR_CAPTION),
                i18n.get(UIMessageKeys.PROCESSING_FORM_UPLOAD_FILE_ERROR_UPLOAD_FAILED),
                Notification.Type.ERROR_MESSAGE).build().show(Page.getCurrent());

        errorLayout.removeAllComponents();
        Label errorLabel = new Label(i18n.get(UIMessageKeys.PROCESSING_FORM_UPLOAD_FILE_ERROR_UPLOAD_FAILED));
        errorLabel.addStyleName(CustomValoTheme.COLOR_RED);
        errorLayout.addComponent(errorLabel);
    }

    public List<Path> getUploadedFiles() {
        return fileList.getAllItems().stream().map(UploadListItem::getItemData).collect(Collectors.toList());
    }

    @Override
    protected void handleOk() {
        // Nothing special to do here
    }

    public Path getRtiCalibrationFile() {
        return rtiList.getCurrentItem() != null ? rtiList.getCurrentItem().getItemData() : null;
    }

    public void initialize() {
        fileList.clear();
        rtiList.clear();

        fileUpload.setComponentError(null);

        errorLayout.removeAllComponents();

        try (Stream<Path> processingFiles = Files.list(processingTempDirectory)) {
            processingFiles.forEach(file -> addProcessingFile(file, file.getFileName().toString()));
        } catch (IOException e) {
            LOGGER.error("An error occured: ", e);
            showFileUploadError(null);
        }

        try (Stream<Path> calibrationFiles = Files.list(calibrationTempDirectory)) {
            calibrationFiles.forEach(file -> addCalibrationFile(file, file.getFileName().toString()));
        } catch (IOException e) {
            LOGGER.error("An error occured: ", e);
            showFileUploadError(null);
        }
    }
}