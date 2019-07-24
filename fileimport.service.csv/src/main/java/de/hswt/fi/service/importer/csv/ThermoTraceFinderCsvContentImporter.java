package de.hswt.fi.service.importer.csv;

import com.google.common.base.Splitter;
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
import java.util.regex.Pattern;

@Component
@Scope("prototype")
public class ThermoTraceFinderCsvContentImporter implements FileBasedFeatureContentImporter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ThermoTraceFinderCsvContentImporter.class);

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

		// Only split ',' which are not surrounded by quotation marks
		Splitter splitter = Splitter.on(Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));

		List<String> data = splitter.splitToList(line);

		if(data.size() < 15) {
			LOGGER.error("Line does not contain enough information to be parsed: {}", line);
			return null;
		}

		String featureName = "SI generated " + idCounter++;

		try {
			String formula = data.get(7);
			double mz = Double.parseDouble(data.get(9));
			double rt = Double.parseDouble(data.get(14));

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
		return path.toString().toLowerCase().endsWith(".csv") && FileUtil.readFirstLine(path).contains("Sample Name,Database Name,MZ,IP,FI,LS,Compound Name,Formula,Adduct");
	}
}
