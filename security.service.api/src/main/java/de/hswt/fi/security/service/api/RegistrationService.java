package de.hswt.fi.security.service.api;


import de.hswt.fi.security.service.model.RegisteredUser;

public interface RegistrationService {

	boolean registerUser(RegisteredUser user);

	boolean isRegistrationEnabled();

}
