package de.hswt.fi.fileimport.service.excel.formats;

import de.hswt.fi.fileimport.service.api.AbstractImportTest;
import de.hswt.fi.fileimport.service.api.FileBasedFeatureContentImporter;
import de.hswt.fi.fileimport.service.excel.ExcelFeatureContentImporter;
import de.hswt.fi.model.Feature;
import de.hswt.fi.model.FeatureSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExcelImportThermoCiecveTest extends AbstractImportTest {

	private static final String TEST_FILE_NAME = "template_thermo_cieve_rti.xlsx";

	@Override
	public FileBasedFeatureContentImporter getFileImporter() {
		return new ExcelFeatureContentImporter(
				Collections.singletonList(new ThermoCiecveExcelReader()),
				Collections.singletonList(new CalibrationExcelReaderGC()));
	}

	@Override
	public FeatureSet createReferenceData() {

		List<Feature> referenceFeatures = new ArrayList<>();

		addReferenceFeature("1.0", 279.1585, 8.12, referenceFeatures);
		addReferenceFeature("2.0", 267.1714, 8.12, referenceFeatures);
		addReferenceFeature("3.0", 251.1849, 6.08, referenceFeatures);
		addReferenceFeature("4.0", 239.1485, 4.01, referenceFeatures);
		addReferenceFeature("5.0", 259.19, 8.12, referenceFeatures);
		addReferenceFeature("6.0", 149.0231, 8.15, referenceFeatures);
		addReferenceFeature("7.0", 225.1958, 7.15, referenceFeatures);
		addReferenceFeature("8.0", 283.1747, 4.64, referenceFeatures);
		addReferenceFeature("9.0", 356.2638, 6.39, referenceFeatures);
		addReferenceFeature("10.0", 400.2901, 6.49, referenceFeatures);
		addReferenceFeature("11.0", 295.2111, 6.24, referenceFeatures);

		return new FeatureSet(TEST_FILE_NAME, referenceFeatures);
	}

	@Override
	public String getFileName() {
		return TEST_FILE_NAME;
	}

}
