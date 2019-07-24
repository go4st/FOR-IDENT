package de.hswt.fi.service.importer.csv;

import de.hswt.fi.common.FileUtil;
import de.hswt.fi.fileimport.service.api.FileBasedFeatureContentImporter;
import de.hswt.fi.model.Feature;
import de.hswt.fi.model.FeatureSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class ThermoCompoundDiscovererCsvContentImporter implements FileBasedFeatureContentImporter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ThermoCompoundDiscovererCsvContentImporter.class);

	private int idCounter;

	@Override
	public FeatureSet importFromFile(Path contentPath) {

		idCounter = 0;

		List<String> lines = FileUtil.readAllLines(contentPath);

		List<Feature> features = new ArrayList<>();

		if(lines == null) {
			return null;
		}

		// Skip first line
		for(int i = 1; i < lines.size(); i++) {
			Feature feature = extractFeature(lines.get(i));
			if(feature != null) {
				features.add(feature);
			}
		}

		return new FeatureSet(contentPath, features);
	}

	private Feature extractFeature(String line) {

		line = line.replaceAll("\"", "");

		String[] data = line.split("\t");

		if(data.length < 3) {
			return null;
		}

		String featureName = "SI generated " + idCounter++;

		try {
			String formula = data[2];
			double mz = Double.parseDouble(data[3]);
			double rt = Double.parseDouble(data[4]);

			return new Feature.Builder(featureName, mz)
					.withRetentionTime(rt)
					.withNeutralFormula(formula)
					.build();

		} catch (NullPointerException | NumberFormatException e) {
			LOGGER.error(e.getMessage());
		}

		return null;
	}

	@Override
	public boolean canHandle(Path path) {
		return path.toString().toLowerCase().endsWith(".csv") && FileUtil.readFirstLine(path).contains("\"Name\"\t\"Predicted Formula\"\t\"Molecular Weight\"\t\"RT [min]\"\t\"Area (Max.)\"");
	}
}
