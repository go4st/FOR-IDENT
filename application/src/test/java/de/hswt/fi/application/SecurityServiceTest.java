package de.hswt.fi.application;

import de.hswt.fi.security.service.api.SecurityService;
import de.hswt.fi.security.service.model.RegisteredUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class SecurityServiceTest extends AbstractTest {

	@Autowired
	private SecurityService securityService;


	@Test
	@WithUserDetails("admin")
	public void getCurrentUserWithUserDetails() {
		RegisteredUser registeredUser = securityService.getCurrentUser();
		assertTrue(registeredUser.getFirstname().equals("Administrator"));
		assertTrue(registeredUser.getMail().equals("admin@fi.de"));
	}

	@Test
	@WithUserDetails("admin")
	public void createAndDeleteUserWithUserDetails() {
		RegisteredUser testUser = getTestUser();
		int numberOfUser = securityService.findAllUsers().size();
		testUser = securityService.createUser(testUser);
		assertTrue(numberOfUser + 1 == securityService.findAllUsers().size());
		assertTrue(testUser.getId() != null);
		assertTrue(securityService.deleteUser(testUser));
		assertTrue(numberOfUser == securityService.findAllUsers().size());
	}

	private RegisteredUser getTestUser() {
		RegisteredUser testUser = new RegisteredUser();
		testUser.setUsername(UUID.randomUUID().toString());
		testUser.setMail(UUID.randomUUID().toString() + "@test.de");
		testUser.setPassword("123456");
		return testUser;
	}

	@Override
	public void setup() {

	}

	@Override
	public void cleanup() {
	}
}
