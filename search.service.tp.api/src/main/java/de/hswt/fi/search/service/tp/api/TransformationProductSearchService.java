package de.hswt.fi.search.service.tp.api;


import de.hswt.fi.search.service.tp.model.TransformationProductJob;
import de.hswt.fi.search.service.tp.model.TransformationProductResult;

public interface TransformationProductSearchService {

	 TransformationProductResult executeJob(TransformationProductJob job);

}
