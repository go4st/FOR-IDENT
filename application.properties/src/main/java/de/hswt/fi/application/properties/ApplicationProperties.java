package de.hswt.fi.application.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "de.hswt.fi")
public class ApplicationProperties {

	private List<String> contactAdresses;

	private String fromEmailAddress;

	@NestedConfigurationProperty
	private SecurityProperties security;

	@NestedConfigurationProperty
	private UIProperties ui;

	public UIProperties getUi() {
		return ui;
	}

	public void setUi(UIProperties ui) {
		this.ui = ui;
	}

	public SecurityProperties getSecurity() {
		return security;
	}

	public void setSecurity(SecurityProperties security) {
		this.security = security;
	}

	public List<String> getContactAdresses() {
		return contactAdresses;
	}

	public void setContactAdresses(List<String> contactAdresses) {
		this.contactAdresses = contactAdresses;
	}

	public String getFromEmailAddress() {
		return fromEmailAddress;
	}

	public void setFromEmailAddress(String fromEmailAddress) {
		this.fromEmailAddress = fromEmailAddress;
	}

	@Override
	public String toString() {
		return "ApplicationProperties [contactAdresses=" + contactAdresses + ", fromEmailAddress="
				+ fromEmailAddress + ", security=" + security + ", ui=" + ui + "]";
	}

}
