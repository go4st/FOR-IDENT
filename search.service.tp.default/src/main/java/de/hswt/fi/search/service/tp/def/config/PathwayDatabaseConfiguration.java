package de.hswt.fi.search.service.tp.def.config;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.persistence.PersistenceContext;

import static de.hswt.fi.search.service.tp.def.config.PathwayDatabaseConfiguration.MONGO_TEMPLATE;

@Configuration
@EnableMongoRepositories(basePackages = "de.hswt.fi.search.service.tp.def",
mongoTemplateRef = MONGO_TEMPLATE)
public class PathwayDatabaseConfiguration {

	private static final String CONFIGURATION_PREFIX = "spring.datasource.pathway";

	private static final String DATA_SOURCE_PROPERTIES = "pathwayDataSourceProperties";

	public static final String DATA_SOURCE = "pathwayDataSource";

	private static final String PERSISTENCE_UNIT = "pathwayPersistenceUnit";

	public static final String MONGO_TEMPLATE = "pathwayMongoTemplate";

	@Primary
	@Bean(name = DATA_SOURCE_PROPERTIES)
	@ConfigurationProperties(CONFIGURATION_PREFIX)
	public MongoProperties dataSourceProperties() {
		return new MongoProperties();
	}


	@Primary
	@Bean(name = DATA_SOURCE)
	public MongoClient pathwayMongoClient() {
		MongoProperties properties = dataSourceProperties();
		return new MongoClient(properties.getHost(), properties.getPort());
	}

	@Primary
	@Bean(name = MONGO_TEMPLATE)
	@PersistenceContext(name = PERSISTENCE_UNIT)
	public MongoTemplate pathwayMongoTemplate(@Qualifier(DATA_SOURCE) MongoClient mongoClient) {
		return new MongoTemplate(mongoClient, dataSourceProperties().getDatabase());
	}

}
