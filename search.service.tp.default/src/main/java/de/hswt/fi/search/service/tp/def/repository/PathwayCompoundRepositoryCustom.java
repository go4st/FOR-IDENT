package de.hswt.fi.search.service.tp.def.repository;

import de.hswt.fi.search.service.tp.model.MatchingPathway;

import java.util.List;

public interface PathwayCompoundRepositoryCustom {

	List<MatchingPathway> aggregateCompoundsInMassRange(double lowerMass, double upperMass);

}
