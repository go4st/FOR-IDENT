package de.hswt.fi.ui.vaadin.handler;

import com.vaadin.server.Page;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import de.hswt.fi.application.properties.ApplicationProperties;
import de.hswt.fi.mail.service.MailService;
import de.hswt.fi.mail.service.model.MailTemplate;
import de.hswt.fi.security.service.api.SecurityService;
import de.hswt.fi.security.service.model.RegisteredUser;
import de.hswt.fi.ui.vaadin.ReportUtil;
import de.hswt.fi.ui.vaadin.windows.AbstractWindow;
import de.hswt.fi.ui.vaadin.windows.ReportWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.vaadin.spring.events.EventBus.ViewEventBus;

import java.util.Map;

public abstract class AbstractReportHandler extends AbstractWindowHandler<ViewEventBus> {

	private static final long serialVersionUID = 9032183532258871196L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractReportHandler.class);

	private ReportWindow reportWindow;

	private SecurityService securityService;

	private MailService mailservice;

	private ApplicationProperties applicationProperties;

	protected ReportUtil reportUtil;
	
	protected String errorNumber;

	protected abstract MailTemplate getMailTemplate();

	protected abstract Map<String, Object> getModel();

	protected abstract String getSubject();

	protected void createAndOpenReportWindow(String bugDescription) {
		if (bugDescription == null || bugDescription.isEmpty()) {
			return;
		}

		reportWindow.clear();
		errorNumber = reportUtil.getErrorNumber();
		RegisteredUser user = securityService.getCurrentUser();
		reportWindow.init(bugDescription, user);

		UI.getCurrent().addWindow(reportWindow);
	}

	private void sendReport() {
		try {
			applicationProperties.getContactAdresses().forEach(
					a -> mailservice.sendTemplateMail(a, getSubject(), getMailTemplate().getLocation(), getModel()));
		} catch (MailException e) {
			LOGGER.error("An error occured", e);
			showErrorNotification();
			return;
		}

		showSuccessNotification();
	}

	private void showErrorNotification() {
		reportUtil.createErrorNotification().show(Page.getCurrent());
	}

	private void showSuccessNotification() {
		reportUtil.createSuccessNotification().show(Page.getCurrent());
	}

	@Override
	public void windowClose(CloseEvent e) {
		if (AbstractWindow.CloseType.OK.equals(reportWindow.getCloseType())) {
			sendReport();
		}
	}

	@Override
	protected Window getWindow() {
		return reportWindow;
	}

	@Autowired
	public void setReportWindow(ReportWindow reportWindow) {
		this.reportWindow = reportWindow;
	}

	@Autowired
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	@Autowired
	public void setMailservice(MailService mailservice) {
		this.mailservice = mailservice;
	}

	@Autowired
	public void setApplicationProperties(ApplicationProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	@Autowired
	public void setReportUtil(ReportUtil reportUtil) {
		this.reportUtil = reportUtil;
	}
}
