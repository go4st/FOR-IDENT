package de.hswt.fi.fileimport.service.excel.formats;

import de.hswt.fi.fileimport.service.api.AbstractImportTest;
import de.hswt.fi.fileimport.service.api.FileBasedFeatureContentImporter;
import de.hswt.fi.fileimport.service.excel.ExcelFeatureContentImporter;
import de.hswt.fi.model.Feature;
import de.hswt.fi.model.FeatureSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExcelImportABSciexTest extends AbstractImportTest {

	private static final String TEST_FILE_NAME = "template_absciex_rti.xlsx";

	@Override
	public FileBasedFeatureContentImporter getFileImporter() {
		return new ExcelFeatureContentImporter(
				Collections.singletonList(new ABSciexExcelReader()),
				Collections.singletonList(new CalibrationExcelReaderGC()));
	}

	@Override
	public FeatureSet createReferenceData() {

		List<Feature> referenceFeatures = new ArrayList<>();

		addReferenceFeature("149.0/6.5 (1)", 149.0448, 6.53, referenceFeatures);
		addReferenceFeature("172.2/10.9 (2)", 172.1689, 10.92, referenceFeatures);
		addReferenceFeature("180.1/7.2 (3)", 180.1011, 7.24, referenceFeatures);
		addReferenceFeature("189.1/5.8 (4)", 189.1023, 5.75, referenceFeatures);
		addReferenceFeature("192.1/9.8 (5)", 192.1381, 9.81, referenceFeatures);
		addReferenceFeature("194.1/4.1 (6)", 194.1154, 4.14, referenceFeatures);
		addReferenceFeature("200.2/13.2 (7)", 200.199, 13.19, referenceFeatures);
		addReferenceFeature("204.1/10.7 (8)", 204.1374, 10.69, referenceFeatures);
		addReferenceFeature("205.1/10.7 (9)", 205.1408, 10.69, referenceFeatures);
		addReferenceFeature("213.1/9.1 (10)", 213.1466, 9.14, referenceFeatures);
		addReferenceFeature("214.1/9.8 (11)", 214.1181, 9.82, referenceFeatures);

		return new FeatureSet(TEST_FILE_NAME, referenceFeatures);
	}

	@Override
	public String getFileName() {
		return TEST_FILE_NAME;
	}

}
