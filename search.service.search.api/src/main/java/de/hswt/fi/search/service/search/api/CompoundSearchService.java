package de.hswt.fi.search.service.search.api;

import de.hswt.fi.search.service.mass.search.model.Entry;
import de.hswt.fi.search.service.mass.search.model.SearchParameter;
import de.hswt.fi.search.service.mass.search.model.SourceList;
import de.hswt.fi.search.service.mass.search.model.WebserviceEntry;
import de.hswt.fi.search.service.mass.search.model.properties.CategoryStringProperty;
import de.hswt.fi.search.service.mass.search.model.properties.NumberSearchProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The Interface SIDataSearchService defines methods to perform queries in the
 * SI DB.
 *
 * @author Marco Luthardt
 */
public interface CompoundSearchService {

	String getIdPrefix();

	String getDatasourceName();

	boolean isAccessible();

	int getIndex();

	/**
	 * Returns the latest time when any of the entries in the DB was updated.
	 *
	 * @return the last update time
	 */
	long getLastUpdateTime();

	/**
	 * Search for entries by first character of the substance name.
	 *
	 * @param character the character to search for
	 * @return the result object containing the results
	 */
	List<Entry> searchByNameFirstCharacter(char character);

	/**
	 * Search for entries by accurate mass.
	 *
	 * @param accurateMass the accurate mass to search for
	 * @return the result object containing the results
	 */
	List<Entry> searchByAccurateMass(NumberSearchProperty accurateMass);

	/**
	 * Search for entries by accurate mass and target pH for logD.
	 *
	 * @param accurateMass the accurate mass to search for
	 * @return the result object containing the results
	 */
	List<Entry> searchByAccurateMassAndPh(NumberSearchProperty accurateMass, double ph);

	/**
	 * The method takes multiple search parameter, which are wrapped in the
	 * searchParameter parameter, and concatenates all search parameter with AND
	 * as search criteria.
	 *
	 * @param searchParameter the search parameters to search for
	 * @return the result object containing the results
	 */
	List<Entry> searchDynamic(SearchParameter searchParameter);

	Page<WebserviceEntry> findByFormula(String elementalFormula, Pageable pageable);

	Page<WebserviceEntry> findByAccurateMass(Double accurateMassMin, Double accurateMassMax, Pageable pageable);

	Page<String> findAllInchiKeys(Pageable pageable);

	Page<Object> findAllInchiKeysAndPublicIDs(Pageable pageable);

	Page<String> findAllPublicIDs(Pageable pageable);

	Optional<WebserviceEntry> findByPublicID(String publicID);

	Optional<WebserviceEntry> findByInchiKeyValue(String inchiKey);

    void writeDatabase(Set<Entry> entries, Set<SourceList> sourceLists, Set<CategoryStringProperty> categories);
}
