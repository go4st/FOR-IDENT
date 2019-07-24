package de.hswt.fi.userproperties.service.api.repository;

import de.hswt.fi.userproperties.service.model.UserProperties;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPropertiesRepository extends CrudRepository<UserProperties, Long> {

	Optional<UserProperties> findByUserID(Long userID);
}
