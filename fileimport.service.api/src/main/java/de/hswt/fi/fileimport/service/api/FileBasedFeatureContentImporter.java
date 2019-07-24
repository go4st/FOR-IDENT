package de.hswt.fi.fileimport.service.api;

import de.hswt.fi.model.FeatureSet;

import java.nio.file.Path;

public interface FileBasedFeatureContentImporter {

	FeatureSet importFromFile(Path path);

	boolean canHandle(Path path);
}
