package de.hswt.fi.search.service.search.compound.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;

import static de.hswt.fi.search.service.search.compound.config.CompoundDatabaseConfiguration.*;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages = BASE_PACKAGE,
		entityManagerFactoryRef = ENTITY_MANAGER,
		transactionManagerRef = TRANSACTION_MANAGER
)
public class CompoundDatabaseConfiguration {

	public static final String ID_PREFIX = "CP";
	public static final String DATABASE_NAME = "Compound";
	public static final String ENTITY_MANAGER = "compoundEntityManager";
	public static final String TRANSACTION_MANAGER = "compoundTransactionManager";
	static final String BASE_PACKAGE = "de.hswt.fi.search.service.search.compound.repositories";
    private static final String CONFIGURATION_PREFIX = "spring.datasource.compound";
	private static final String PERSISTENCE_UNIT = "compoundPersistenceUnit";

	private static final String ENTITY_PACKAGES = "de.hswt.fi.search.service.mass.search.model";

	private static final String DATA_SOURCE = "compoundDataSource";

	private static final String DATA_SOURCE_PROPERTIES = "compoundDataSourceProperties";

	@Bean(name = DATA_SOURCE_PROPERTIES)
	@ConfigurationProperties(CONFIGURATION_PREFIX)
	public DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = DATA_SOURCE)
	public DataSource stoffidentDataSource() {
		return dataSourceProperties().initializeDataSourceBuilder().build();
	}

	@Bean(name = ENTITY_MANAGER)
	@PersistenceUnit(name = PERSISTENCE_UNIT)
	public LocalContainerEntityManagerFactoryBean stoffidentEntityManager(@Qualifier(DATA_SOURCE) DataSource dataSource,
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
	public PlatformTransactionManager compoundTransactionManager(@Qualifier(ENTITY_MANAGER) EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
}
