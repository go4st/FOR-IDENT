package de.hswt.fi.security.service.api.repositories;

import de.hswt.fi.security.service.model.Database;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseRepository extends CrudRepository<Database, String> {

	List<Database> findAll();

	Database findByName(String databaseName);

}
