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
public class BrukerCsvContentImporter implements FileBasedFeatureContentImporter {

	private static final Logger LOGGER = LoggerFactory.getLogger(BrukerCsvContentImporter.class);

	@Override
	public FeatureSet importFromFile(Path contentPath) {

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

		String[] data = line.split(",");

		if(data.length < 3) {
			return null;
		}

		String featureName = data[0];

		try {
			double rt = Double.parseDouble(data[1]);
			String formula = data[3].replaceAll(" ", "").trim();
			double mz = Double.parseDouble(data[4]);

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
		return path.toString().toLowerCase().endsWith(".csv") &&
				FileUtil.readFirstLine(path).contains("#, RT [min], Area, Cmpd.SF, Max. m/z, Prec. m/z");
	}
}
