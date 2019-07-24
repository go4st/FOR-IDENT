package de.hswt.fi.application;

import de.hswt.fi.common.spring.Profiles;
import de.hswt.fi.security.service.api.SecurityService;
import de.hswt.fi.security.service.model.Group;
import de.hswt.fi.security.service.model.Groups;
import de.hswt.fi.security.service.model.RegisteredUser;
import de.hswt.fi.security.service.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    private final SecurityService securityService;

    @Autowired
    public ApplicationStartupListener(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Environment environment = contextRefreshedEvent.getApplicationContext().getEnvironment();
        if (environment.acceptsProfiles(org.springframework.core.env.Profiles.of(Profiles.INITIALIZATION))) {
            LOGGER.debug("Running application in initialization mode!");
            initAdminAccount(securityService);
        }
    }

    private void initAdminAccount(SecurityService securityService) {

        LOGGER.debug("Creating groups and roles ...");

        Group groupAdmin = new Group();
        groupAdmin.setName(Groups.GROUP_ADMIN.name());
        groupAdmin.addAuthority(Role.ROLE_ADMIN);

        Group groupUser = new Group();
        groupUser.setName(Groups.GROUP_USER.name());
        groupUser.addAuthority(Role.ROLE_USER);

        Group groupScientist = new Group();
        groupScientist.setName(Groups.GROUP_SCIENTIST.name());
        groupScientist.addAuthority(Role.ROLE_SCIENTIST);

        securityService.createGroup(groupAdmin);
        securityService.createGroup(groupUser);
        securityService.createGroup(groupScientist);

        LOGGER.debug("Creating admin user ...");

		RegisteredUser adminUser = new RegisteredUser();
		adminUser.setUsername("admin");
		adminUser.setPassword("123456");
		adminUser.setFirstname("Administrator");
		adminUser.setLastname("Administrator");
		adminUser.setMail("admin@fi.de");
		adminUser.setOrganisation("");
        adminUser.addGroup(groupAdmin);

		securityService.createUser(adminUser);
		adminUser.setEnabled(true);
        securityService.updateUser(adminUser);

		LOGGER.debug("Everything done!");
    }
}