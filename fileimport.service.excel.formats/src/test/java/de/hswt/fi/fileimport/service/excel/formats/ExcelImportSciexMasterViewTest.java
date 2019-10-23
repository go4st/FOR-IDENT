package de.hswt.fi.fileimport.service.excel.formats;

import de.hswt.fi.fileimport.service.api.AbstractImportTest;
import de.hswt.fi.fileimport.service.api.FileBasedFeatureContentImporter;
import de.hswt.fi.fileimport.service.excel.ExcelFeatureContentImporter;
import de.hswt.fi.model.Feature;
import de.hswt.fi.model.FeatureSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExcelImportSciexMasterViewTest extends AbstractImportTest {

	private static final String TEST_FILE_NAME = "template_sciex_rti.xlsx";

	@Override
	public FileBasedFeatureContentImporter getFileImporter() {
		return new ExcelFeatureContentImporter(
				Collections.singletonList(new SciexMasterViewExcelReader()),
				Collections.singletonList(new CalibrationExcelReaderGC()));
	}

	@Override
	public FeatureSet createReferenceData() {

		List<Feature> referenceFeatures = new ArrayList<>();

		addReferenceFeature("Amantadin", 152.1433, 5.08, "C10H17N", referenceFeatures);
		addReferenceFeature("Simeton", 198.1351, 5.48, "C8H15N5O", referenceFeatures);
		addReferenceFeature("Atrazine-2-hydroxy", 198.1351, 4.98, "C8H15N5O", referenceFeatures);
		addReferenceFeature("Desethylterbutylazin", 202.0860, 8.53, "C7H12ClN5", referenceFeatures);
		addReferenceFeature("Simazine", 202.0858, 8.36, "C7H12ClN5", referenceFeatures);
		addReferenceFeature("Atrazine", 216.1016, 9.72, "C8H14ClN5", referenceFeatures);
		addReferenceFeature("Chloridazon", 222.0427, 6.47, "C10H8ClN3O", referenceFeatures);
		addReferenceFeature("Methiocarb", 226.0898, 11.18, "C11H15NO2S", referenceFeatures);
		addReferenceFeature("Diuron", 233.0246, 9.86, "C9H10Cl2N2O", referenceFeatures);
		addReferenceFeature("Pindolol", 249.1600, 5.02, "C14H20N2O2", referenceFeatures);
		addReferenceFeature("Sulfamethoxazole", 254.0598, 6.79, "C10H11N3O3S", referenceFeatures);
		addReferenceFeature("Tramadol", 264.1967, 5.62, "C16H25NO2", referenceFeatures);
		addReferenceFeature("Metoprolol", 268.1915, 5.57, "C15H25NO3", referenceFeatures);

		return new FeatureSet(TEST_FILE_NAME, referenceFeatures);
	}

	@Override
	public String getFileName() {
		return TEST_FILE_NAME;
	}

}
