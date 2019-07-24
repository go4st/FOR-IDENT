package de.hswt.fi.mail.service.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

public class RequestUserAccountNotifyAdminTemplate {

	private String username;

	private String caption;

	public RequestUserAccountNotifyAdminTemplate(String username, String caption) {
		setUsername(username);
		setCaption(caption);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		checkArgument(!isNullOrEmpty(username), "Username must not be null or empty");
		this.username = username;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		checkArgument(!isNullOrEmpty(caption), "Caption must not be null or empty");
		this.caption = caption;
	}

}
