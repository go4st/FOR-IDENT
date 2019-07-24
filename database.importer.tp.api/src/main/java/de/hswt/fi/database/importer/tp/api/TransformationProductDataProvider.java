package de.hswt.fi.database.importer.tp.api;

import de.hswt.fi.search.service.tp.model.Pathway;

import java.util.List;


public interface TransformationProductDataProvider {

	public List<Pathway> getPathways();

}
