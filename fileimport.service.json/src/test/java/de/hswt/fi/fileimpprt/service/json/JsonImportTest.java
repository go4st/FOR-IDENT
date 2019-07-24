package de.hswt.fi.fileimpprt.service.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hswt.fi.fileimport.service.api.AbstractImportTest;
import de.hswt.fi.fileimport.service.api.FileBasedFeatureContentImporter;
import de.hswt.fi.fileimport.service.json.JsonFeatureContentImporter;
import de.hswt.fi.fileimport.service.json.JsonReader;
import de.hswt.fi.model.Feature;
import de.hswt.fi.model.FeatureSet;

import java.util.ArrayList;
import java.util.List;

public class JsonImportTest extends AbstractImportTest {

	private static final String TEST_FILE_NAME = "example.json";

	@Override
	public FileBasedFeatureContentImporter getFileImporter() {
		return new JsonFeatureContentImporter(new JsonReader(new ObjectMapper()));
	}

	@Override
	public FeatureSet createReferenceData() {

		List<Feature> referenceFeatures = new ArrayList<>();

		addReferenceFeature("Amantadin", 152.143265994439, 5.08055067694997, "C10H17N",
				createPeakList(67.0545886697714, 361, 77.0388841404489, 1240, 79.0543173088268, 1492,
						81.0700398876892, 552, 91.0539980694497, 842, 93.0697906289385, 1875, 107.085177721938, 1026,
						135.11656650017, 6993), referenceFeatures);
		addReferenceFeature("Simeton", 198.135076978148, 5.48038670565246, "C8H15N5O",
				createPeakList(43.0313581297257, 361, 57.0454549074468, 1240, 58.0297052591329, 1492,
						68.0246762259361, 552, 71.0604959288052, 842, 83.0232068950724, 1875, 85.0759283312984, 1026,
						96.0551865227522, 6993), referenceFeatures);

		return new FeatureSet(TEST_FILE_NAME, referenceFeatures);

	}

	@Override
	public String getFileName() {
		return TEST_FILE_NAME;
	}
}
