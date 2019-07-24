package de.hswt.fi.ui.vaadin.eventbus;

import de.hswt.fi.ui.vaadin.eventbus.payloads.UserPropertyPayload;
import de.hswt.fi.ui.vaadin.handler.security.LogoutHandler;
import de.hswt.fi.ui.vaadin.handler.security.RequestUserAccountHandler;
import de.hswt.fi.ui.vaadin.handler.security.ResetPasswordHandler;

/**
 * The class defines string constants which defines topics used as source and
 * targets for events published and handled through the vaadin4spring event bus
 * addon.
 *
 * Each event can be published within a specific topic. Each listener which
 * listens in that topic and has the corresponding payload will be called from
 * the event bus.
 *
 * The topics are defined through strings, which must be provided while
 * publishing an event and at the desired listeners.
 *
 * With topics it's possible to distinguish events with the same payload but
 * different purpose.
 *
 * @author Marco Luthardt
 * @author Tobias Placht
 * @author August Gilg
 */
public class EventBusTopics {

	/**
	 * Defines a topic for container changes within a VAADIN view with a
	 * container payload as target (e.g. a the current search container is
	 * changed, by selecting another one in the search history).
	 */
	public static final String TARGET_HANDLER_SELECT_CONTAINER = "de.hswt.fi.ui.vaadin.view.container.select";

	/**
	 * Defines a topic for entry selection changes within a VAADIN view with a
	 * generic entry payload as target (e.g. one entry from the search results
	 * is selected, the search history adds this entry to the selection
	 * history).
	 */
	public static final String TARGET_HANDLER_SELECT_ENTRY = "de.hswt.fi.ui.vaadin.view.entry.select";

	/**
	 * Defines a topic to trigger the download for a single generic result entry
	 * as payload as target.
	 */
	public static final String TARGET_HANDLER_REQUEST_RESULT_DOWNLOAD = "de.hswt.fi.ui.vaadin.view.download.result.request";

	/**
	 * Defines a topic to trigger the download for a single generic result entry
	 * as payload as target.
	 */
	public static final String TARGET_HANDLER_REQUEST_NON_RESULT_DOWNLOAD = "de.hswt.fi.ui.vaadin.view.download.nonresult.request";

	/**
	 * Defines a topic to trigger the download for the complete sheet with result entry
	 * as payload as target.
	 */
	public static final String TARGET_HANDLER_REQUEST_COMPLETE_DOWNLOAD = "de.hswt.fi.ui.vaadin.view.download.complete.request";

	/**
	 * Defines a topic to trigger the download for a single generic rti result
	 * entry as payload as target.
	 */
	public static final String TARGET_HANDLER_REQUEST_RTI_DATA_DOWNLOAD = "de.hswt.fi.ui.vaadin.view.download.data.rti.request";

	
	/**
	 * Defines a topic to trigger the report handler with a single generic entry
	 * payload to report as target.
	 */
	public static final String TARGET_HANDLER_REPORT_TO_STAFF = "de.hswt.fi.ui.vaadin.view.report.staff";

	/**
	 * Defines a topic as target to clear the search forms.
	 */
	public static final String TARGET_HANDLER_DELETE_FILES = "de.hswt.fi.ui.vaadin.view.delete.files";

	/**
	 * Defines a topic which has a view controller as target to clear the search
	 * history.
	 */
	public static final String TARGET_HANDLER_CLEAR_HISTORY = "de.hswt.fi.ui.vaadin.view.clear.history";

	/**
	 * Defines a topic to reload a submitted data file.
	 */
	public static final String TARGET_HANDLER_RELOAD_FILE_DATA = "de.hswt.fi.ui.vaadin.view.reload.file.data";

	/**
	 * Defines a topic which has a handler as source to clear the file
	 * search history with a dummy payload.
	 */
	public static final String SOURCE_HANDLER_CLEARED_SEARCH_HISTORY = "de.hswt.fi.ui.vaadin.view.cleared.search.history";

