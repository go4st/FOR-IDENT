package de.hswt.fi.security.service.vaadin;

import de.hswt.fi.application.properties.ApplicationProperties;
import de.hswt.fi.security.service.api.RegistrationService;
import de.hswt.fi.security.service.api.SecurityService;
import de.hswt.fi.security.service.model.RegisteredUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class DefaultRegistrationService implements RegistrationService {

	private SecurityService securityService;

	private ApplicationProperties applicationProperties;

	@Autowired
	public DefaultRegistrationService(SecurityService securityService, ApplicationProperties applicationProperties) {
		this.securityService = securityService;
		this.applicationProperties = applicationProperties;
	}

	@Override
	public boolean registerUser(RegisteredUser user) {
		return securityService.requestUserAccount(user);
	}

	@Override
	public boolean isRegistrationEnabled() {
		return applicationProperties.getSecurity().isRegistrationEnabled();
	}
}