package de.hswt.fi.mail.service.model;

public enum MailTemplate {

	SEARCH_HISTORY_REPORT("search_history_report.ftl"),
	PROCESSING_HISTORY_REPORT("processing_history_report.ftl"),
	ENTRY_REPORT("entry_report.ftl"),
	USER_ACCOUNT_REQUEST_NOTIFY_ADMIN("user_account_request_notify_admin.ftl"),
	USER_ACCOUNT_REQUEST_NOTIFY_USER("user_account_request_notify_user.ftl");

	MailTemplate(String templateLocation) {
		this.templateLocation = templateLocation;
	}

	private final String templateLocation;

	public String getLocation() {
		return templateLocation;
	}

}