	/**
	 * Defines a topic which has a handler as source to clear the file
	 * search history with a dummy payload.
	 */
	public static final String SOURCE_HANDLER_CLEARED_FILE_SEARCH_HISTORY = "de.hswt.fi.ui.vaadin.view.cleared.search.history.file";

	/**
	 * Defines a topic which has a view controller as source to clear the file
	 * search history.
	 */
	public static final String SOURCE_HANDLER_CLEARED_RTI_SEARCH_HISTORY = "de.hswt.fi.ui.vaadin.view.cleared.search.history.rti";

	public static final String SOURCE_HANDLER_CLEARED_PROCESSING_SEARCH_HISTORY = "de.hswt.fi.ui.vaadin.view.cleared.search.history.processing";

	/**
	 * Defines a topic to signal that the temporary files have been deleted.
	 */
	public static final String SOURCE_HANDLER_DELETED_FILES = "de.hswt.fi.ui.vaadin.view.deleted.files";

	/**
	 * Defines a topic which has a view controller as source to delete the
	 * temporary RTI files.
	 */
	public static final String SOURCE_HANDLER_DELETED_FILES_RTI = "de.hswt.fi.ui.vaadin.view.deleted.files.rti";

	public static final String SOURCE_HANDLER_DELETED_FILES_PROCESSING = "de.hswt.fi.ui.vaadin.view.deleted.files.processing";

	/**
	 * Defines a topic which has a view controller with a generic controller
	 * container payload as source.
	 */
	public static final String SOURCE_HANDLER_CONTAINER_CHANGED = "de.hswt.fi.ui.vaadin.view.container.changed";

	/**
	 * Defines a topic which has a view controller with a generic entry payload
	 * as source.
	 */
	public static final String SOURCE_HANDLER_ENTRY_SELECTED = "de.hswt.fi.ui.vaadin.view.entry.selected";

	/**
	 * Defines a topic which has a controller with a search paramter payload as
	 * source. The purpose is notify the search field components that search
	 * parameter were changed.
	 */
	public static final String SOURCE_HANDLER_SEARCH_SELECTED = "de.hswt.fi.ui.vaadin.view.search.selected";

	public static final String FILE_SELECTED = "de.hswt.fi.ui.vaadin.view.file.selected";

	public static final String SOURCE_HANDLER_FILE_ADDED = "de.hswt.fi.ui.vaadin.view.file.added";

	public static final String SOURCE_HANDLER_SOURCE_LISTS_SELECTED = "de.hswt.fi.ui.vaadin.view.sourcelists.selected";

	/**
	 * Defines a topic which has a handler with a registered user payload as
	 * target. The purpose is to edit the payload user.
	 */
	public static final String TARGET_HANDLER_USER_EDIT = "de.hswt.fi.ui.vaadin.handler.user.edit.target";

	/**
	 * Defines a topic which has a handler with no payload as
	 * target. The purpose is to show the compound database upload window.
	 */
	public static final String TARGET_HANDLER_SHOW_UPLOAD_COMPOUND_DATABASE_WINDOW = "de.hswt.fi.ui.vaadin.handler.show.upload.compound.database.window.target";

	/**
	 * Defines a topic which has a handler with no payload as
	 * target. The purpose is to show the refresh tp database confirmation window.
	 */
	public static final String TARGET_HANDLER_SHOW_REFRESH_TP_DATABASE_WINDOW = "de.hswt.fi.ui.vaadin.handler.show.refresh.tp.database.window.target";

	/**
	 * Defines a topic which has a handler with no payload as
	 * target. The purpose is to confirm the compound database upload procedure.
	 */
	public static final String TARGET_HANDLER_CONFIRM_UPLOAD_COMPOUND_DATABASE_WINDOW = "de.hswt.fi.ui.vaadin.handler.confirm.upload.compound.database.window.target";

