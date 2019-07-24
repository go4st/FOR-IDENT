package de.hswt.fi.ui.vaadin.handler.security;

import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import de.hswt.fi.security.service.api.SecurityService;
import de.hswt.fi.security.service.model.RegisteredUser;
import de.hswt.fi.ui.vaadin.CustomNotification;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.handler.AbstractWindowHandler;
import de.hswt.fi.ui.vaadin.windows.AbstractWindow;
import de.hswt.fi.ui.vaadin.windows.ConfirmationDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;

@SpringComponent
@ViewScope
public class DeleteUserHandler extends AbstractWindowHandler<ViewEventBus> {

	private static final long serialVersionUID = 3033061802993063270L;

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUserHandler.class);

	@Autowired
	private ConfirmationDialog<RegisteredUser> confirmationDialog;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private I18N i18n;

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_USER_DELETE)
	private void handleEditUser(RegisteredUser user) {
		LOGGER.debug("entering event bus listener handleUserDelete with payload {} in topic {}",
				user, EventBusTopics.TARGET_HANDLER_USER_DELETE);

		if (user == null) {
			LOGGER.debug("user is null - returning");
			return;
		}

		confirmationDialog.initDialog(i18n.get(UIMessageKeys.DELETE_USER_DIALOG_CAPTION),
				i18n.get(UIMessageKeys.DELETE_USER_DIALOG_DESCRIPTION,
						new Object[] { user.getFirstname() + " " + user.getLastname() }));
		confirmationDialog.setDataObject(user);

		UI.getCurrent().addWindow(confirmationDialog);
	}

	@Override
	public void windowClose(CloseEvent e) {
		if (AbstractWindow.CloseType.OK.equals(confirmationDialog.getCloseType())) {
			deleteUser(confirmationDialog.getDataObject());
		}
	}

	private void deleteUser(RegisteredUser user) {
		LOGGER.debug("entering method deleteUser");
		if (user == null) {
			LOGGER.debug("user is null - returning");
			return;
		}

		if (!securityService.deleteUser(user)) {
			showErrorNotifcation();
			return;
		}
		showSuccessNotification();

		LOGGER.debug("publish event inside deleteUser with topic {}",
				EventBusTopics.SOURCE_HANDLER_USER_DELETE);
		eventBus.publish(EventBusTopics.SOURCE_HANDLER_USER_DELETE, this, user);
	}

	private void showSuccessNotification() {
		new CustomNotification.Builder(
				i18n.get(UIMessageKeys.DELETE_USER_SUCCESS_NOTIFICATION_CAPTION), "",
				Type.HUMANIZED_MESSAGE).styleName(ValoTheme.NOTIFICATION_SUCCESS).build()
						.show(Page.getCurrent());
	}

	private void showErrorNotifcation() {
		new CustomNotification.Builder(
				i18n.get(UIMessageKeys.DELETE_USER_ERROR_NOTIFICATION_CAPTION),
				i18n.get(UIMessageKeys.DELETE_USER_ERROR_NOTIFICATION_DESCRIPTION),
				Type.ERROR_MESSAGE).build().show(Page.getCurrent());
	}

	@Override
	protected Window getWindow() {
		return confirmationDialog;
	}
}
