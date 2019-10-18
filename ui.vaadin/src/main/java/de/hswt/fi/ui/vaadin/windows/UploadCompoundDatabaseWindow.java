package de.hswt.fi.ui.vaadin.windows;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import de.hswt.fi.search.service.search.api.CompoundSearchService;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.UIConstants;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.components.CustomFileUpload;
import de.hswt.fi.ui.vaadin.components.CustomList;
import de.hswt.fi.ui.vaadin.components.UploadListItem;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.i18n.I18N;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

@SpringComponent
@PrototypeScope
public class UploadCompoundDatabaseWindow extends AbstractWindow {

	private static final long serialVersionUID = -7335967376753331255L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UploadCompoundDatabaseWindow.class);

	private final Path uploadCompoundDatabasePath;

	private CssLayout contentLayout;

	private CustomList<UploadListItem<Path>> fileList;

	private DateField dateField;

	private Label uploadCaption;

	private List<CompoundSearchService> compoundSearchServices;

	private ComboBox<CompoundSearchService> searchServiceComboBox;

	@Autowired
	protected UploadCompoundDatabaseWindow(ComponentFactory componentFactory, I18N i18n,
										   @Qualifier(UIConstants.TEMP_DIRECTORY_UPLOAD_COMPOUND_DATABASE_PATH)
												   Path uploadCompoundDatabasePath, List<CompoundSearchService> compoundSearchServices) {
		super(componentFactory, i18n, false);
		this.uploadCompoundDatabasePath = uploadCompoundDatabasePath;
		this.compoundSearchServices = compoundSearchServices;
	}

	@PostConstruct
	private void afterConstruct() {
		setWidth(LayoutConstants.HUGE);
	}

	@Override
	protected String getWindowCaption() {
		return i18n.get(UIMessageKeys.WINDOW_UPLOAD_COMPOUND_DATABASE_CAPTION);
	}

	@Override
	protected Component getContentComponent() {

		setCancelButtonVisible(false);

		contentLayout = new CssLayout();
		contentLayout.setSizeFull();
		contentLayout.addStyleName(CustomValoTheme.PADDING_HALF);

		Label descriptionLabel = new Label(i18n.get(UIMessageKeys.UPLOAD_COMPOUND_WINDOW_DATABASE_LABEL_TEXT));
		descriptionLabel.setWidth("100%");
		descriptionLabel.addStyleName(CustomValoTheme.MARGIN_BOTTOM);
		contentLayout.addComponent(descriptionLabel);

		initFileUploadRow();

		setCanFinish(false);
		
		return contentLayout;
	}

	private void initFileUploadRow() {

		CssLayout fileUploadLayout = new CssLayout();
		fileUploadLayout.setWidth("100%");
		fileUploadLayout.addStyleName(CustomValoTheme.MARGIN_RIGHT);

		searchServiceComboBox = new ComboBox<>();
		searchServiceComboBox.setItems(compoundSearchServices);
		searchServiceComboBox.setWidth("100%");
		searchServiceComboBox.setSelectedItem(compoundSearchServices.iterator().next());
		searchServiceComboBox.setEmptySelectionAllowed(false);
		searchServiceComboBox.setItemCaptionGenerator(CompoundSearchService::getDatasourceName);
		fileUploadLayout.addComponent(searchServiceComboBox);

		fileList = new CustomList<>("");
		fileList.addSelectionStyleName(CustomValoTheme.BACKGROUND_COLOR_GRADIENT_ALT3,
				CustomValoTheme.COLOR_WHITE);
		fileList.setHeight("7rem");
		fileList.setSelectItemsOnClick(false);

		CustomFileUpload multiFileUpload = new CustomFileUpload(uploadCompoundDatabasePath);
		multiFileUpload.setButtonCaption(i18n.get(UIMessageKeys.PROCESSING_FORM_COMPONENT_UPLOAD_BUTTON_CAPTION));
		multiFileUpload.addSucceededListener(this::handleFileUpload);
		fileUploadLayout.addComponent(multiFileUpload);

		uploadCaption = new Label();
		uploadCaption.setWidth("100%");
		fileUploadLayout.addComponent(uploadCaption);

		dateField = new DateField(i18n.get(UIMessageKeys.UPLOAD_COMPOUND_WINDOW_DATEFIELD_CAPTION));
		dateField.setWidth("100%");
		dateField.setValue(LocalDate.now());

		fileUploadLayout.addComponent(dateField);

		contentLayout.addComponent(fileUploadLayout);
	}

	private void handleFileUpload(Upload.SucceededEvent event) {

		Path path = ((CustomFileUpload) event.getUpload()).getReceiver().getPath();

		UploadListItem<Path> listItem = new UploadListItem<>(path, event.getFilename(), true);
		setCanFinish(true);
		listItem.addDeletionsListener(i -> {
			fileList.removeItem(i);
			if(fileList.getAllItems().isEmpty()) {
				setCanFinish(false);
			}
		});
		fileList.addItem(listItem);
		uploadCaption.setValue(listItem.getItemData().getFileName().toString());
	}

	@Override
	protected void handleOk() {
		// Nothing special to do here
	}

	@Override
	protected void handleCancel() {
		LOGGER.debug("Cancelling compound database upload");
		Path path = getPath();
		if (path != null) {
			LOGGER.debug("Deleting uploaded data file {} successful: {}", path.getFileName(), path.toFile().delete());
		}
	}

	public void clear() {
		fileList.clear();
		uploadCaption.setValue("");
	}

	public Path getPath() {
		if (!fileList.getAllItems().isEmpty()) {
			return fileList.getAllItems().get(0).getItemData();
		}
		return null;
	}

	public CompoundSearchService getCompoundSearchService() {
		return searchServiceComboBox.getSelectedItem().orElse(null);
	}

	public LocalDate getDate() {
		return dateField.getValue();
	}
}