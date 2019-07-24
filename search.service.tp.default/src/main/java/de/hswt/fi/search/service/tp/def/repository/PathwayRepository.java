package de.hswt.fi.search.service.tp.def.repository;

import de.hswt.fi.search.service.tp.model.Pathway;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PathwayRepository extends MongoRepository<Pathway, String> {

	Optional<Pathway> findById(String id);

	List<Pathway> findByCompounds_InChiKey(String inChiKey);
}
