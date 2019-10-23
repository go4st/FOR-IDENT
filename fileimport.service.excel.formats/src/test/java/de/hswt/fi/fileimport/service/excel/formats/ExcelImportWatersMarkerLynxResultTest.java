package de.hswt.fi.fileimport.service.excel.formats;

import de.hswt.fi.fileimport.service.api.AbstractImportTest;
import de.hswt.fi.fileimport.service.api.FileBasedFeatureContentImporter;
import de.hswt.fi.fileimport.service.excel.ExcelFeatureContentImporter;
import de.hswt.fi.model.Feature;
import de.hswt.fi.model.FeatureSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExcelImportWatersMarkerLynxResultTest extends AbstractImportTest {

	private static final String TEST_FILE_NAME = "template_waters_marker_lynx_result.xlsx";

	@Override
	public FileBasedFeatureContentImporter getFileImporter() {
		return new ExcelFeatureContentImporter(
				Collections.singletonList(new WatersMarkerLynxResultsExcelReader()),
				Collections.singletonList(new CalibrationExcelReaderGC()));
	}

	@Override
	public FeatureSet createReferenceData() {
		
		List<Feature> referenceFeatures = new ArrayList<>();
		
		addReferenceFeature("SI generated 1", 101.0599, 3.34, referenceFeatures);
		addReferenceFeature("SI generated 2", 101.06, 3.41, referenceFeatures);
		addReferenceFeature("SI generated 3", 103.0394, 4.22, referenceFeatures);
		addReferenceFeature("SI generated 4", 103.0395, 4.16, referenceFeatures);
		addReferenceFeature("SI generated 5", 103.0405, 3.48, referenceFeatures);

		return new FeatureSet(TEST_FILE_NAME, referenceFeatures);
	}

	@Override
	public String getFileName() {
		return TEST_FILE_NAME;
	}
	
}
