package de.hswt.fi.fileimport.service.excel.formats;

import de.hswt.fi.fileimport.service.api.AbstractImportTest;
import de.hswt.fi.fileimport.service.api.FileBasedFeatureContentImporter;
import de.hswt.fi.fileimport.service.excel.ExcelFeatureContentImporter;
import de.hswt.fi.model.Feature;
import de.hswt.fi.model.FeatureSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExcelImportThermoCompoundDiscovererTest extends AbstractImportTest {

	private static final String TEST_FILE_NAME = "template_thermo_compound_discoverer_rti.xlsx";

	@Override
	public FileBasedFeatureContentImporter getFileImporter() {
		return new ExcelFeatureContentImporter(
				Collections.singletonList(new ThermoCompoundDiscovererExcelReader()),
				Collections.singletonList(new CalibrationExcelReader()));
	}

	@Override
	public FeatureSet createReferenceData() {

		List<Feature> referenceFeatures = new ArrayList<>();

		addReferenceFeature("SI generated 1", 255.0134, 4.4, referenceFeatures);
		addReferenceFeature("SI generated 2", 255.0136, 4.32, referenceFeatures);
		addReferenceFeature("SI generated 3", 250.1568, 10.3, referenceFeatures);
		addReferenceFeature("SI generated 4", 187.0625, 6.24, referenceFeatures);

		return new FeatureSet(TEST_FILE_NAME, referenceFeatures);
	}

	@Override
	public String getFileName() {
		return TEST_FILE_NAME;
	}

}
