package de.hswt.fi.application;

/**
 * Created by August Gilg on 31.10.2016.
 */

import de.hswt.fi.database.importer.compounds.api.StoffIdentImporter;
import de.hswt.fi.search.service.search.api.CompoundSearchService;
import de.hswt.fi.search.service.search.compound.config.CompoundDatabaseConfiguration;
import de.hswt.fi.security.service.api.SecurityService;
import de.hswt.fi.security.service.model.Group;
import de.hswt.fi.security.service.model.Groups;
import de.hswt.fi.security.service.model.RegisteredUser;
import de.hswt.fi.security.service.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@SpringBootApplication(scanBasePackages = "de.hswt.fi")
public class TestApplicationContext implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestApplicationContext.class);

    private static final String SI_TEST_COMPOUND_DATA = "SI_Content_Test_25.xlsx";

    private static final String PI_TEST_COMPOUND_DATA = "PI_Content_Test_25.xlsx";

    @Autowired
    private SecurityService securityService;

    @Autowired
    private List<CompoundSearchService> compoundSearchServices;

    @Autowired
    private StoffIdentImporter stoffIdentImporter;

    public static void main(String[] args) {
        SpringApplication.run(TestApplicationContext.class, args);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void run(String... args) {
        initGroups();
        initAdminAccount();
        initTestData(CompoundDatabaseConfiguration.DATABASE_NAME, SI_TEST_COMPOUND_DATA);
    }


    private void initGroups() {
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
    }

    @Transactional
    public void initAdminAccount() {

        RegisteredUser adminUser = new RegisteredUser();
        adminUser.setUsername("admin");
        adminUser.setPassword("123456");
        adminUser.setFirstname("Administrator");
        adminUser.setLastname("Administrator");
        adminUser.setMail("admin@fi.de");
        adminUser.setEnabled(true);
        adminUser.setOrganisation("");

        adminUser = securityService.createUser(adminUser);
        securityService.findAllGroups().forEach(adminUser::addGroup);
        securityService.updateUser(adminUser);
    }

    private void initTestData(String databaseName, String testDataPathName) {
        compoundSearchServices.stream()
                .filter(compoundSearchService -> compoundSearchService.getDatasourceName().equals(databaseName))
                .findFirst()
                .ifPresent(sicss -> stoffIdentImporter.importStoffIdentDataSet(getResourcePath(testDataPathName), LocalDate.now(), sicss));
    }

    private Path getResourcePath(String pathName) {
        try {
            return Paths.get(Objects.requireNonNull(this.getClass().getClassLoader().getResource(pathName)).toURI());
        } catch (URISyntaxException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }
}
