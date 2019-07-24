package de.hswt.fi.fileimport.service.txt;

import de.hswt.fi.fileimport.service.api.AbstractImportTest;
import de.hswt.fi.fileimport.service.api.FileBasedFeatureContentImporter;
import de.hswt.fi.model.Feature;
import de.hswt.fi.model.FeatureSet;

import java.util.ArrayList;
import java.util.List;

public class TxtImportSciexTest extends AbstractImportTest {

	private static final String TEST_FILE_NAME = "exampleSciex.txt";

	@Override
	public FileBasedFeatureContentImporter getFileImporter() {
		return new SciexTxtFeatureContentImporter();
	}

	@Override
	public FeatureSet createReferenceData() {

		List<Feature> referenceFeatures = new ArrayList<>();

		addReferenceFeature("100.0755 / 4.53", 100.0758, 4.536, "C5H9NO",
				createPeakList(37.0098, 5, 37.0142, 10, 38.0175, 5, 38.0238, 5, 39.0256, 41), referenceFeatures);

		addReferenceFeature("120.0805 / 4.47", 120.0806, 4.460, "C8H9N",
				createPeakList(51.0239, 20, 51.0291, 20, 65.0392, 20, 77.0387, 61, 91.0543, 31), referenceFeatures);

		addReferenceFeature("152.1431 / 5.13", 152.1431, 5.123, "C10H17N",
				createPeakList(39.0268, 20, 41.0405, 31, 53.0413, 51, 55.0548, 41, 65.0387, 31), referenceFeatures);

		addReferenceFeature("154.1224 / 7.22", 154.1226, 7.217, "C9H15NO",
				createPeakList(39.0264, 31, 41.0403, 72, 43.0200, 31, 44.0156, 82, 44.0505, 10), referenceFeatures);

		addReferenceFeature("160.1325 / 4.77", 160.1328, 4.767, "C8H17NO2",
				createPeakList(37.0085, 5, 38.0173, 5, 39.0257, 26, 41.0402, 56, 41.0470, 20), referenceFeatures);

		addReferenceFeature("166.0858 / 4.47", 166.0861, 4.461, "C9H11NO2",
				createPeakList(42.0344, 10, 42.0426, 10, 51.0234, 51, 53.0423, 20, 65.0384, 20), referenceFeatures);

		addReferenceFeature("172.1692 / 10.96", 172.1692, 10.946, "C10H21NO",
				createPeakList(37.0137, 10, 38.0213, 10, 39.0250, 20, 39.0309, 10, 41.0405, 41), referenceFeatures);

		addReferenceFeature("180.1015 / 7.26", 180.1015, 7.256, "C10H13NO2",
				createPeakList(39.0245, 31, 39.0299, 10, 43.0199, 51, 65.0393, 237, 66.0484, 10), referenceFeatures);

		addReferenceFeature("188.0816 / 5.77", 188.0816, 5.764, "C10H9N3O",
				createPeakList(37.0121, 10, 39.0235, 10, 41.0474, 10, 42.0367, 133, 44.0158, 2), referenceFeatures);

		addReferenceFeature("189.1015 / 5.85", 189.1018, 5.856, "C11H12N2O",
				createPeakList(39.0256, 17, 41.0395, 10, 41.0455, 14, 42.0363, 27, 43.0434, 10), referenceFeatures);

		addReferenceFeature("192.1381 / 9.72", 192.1382, 9.741, "C12H17NO",
				createPeakList(44.0149, 133, 65.0396, 245, 72.0451, 143, 90.7818, 36, 91.0544, 3068), referenceFeatures);

		return new FeatureSet(TEST_FILE_NAME, referenceFeatures);

	}

	@Override
	public String getFileName() {
		return TEST_FILE_NAME;
	}
}
