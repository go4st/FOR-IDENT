package de.hswt.fi.ui.vaadin.eventbus.payloads;

public class LoginPayload {

	private String username;

	private String password;

	private String redirectUrl;

	public String getUsername() {
		return username;
	}

	public LoginPayload(String username, String password, String redirectUrl) {
		this.username = username;
		this.password = password;
		this.redirectUrl = redirectUrl;
	}

	public LoginPayload() {

	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

}
