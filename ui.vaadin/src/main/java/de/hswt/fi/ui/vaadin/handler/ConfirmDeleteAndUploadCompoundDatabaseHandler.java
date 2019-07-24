package de.hswt.fi.ui.vaadin.handler;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import de.hswt.fi.database.importer.compounds.api.StoffIdentImporter;
import de.hswt.fi.search.service.search.api.CompoundSearchService;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.CompoundDatabasePayload;
import de.hswt.fi.ui.vaadin.windows.AbstractWindow;
import de.hswt.fi.ui.vaadin.windows.ConfirmationDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;

import java.nio.file.Path;
import java.time.LocalDate;

@SpringComponent
@ViewScope
public class ConfirmDeleteAndUploadCompoundDatabaseHandler extends AbstractWindowHandler<ViewEventBus> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmDeleteAndUploadCompoundDatabaseHandler.class);

	private final I18N i18n;

	private final ConfirmationDialog confirmationDialog;

	private final StoffIdentImporter stoffIdentImporter;

	private Path path;

	private LocalDate date;

	private CompoundSearchService compoundSearchService;

	@Autowired
	public ConfirmDeleteAndUploadCompoundDatabaseHandler(I18N i18n, ConfirmationDialog confirmationDialog, StoffIdentImporter stoffIdentImporter) {
		this.i18n = i18n;
		this.confirmationDialog = confirmationDialog;
		this.stoffIdentImporter = stoffIdentImporter;
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_CONFIRM_UPLOAD_COMPOUND_DATABASE_WINDOW)
	private void handleShowUploadCompoundDatabaseWindow(CompoundDatabasePayload compoundDatabasePayload) {
		LOGGER.debug(
				"entering event bus listener handleShowUploadCompoundDatabaseWindow with topic {} and payload {}",
				EventBusTopics.TARGET_HANDLER_CONFIRM_UPLOAD_COMPOUND_DATABASE_WINDOW, compoundDatabasePayload);

		path = compoundDatabasePayload.getPath();
		date = compoundDatabasePayload.getDate();
		compoundSearchService = compoundDatabasePayload.getCompoundSearchService();

		if(path == null || date == null || compoundSearchService == null) {
			LOGGER.error("Path to file, date or entity manager of data set is not defined, cancelling");
			return;
		}

		confirmationDialog.initDialog(i18n.get(UIMessageKeys.CONFIRM_UPLOAD_COMPOUND_WINDOW_CAPTION),
				i18n.get(UIMessageKeys.CONFIRM_UPLOAD_COMPOUND_WINDOW_LABEL_TEXT));
		confirmationDialog.setWidth(LayoutConstants.REALLY_HUGE);
		UI.getCurrent().addWindow(confirmationDialog);
	}

	@Override
	public void windowClose(CloseEvent e) {
		if (AbstractWindow.CloseType.OK.equals(confirmationDialog.getCloseType())) {
			LOGGER.debug("Starting database update procedure on [{}]... this will take a while", compoundSearchService.getDatasourceName());
			stoffIdentImporter.importStoffIdentDataSet(path, date, compoundSearchService);
			LOGGER.debug("Deleting data file {} successful: {}", path.getFileName(), path.toFile().delete());
		}
	}

	@Override
	protected Window getWindow() {
		return confirmationDialog;
	}
}