	/**
	 * Defines a topic which has a handler with a new user payload as target.
	 * The purpose is to gather data for payload user.
	 */
	public static final String TARGET_HANDLER_USER_ADD = "de.hswt.fi.ui.vaadin.handler.user.add.target";

	/**
	 * Defines a topic which has a handler with a registered user payload as
	 * target. The purpose is to delete the payload user.
	 */
	public static final String TARGET_HANDLER_USER_DELETE = "de.hswt.fi.ui.vaadin.handler.user.delete.target";

	/**
	 * Defines a topic which has a handler with a registered user payload as
	 * target. The purpose is to change the password of the payload user.
	 */
	public static final String TARGET_HANDLER_USER_PASSWORD = "de.hswt.fi.ui.vaadin.handler.user.password.target";

	/**
	 * Defines a topic which has a handler with a registered user payload as
	 * source. The purpose is to notify that a user was changed.
	 */
	public static final String SOURCE_HANDLER_USER_CHANGED = "de.hswt.fi.ui.vaadin.handler.user.changed.source";

	/**
	 * Defines a topic which has a handler with a registered user payload as
	 * source. The purpose is to notify that a user was added.
	 */
	public static final String SOURCE_HANDLER_USER_ADDED = "de.hswt.fi.ui.vaadin.handler.user.added.source";

	/**
	 * Defines a topic which has a handler with a registered user payload as
	 * source. The purpose is notify that a user is deleted.
	 */

	public static final String SOURCE_HANDLER_USER_DELETE = "de.hswt.fi.ui.vaadin.handler.user.delete.source";

	/**
	 * Defines the Topic which has the UserPropertiesHandler with a
	 * {@link UserPropertyPayload} as a target. The Payload contains a String
	 * Key and a Boolean value.
	 */
	public static final String TARGET_HANDLER_USER_PROPERTIES_BOOLEAN = "de.hswt.fi.ui.vaadin.handler.userproperties.boolean.target";

	/**
	 * Defines the Topic which has the UserPropertiesHandler with a
	 * {@link UserPropertyPayload} as a target. The Payload contains a String
	 * Key and a String value.
	 */
	public static final String TARGET_HANDLER_USER_PROPERTIES_STRING = "de.hswt.fi.ui.vaadin.handler.userproperties.string.target";

	/**
	 * Defines the Topic which has the UserPropertiesHandler with a Boolean
	 * payload as a target. The Payload contains a String Key and a Integer
	 * value.
	 */
	public static final String TARGET_HANDLER_USER_PROPERTIES_INTEGER = "de.hswt.fi.ui.vaadin.handler.userproperties.integer.target";

	/**
	 * Defines the Topic which has the UserPropertiesHandler with a Boolean
	 * payload as a target. The Payload contains a String Key and a Double
	 * value.
	 */
	public static final String TARGET_HANDLER_USER_PROPERTIES_DOUBLE = "de.hswt.fi.ui.vaadin.handler.userproperties.double.target";

	/**
	 * Defines the Topic which has the {@link RequestUserAccountHandler} as a
	 * target
	 */
	public static final String TARGET_HANDLER_REQUEST_USER_ACCOUNT = "de.hswt.fi.ui.vaadin.handler.requestuser.target";

	/**
	 * Defines the Topic which has the {@link ResetPasswordHandler} as a target
	 */
	public static final String TARGET_HANDLER_RESET_PASSWORD = "de.hswt.fi.ui.vaadin.handler.resetpassword.target";

	/**
	 * Defines the Topic which has the {@link ResetPasswordHandler} as a target
	 */
	public static final String TARGET_HANDLER_SPREADSHEET = "de.hswt.fi.ui.vaadin.handler.spreadsheet.target";

	/**
	 * Defines the Topic which has the {@link LogoutHandler} as a target
	 */
	public static final String TARGET_HANDLER_LOGOUT = "de.hswt.fi.ui.vaadin.handler.logout.target";

