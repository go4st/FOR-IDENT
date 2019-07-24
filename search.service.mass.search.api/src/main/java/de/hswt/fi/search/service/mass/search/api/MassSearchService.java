package de.hswt.fi.search.service.mass.search.api;

import de.hswt.fi.search.service.mass.search.model.MassSearchJob;
import de.hswt.fi.search.service.mass.search.model.MassSearchResults;
import de.hswt.fi.search.service.search.api.CompoundSearchService;

import java.util.List;

public interface MassSearchService {

	MassSearchResults executeJob(MassSearchJob job, List<CompoundSearchService> selectedSearchServices);

}
