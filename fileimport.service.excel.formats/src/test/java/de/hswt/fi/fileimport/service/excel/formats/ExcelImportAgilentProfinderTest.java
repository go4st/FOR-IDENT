package de.hswt.fi.fileimport.service.excel.formats;

import de.hswt.fi.fileimport.service.api.AbstractImportTest;
import de.hswt.fi.fileimport.service.api.FileBasedFeatureContentImporter;
import de.hswt.fi.fileimport.service.excel.ExcelFeatureContentImporter;
import de.hswt.fi.model.Feature;
import de.hswt.fi.model.FeatureSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExcelImportAgilentProfinderTest extends AbstractImportTest {

	private static final String TEST_FILE_NAME = "template_agilent_profinder_rti.xlsx";

	@Override
	public FileBasedFeatureContentImporter getFileImporter() {
		return new ExcelFeatureContentImporter(
				Collections.singletonList(new AgilentProFinderExcelReader()),
				Collections.singletonList(new CalibrationExcelReaderGC()));
	}

	@Override
	public FeatureSet createReferenceData() {

		List<Feature> referenceFeatures = new ArrayList<>();

		addReferenceFeature("SI generated 1", 567.288, 26.49, referenceFeatures);
		addReferenceFeature("SI generated 2", 273.267, 32.43, referenceFeatures);
		addReferenceFeature("SI generated 3", 295.252, 31.99, referenceFeatures);
		addReferenceFeature("SI generated 4", 278.152, 34.04, referenceFeatures);
		addReferenceFeature("SI generated 5", 479.414, 37.01, referenceFeatures);
		addReferenceFeature("SI generated 6", 317.292, 32.04, referenceFeatures);
		addReferenceFeature("SI generated 7", 479.419, 37.48, referenceFeatures);
		addReferenceFeature("SI generated 8", 572.244, 26.49, referenceFeatures);
		addReferenceFeature("SI generated 9", 295.252, 31.53, referenceFeatures);
		addReferenceFeature("SI generated 10", 787.601, 37.48, referenceFeatures);
		addReferenceFeature("SI generated 11", 362.303, 37.48, referenceFeatures);

		return new FeatureSet(TEST_FILE_NAME, referenceFeatures);
	}

	@Override
	public String getFileName() {
		return TEST_FILE_NAME;
	}

}
