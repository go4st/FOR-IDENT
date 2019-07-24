package de.hswt.fi.fileimport.service.api;

import de.hswt.fi.model.FeatureSet;

import java.nio.file.Path;
import java.util.Optional;

public interface FeatureContentImporter {

	Optional<FeatureSet> importFromFile(Path path);

	Optional<FeatureSet> importFromFileWithCalibrationData(Path filePath, Path calibrationDataPath);

	Optional<FeatureSet> importContent(String content);

}
