package de.hswt.fi.database.importer.compounds.api;

import de.hswt.fi.search.service.mass.search.model.Entry;
import de.hswt.fi.search.service.search.api.CompoundSearchService;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Set;

/**
 * Created by August Gilg on 05.10.2016.
 */
public interface CompoundImporter {

    boolean importCompoundDataSet(Path path, LocalDate date, CompoundSearchService compoundSearchService);

    Set<Entry> importEntries(Path path);
}
