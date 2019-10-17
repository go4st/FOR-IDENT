package de.hswt.fi.search.service.index.api;

import de.hswt.fi.search.service.index.model.IndexJob;
import de.hswt.fi.search.service.index.model.IndexSearchResults;
import de.hswt.fi.search.service.search.api.CompoundSearchService;

import java.util.List;

public interface IndexSearchService {
	 IndexSearchResults executeJob(IndexJob data, List<CompoundSearchService> selectedSearchServices);
}
