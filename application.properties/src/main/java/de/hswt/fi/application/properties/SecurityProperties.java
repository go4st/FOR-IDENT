package de.hswt.fi.application.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "de.hswt.fi.security")
public class SecurityProperties {

	private String logoutUrl;

	private String redirectUrl;

	private int passwordLength;

	private String defaultUsername;

	private String defaultPassword;

	private boolean registrationEnabled;

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public int getPasswordLength() {
		return passwordLength;
	}

	public void setPasswordLength(int passwordLength) {
		this.passwordLength = passwordLength;
	}

	public String getDefaultPassword() {
		return defaultPassword;
	}

	public void setDefaultPassword(String defaultPassword) {
		this.defaultPassword = defaultPassword;
	}

	public String getDefaultUsername() {
		return defaultUsername;
	}

	public void setDefaultUsername(String defaultUsername) {
		this.defaultUsername = defaultUsername;
	}

	public boolean isRegistrationEnabled() {
		return registrationEnabled;
	}

	public void setRegistrationEnabled(boolean registrationEnabled) {
		this.registrationEnabled = registrationEnabled;
	}

}