	/**
	 * Defines a topic for all listeners which are interested in getting
	 * notifies when the header visibility should be changed.
	 */
	public static final String TARGET_HANDLER_HEADER_SWITCH = "de.hswt.fi.ui.vaadin.header.switch";

	/**
	 * Defines a topic for all listeners which are interested in getting
	 * notifies when the header visibility was changed.
	 */
	public static final String TARGET_HANDLER_HEADER_SWITCH_UPDATE = "de.hswt.fi.ui.vaadin.header.switch.update";

	/**
	 * Defines a topic for all listeners which are interested in getting
	 * notified when the RTI Selection has changed.
	 */
	public static final String SOURCE_CONTROLLER_SELECTION_RTI = "de.hswt.fi.ui.vaadin.view.controller.selection.rti.source";

	/**
	 * Defines a topic for all listeners which are interested in getting
	 * notified when a user logs in
	 */
	public static final String TARGET_HANDLER_LOGIN = "de.hswt.fi.ui.vaadin.handler.login.target";

	/**
	 * Defines a topic for all listeners which are interested in getting
	 * notified when a user login failed
	 */
	public static final String SOURCE_HANDLER_LOGIN_FAILED = "de.hswt.fi.ui.vaadin.handler.login.failed";

	/**
	 * Defines a topic for all listeners which are interested in getting
	 * notified when a user changes his settings
	 */
	public static final String TARGET_HANDLER_EDIT_USER_SETTINGS = "de.hswt.fi.ui.vaadin.handler.edit.user.target";

	/**
	 * Defines a topic with a single generic container component as payload to
	 * notify the processing view that a tab change occured.
	 */
	public static final String PROCESSING_RESULTS_TAB_CHANGED = "de.hswt.fi.ui.vaadin.handler.processing.results.tab.changed";

	/**
	 * Defines a topic with a single generic container component as payload to
	 * notify the processing view that a tab change occured.
	 */
	public static final String PROCESSING_DATA_MSMS_ENTRY_SELECTED = "de.hswt.fi.ui.vaadin.handler.processing.data.msms.entry.selected";

	/**
	 * Defines a topic with a processing job payload to
	 * show the score settings window.
	 */
	public static final String OPEN_SCORE_WINDOW = "de.hswt.fi.ui.vaadin.handler.score.window.open";

	/**
	 * Defines a topic with a processing job payload to
	 * update the edited score values.
	 */
	public static final String PROCESSING_SCORE_SETTINGS_CHANGED = "de.hswt.fi.ui.vaadin.handler.score.window.settings.changed";

	/**
	 * Defines a topic with a dummy payload to
	 * notify the score window that a weight has changed.
	 */
	public static final String SCORE_SETTINGS_WEIGHT_CHANGED = "de.hswt.fi.ui.vaadin.handler.score.window.settings.weight.changed";
	
	/**
	 * Defines a topic with a dummy payload to
	 * show the summary settings window.
	 */
	public static final String OPEN_SUMMARY_WINDOW = "de.hswt.fi.ui.vaadin.handler.summary.window.open";

	/**
	 * Defines a topic with a dummy payload to
	 * show the file upload window.
	 */
	public static final String OPEN_FILE_UPLOAD_WINDOW = "de.hswt.fi.ui.vaadin.handler.file.upload.window.open";

	/**
	 * Defines a topic with a file to
	 * add a RTI calibration file.
	 */
	public static final String SOURCE_HANDLER_RTI_CALIBRATION_FILE_ADDED = "de.hswt.fi.ui.vaadin.handler.rti.calibration.file.added";

	/**
	 * Defines a topic with dummy payload
	 * to notify ProcessingImportHandler that
	 * the RTI caliration file was deleted.
	 */
	public static final String FILE_UPLOAD_WINDOW_DELETE_RTI_CALIBRATION_FILE = "de.hswt.fi.ui.vaadin.file.upload.window.delete.rti.calibration.file";
}
