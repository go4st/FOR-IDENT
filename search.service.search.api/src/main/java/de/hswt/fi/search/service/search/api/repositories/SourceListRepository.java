package de.hswt.fi.search.service.search.api.repositories;

import de.hswt.fi.search.service.mass.search.model.SourceList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SourceListRepository extends CrudRepository<SourceList, Long> {

}
