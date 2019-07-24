package de.hswt.fi.application;

import de.hswt.fi.search.service.search.api.CompoundSearchService;
import de.hswt.fi.security.service.api.config.UserDatabaseConfiguration;
import de.hswt.fi.security.service.api.repositories.DatabaseRepository;
import de.hswt.fi.security.service.model.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(transactionManager = UserDatabaseConfiguration.TRANSACTION_MANAGER)
public class UpdateAvailableDatabases {

	private final DatabaseRepository databaseRepository;

	private final List<CompoundSearchService> compoundSearchServices;

	@PersistenceContext(unitName = UserDatabaseConfiguration.ENTITY_MANAGER)
	private EntityManager entityManager;

	@Autowired
	public UpdateAvailableDatabases(DatabaseRepository databaseRepository, List<CompoundSearchService> compoundSearchServices) {
		this.databaseRepository = databaseRepository;
		this.compoundSearchServices = compoundSearchServices;
	}

	@EventListener
	public void appReady(ApplicationReadyEvent event) {

		List<Database> newDatabases = new ArrayList<>();
		List<Database> removedDatabases = new ArrayList<>();

		List<Database> persistedDatabases = databaseRepository.findAll();

		for (CompoundSearchService compoundSearchService : compoundSearchServices) {
			if (persistedDatabases.stream().noneMatch(persistedDb -> persistedDb.getName().equals(compoundSearchService.getDatasourceName()))) {
				newDatabases.add(new Database(compoundSearchService.getDatasourceName()));
			}
		}

		for (Database persistedDatabase : persistedDatabases) {
			if (compoundSearchServices.stream().noneMatch(configDb -> configDb.getDatasourceName().equals(persistedDatabase.getName()))) {
				removedDatabases.add(new Database(persistedDatabase.getName()));
			}
		}

		newDatabases.forEach(entityManager::persist);
		removedDatabases.forEach(entityManager::remove);
	}

}
