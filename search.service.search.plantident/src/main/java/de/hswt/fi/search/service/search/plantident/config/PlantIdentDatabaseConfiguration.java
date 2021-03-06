package de.hswt.fi.search.service.search.plantident.config;

import de.hswt.fi.common.spring.Profiles;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;

@Profile({Profiles.LC, Profiles.DEVELOPMENT_LC, Profiles.TEST})
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages = PlantIdentDatabaseConfiguration.BASE_PACKAGE,
		entityManagerFactoryRef = PlantIdentDatabaseConfiguration.ENTITY_MANAGER,
		transactionManagerRef = PlantIdentDatabaseConfiguration.TRANSACTION_MANAGER
)
public class PlantIdentDatabaseConfiguration {

    public static final String DATABASE_NAME = "PLANT-IDENT";
    public static final String ENTITY_MANAGER = "plantidentEntityManager";
    public static final String TRANSACTION_MANAGER = "plantidentTransactionManager";
    public static final String ID_PREFIX = "PI";
    static final String BASE_PACKAGE = "de.hswt.fi.search.service.search.plantident.repositories";
    private static final String CONFIGURATION_PREFIX = "spring.datasource.plantident";
    private static final String PERSISTENCE_UNIT = "plantidentPersistenceUnit";
    private static final String ENTITY_PACKAGES = "de.hswt.fi.search.service.mass.search.model";
    private static final String DATA_SOURCE = "plantidentDataSource";
    private static final String DATA_SOURCE_PROPERTIES = "plantidentDataSourceProperties";

	@Bean(name = DATA_SOURCE_PROPERTIES)
	@ConfigurationProperties(CONFIGURATION_PREFIX)
	public DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = DATA_SOURCE)
	public DataSource plantidentDataSource() {
		return dataSourceProperties().initializeDataSourceBuilder().build();
	}

	@Bean(name = ENTITY_MANAGER)
	@PersistenceUnit(name = PERSISTENCE_UNIT)
	public LocalContainerEntityManagerFactoryBean plantidentEntityManager(@Qualifier(DATA_SOURCE) DataSource dataSource,
																			  // Fix for generate-ddl property not picked up by spring with multiple data sources
																			  @Value("${" + CONFIGURATION_PREFIX + ".generate-ddl:false}") boolean generateDdl) {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPackagesToScan(ENTITY_PACKAGES);

        EclipseLinkJpaVendorAdapter vendorAdapter = new EclipseLinkJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(generateDdl);

		em.setJpaVendorAdapter(vendorAdapter);
		em.setPersistenceUnitName(PERSISTENCE_UNIT);

		return em;
	}

	@Bean(name = TRANSACTION_MANAGER)
	public PlatformTransactionManager plantidentTransactionManager(@Qualifier(ENTITY_MANAGER) EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
}
