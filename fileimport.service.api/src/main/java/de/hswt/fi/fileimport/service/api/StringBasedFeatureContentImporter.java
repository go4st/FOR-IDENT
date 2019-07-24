package de.hswt.fi.fileimport.service.api;

import de.hswt.fi.model.FeatureSet;

public interface StringBasedFeatureContentImporter extends FileBasedFeatureContentImporter {

	FeatureSet importContent(String content);

	boolean canHandle(String content);

}
