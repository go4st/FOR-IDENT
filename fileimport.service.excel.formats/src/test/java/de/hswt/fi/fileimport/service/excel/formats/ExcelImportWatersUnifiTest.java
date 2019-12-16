package de.hswt.fi.fileimport.service.excel.formats;

import de.hswt.fi.fileimport.service.api.AbstractImportTest;
import de.hswt.fi.fileimport.service.api.FileBasedFeatureContentImporter;
import de.hswt.fi.fileimport.service.excel.ExcelFeatureContentImporter;
import de.hswt.fi.model.Feature;
import de.hswt.fi.model.FeatureSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExcelImportWatersUnifiTest extends AbstractImportTest {

	private static final String TEST_FILE_NAME = "template_waters_unifi_rti.xlsx";

	@Override
	public FileBasedFeatureContentImporter getFileImporter() {
		return new ExcelFeatureContentImporter(
				Collections.singletonList(new WatersUnifiExcelReader()),
				Collections.singletonList(new CalibrationExcelReaderGC()));
	}

	@Override
	public FeatureSet createReferenceData() {

		List<Feature> refs = new ArrayList<>();

		addReferenceFeature("carbamazepine-H2+H2", 237.1029, 6.05, "C15H12N2O", refs);
		addReferenceFeature("carbamazepine-CHNO(cleavage)", 194.0964, 6.12, "C14H11N", refs);
		addReferenceFeature("carbamazepine-CHNO(cleavage)-H2+H2", 194.0964, 6.12, "C14H11N", refs);
		addReferenceFeature("carbamazepine-CHNO(cleavage)", 194.0966, 6.06, "C14H11N", refs);
		addReferenceFeature("carbamazepine-CHNO(cleavage)-H2+H2", 194.0966, 6.06, "C14H11N", refs);
		addReferenceFeature("carbamazepine-CHNO(cleavage)-H2", 192.0805, 6.06, "C14H9N", refs);
		addReferenceFeature("carbamazepine-CHNO(cleavage)+2x(-H2)", 190.0645, 6.05, "C14H7N", refs);
		addReferenceFeature("carbamazepine-CHNO(cleavage)+O-H2-H2+CH2", 220.075, 6.06, "C15H9NO",
				refs);
		addReferenceFeature("carbamazepine-HN(cleavage)-H2", 220.075, 6.06, "C15H9NO", refs);
		addReferenceFeature("carbamazepine+CH2", 251.1173, 7.01, "C16H14N2O", refs);
		addReferenceFeature("carbamazepine-H2+H2+CH2", 251.1173, 7.01, "C16H14N2O", refs);

		return new FeatureSet(TEST_FILE_NAME, refs);
	}

	@Override
	public String getFileName() {
		return TEST_FILE_NAME;
	}

}
