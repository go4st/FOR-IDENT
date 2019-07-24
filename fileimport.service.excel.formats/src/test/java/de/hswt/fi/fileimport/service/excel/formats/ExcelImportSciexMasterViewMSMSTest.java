package de.hswt.fi.fileimport.service.excel.formats;

import de.hswt.fi.fileimport.service.api.AbstractImportTest;
import de.hswt.fi.fileimport.service.api.FileBasedFeatureContentImporter;
import de.hswt.fi.fileimport.service.excel.ExcelFeatureContentImporter;
import de.hswt.fi.model.Feature;
import de.hswt.fi.model.FeatureSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExcelImportSciexMasterViewMSMSTest extends AbstractImportTest {

	private static final String TEST_FILE_NAME = "template_sciex_msms_rti.xlsx";

	@Override
	public FileBasedFeatureContentImporter getFileImporter() {
		return new ExcelFeatureContentImporter(
				Collections.singletonList(new SciexMSMSExcelReader()),
				Collections.singletonList(new CalibrationExcelReader()));
	}

	@Override
	public FeatureSet createReferenceData() {

		List<Feature> referenceFeatures = new ArrayList<>();
		
		addReferenceFeature("Amantadin", 152.1433, 5.08, "C10H17N",
				createPeakList(
						67.05458866, 361,
						77.03888414, 1240,
						79.0543173, 1492,
						81.07003988, 552,
						91.05399806, 842,
						93.06979062, 1875,
						107.08517772, 1026,
						135.116566, 6993
				), referenceFeatures);

		addReferenceFeature("Simeton", 198.1351, 5.48, "C8H15N5O",
				createPeakList(
						43.03135812, 762,
						57.0454549, 793,
						58.02970525, 428,
						68.02467622, 2534,
						69.00820199, 769,
						71.06049592, 963,
						83.0232068, 350,
						85.07592833, 350,
						96.05518652, 1753,
						97.03941229, 581,
						100.05030123, 3645,
						114.06588602, 951,
						124.086341, 1962,
						128.0811012, 2337,
						142.0712479, 270,
						166.10799181, 207,
						170.1031857, 759,
						198.13524306, 3252
				), referenceFeatures);

		addReferenceFeature("Atrazine-2-hydroxy", 198.1351, 4.98, "C8H15N5O",
				createPeakList(
						43.03085983, 477,
						69.008867, 2699,
						71.0605682, 358,
						86.034865, 3409,
						97.03947771, 1681,
						113.08076594, 226,
						114.06571068, 2945,
						128.05599706, 263,
						128.08169068, 223,
						156.08768495, 4458,
						198.13429032, 1759
				), referenceFeatures);

		addReferenceFeature("Desethylterbutylazin", 202.0860, 8.53, "C7H12ClN5",
				createPeakList(
						43.0317512, 552,
						61.9806466, 436,
						68.02563786, 963,
						79.0065815, 1576,
						104.0014746, 2836,
						110.04662499, 1238,
						146.0233035, 7229
				), referenceFeatures);

		return new FeatureSet(TEST_FILE_NAME, referenceFeatures);
	}

	@Override
	public String getFileName() {
		return TEST_FILE_NAME;
	}

}
