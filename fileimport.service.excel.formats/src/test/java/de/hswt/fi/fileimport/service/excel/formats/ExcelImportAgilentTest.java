package de.hswt.fi.fileimport.service.excel.formats;

import de.hswt.fi.fileimport.service.api.AbstractImportTest;
import de.hswt.fi.fileimport.service.api.FileBasedFeatureContentImporter;
import de.hswt.fi.fileimport.service.excel.ExcelFeatureContentImporter;
import de.hswt.fi.model.Feature;
import de.hswt.fi.model.FeatureSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExcelImportAgilentTest extends AbstractImportTest {

	private static final String TEST_FILE_NAME = "template_agilent_rti.xls";

	@Override
	public FileBasedFeatureContentImporter getFileImporter() {
		return new ExcelFeatureContentImporter(
				Collections.singletonList(new AgilentExcelReader()),
				Collections.singletonList(new CalibrationExcelReaderGC()));
	}

	@Override
	public FeatureSet createReferenceData() {

		List<Feature> referenceFeatures = new ArrayList<>();
		
		addReferenceFeature("Cpd 404: 17.853", 431.2725, 17.85, referenceFeatures);
		addReferenceFeature("Cpd 405: 21.771", 286.1387, 21.77, referenceFeatures);
		addReferenceFeature("Cpd 406: 21.771", 281.1831, 21.77, referenceFeatures);
		addReferenceFeature("Cpd 407: 21.894", 475.3008, 21.89, referenceFeatures);
		addReferenceFeature("Cpd 408: 21.898", 480.2538, 21.9, referenceFeatures);
		addReferenceFeature("Cpd 409: 21.940", 549.333, 21.94, referenceFeatures);
		addReferenceFeature("Cpd 410: 22.101", 510.3887, 22.1, referenceFeatures);
		addReferenceFeature("Cpd 411: 22.102", 452.3365, 22.1, referenceFeatures);
		addReferenceFeature("Cpd 412: 22.412", 519.3255, 22.41, referenceFeatures);
		addReferenceFeature("Cpd 413: 22.413", 524.2802, 22.41, referenceFeatures);
		addReferenceFeature("Cpd 414: 22.424", 397.2621, 22.42, referenceFeatures);
		
		return new FeatureSet(TEST_FILE_NAME, referenceFeatures);
	}

	@Override
	public String getFileName() {
		return TEST_FILE_NAME;
	}

}
