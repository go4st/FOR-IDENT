package de.hswt.fi.fileimport.service.excel.formats;

import de.hswt.fi.fileimport.service.api.AbstractImportTest;
import de.hswt.fi.fileimport.service.api.FileBasedFeatureContentImporter;
import de.hswt.fi.fileimport.service.excel.ExcelFeatureContentImporter;
import de.hswt.fi.model.Feature;
import de.hswt.fi.model.FeatureSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExcelImportWatersMarkerLynxMarkerTest extends AbstractImportTest {

	private static final String TEST_FILE_NAME = "template_waters_marker_lynx_marker_rti.xlsx";

	@Override
	public FileBasedFeatureContentImporter getFileImporter() {
		return new ExcelFeatureContentImporter(
				Collections.singletonList(new WatersMarkerLynxMarkerExcelReader()),
				Collections.singletonList(new CalibrationExcelReader()));
	}

	@Override
	public FeatureSet createReferenceData() {
		
		List<Feature> referenceFeatures = new ArrayList<>();
		
		addReferenceFeature("SI generated 1", 100.0753, 2.21, referenceFeatures);
		addReferenceFeature("SI generated 2", 100.0755, 2.12, referenceFeatures);
		addReferenceFeature("SI generated 3", 100.0758, 2.80, referenceFeatures);
		addReferenceFeature("SI generated 4", 100.0759, 2.30, referenceFeatures);
		addReferenceFeature("SI generated 5", 100.076, 2.74, referenceFeatures);
		addReferenceFeature("SI generated 6", 100.0761, 2.68, referenceFeatures);
		addReferenceFeature("SI generated 7", 101.0599, 3.34, referenceFeatures);

		return new FeatureSet(TEST_FILE_NAME, referenceFeatures);
	}

	@Override
	public String getFileName() {
		return TEST_FILE_NAME;
	}
	
}
