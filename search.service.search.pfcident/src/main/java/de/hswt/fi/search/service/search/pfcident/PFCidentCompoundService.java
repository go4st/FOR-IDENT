package de.hswt.fi.search.service.search.pfcident;

import de.hswt.fi.common.spring.Profiles;
import de.hswt.fi.search.service.search.api.AbstractCompoundService;
import de.hswt.fi.search.service.search.api.repositories.EntryRepository;
import de.hswt.fi.search.service.search.pfcident.config.PFCIdentDatabaseConfiguration;
import de.hswt.fi.search.service.search.pfcident.repositories.PFCidentEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Profile({Profiles.LC, Profiles.DEVELOPMENT_LC})
@Primary
@Service
@Transactional(transactionManager = PFCIdentDatabaseConfiguration.TRANSACTION_MANAGER, readOnly = true)
public class PFCidentCompoundService extends AbstractCompoundService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PFCidentCompoundService.class);

	private final PFCidentEntryRepository entryRepository;

	@PersistenceContext(unitName = PFCIdentDatabaseConfiguration.ENTITY_MANAGER)
	private EntityManager entityManager;

	@Autowired
	public PFCidentCompoundService(PFCidentEntryRepository entryRepository) {
		this.entryRepository = entryRepository;
	}

	@Override
	public EntryRepository getEntryRepository() {
		return entryRepository;
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public String getIdPrefix() {
		return PFCIdentDatabaseConfiguration.ID_PREFIX;
	}

	@Override
	public String getDatasourceName() {
		return PFCIdentDatabaseConfiguration.DATABASE_NAME;
	}

	@Override
	public boolean isAccessible() {
		return true;
	}

	@Override
	public int getIndex() {
		return 0;
	}
}
