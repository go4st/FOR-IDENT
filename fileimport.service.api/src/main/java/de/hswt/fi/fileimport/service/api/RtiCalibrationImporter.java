package de.hswt.fi.fileimport.service.api;

import de.hswt.fi.model.RTICalibrationData;

import java.nio.file.Path;
import java.util.List;

public interface RtiCalibrationImporter {

	List<RTICalibrationData> importCalibrationData(Path calibrationPath);
}
