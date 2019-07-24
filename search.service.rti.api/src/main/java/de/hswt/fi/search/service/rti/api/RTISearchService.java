package de.hswt.fi.search.service.rti.api;

import de.hswt.fi.search.service.rti.model.RTIJob;
import de.hswt.fi.search.service.rti.model.RTISearchResults;
import de.hswt.fi.search.service.search.api.CompoundSearchService;

import java.util.List;

/**
 * The Interface RTISearchService defines the RTI search service.
 *
 * @author Marco Luthardt
 */
public interface RTISearchService {

	/**
	 * Performs a search in the SI database. The search parameter are defined
	 * through the RTI based calculation. The search uses the mass of substances
	 * (with ppm for ranges) and the logD value at a given pH value.
	 *
	 * @param data
	 *            the data which defines the search parameter
	 * @return result object with results, always return a non null
	 */
	 RTISearchResults executeJob(RTIJob data, List<CompoundSearchService> selectedSearchServices);

}
