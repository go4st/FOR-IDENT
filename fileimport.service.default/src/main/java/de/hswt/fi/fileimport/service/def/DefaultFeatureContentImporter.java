package de.hswt.fi.fileimport.service.def;

import de.hswt.fi.calculation.service.api.CalculationService;
import de.hswt.fi.calculation.service.rti.api.RTICalculationService;
import de.hswt.fi.common.ValueFormatUtil;
import de.hswt.fi.fileimport.service.api.FeatureContentImporter;
import de.hswt.fi.fileimport.service.api.FileBasedFeatureContentImporter;
import de.hswt.fi.fileimport.service.api.RtiCalibrationImporter;
import de.hswt.fi.fileimport.service.api.StringBasedFeatureContentImporter;
import de.hswt.fi.model.Feature;
import de.hswt.fi.model.FeatureSet;
import de.hswt.fi.model.RTICalibrationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Scope("prototype")
public class DefaultFeatureContentImporter implements FeatureContentImporter {

	private CalculationService calculationService;

	private RTICalculationService rtiCalculationService;
	
	private List<FileBasedFeatureContentImporter> fileBasedImporter;

	private List<StringBasedFeatureContentImporter> stringBasedImporter;

	private List<RtiCalibrationImporter> calibrationImporter;

	@Override
	public Optional<FeatureSet> importFromFile(Path contentPath) {
		return importFromFileWithCalibrationData(contentPath, contentPath);
	}
	
	@Override
	public Optional<FeatureSet> importFromFileWithCalibrationData(Path contentPath, Path calibrationPath) {

		Optional<FileBasedFeatureContentImporter> importer = fileBasedImporter
				.stream()
				.filter(p -> p.canHandle(contentPath))
				.findFirst();

		if (importer.isPresent()) {
			FeatureSet featureSet = importer.get().importFromFile(contentPath);
			initialisePrecursorMass(featureSet);
			importRtiCalibration(featureSet, calibrationPath);
			initializeRtiCalibration(featureSet);
			return Optional.of(featureSet);
		}
		return Optional.empty();
	}

	@Override
	public Optional<FeatureSet> importContent(String content) {

		Optional<StringBasedFeatureContentImporter> importer = stringBasedImporter
				.stream()
				.filter(p -> p.canHandle(content))
				.findFirst();

		return importer.map(stringBasedFeatureContentImporter -> stringBasedFeatureContentImporter.importContent(content));
	}

	private void initialisePrecursorMass(FeatureSet featureSet) {

		for(Feature feature : featureSet.getFeatures()) {

			feature.setPrecursorMass(ValueFormatUtil.roundMass(feature.getPrecursorMass()));

			String formula = feature.getNeutralFormula();

			if (formula != null && !formula.isEmpty()) {
				formula = formula.replace(" ", "");
				Double formulaDerivedMass = calculationService.getMassFromFormula(formula);
				if(formulaDerivedMass == null) {
					continue;
				}
				feature.setNeutralFormula(formula);
				feature.setFormulaDerivedMass(ValueFormatUtil.roundMass(formulaDerivedMass));
			}
		}
	}

	private void importRtiCalibration(FeatureSet featureSet, Path calibrationPath) {
		for (RtiCalibrationImporter importer : calibrationImporter) {
			List<RTICalibrationData> calibrationsData = importer.importCalibrationData(calibrationPath);
			if (calibrationsData != null && !calibrationsData.isEmpty()) {
				featureSet.setRtiCalibrationData(calibrationsData);
				break;
			}
		}
	}
	
	private void initializeRtiCalibration(FeatureSet featureSet) {

		if (featureSet.getRtiCalibrationData().isEmpty()) {
			return;
		}

		for (Feature feature : featureSet.getFeatures()) {
			feature.setRetentionTime(ValueFormatUtil.roundRT(feature.getRetentionTime()));
		}

		rtiCalculationService.calculateRetentionTimeIndex(featureSet.getRtiCalibrationData());
		rtiCalculationService.calculateSignal(featureSet.getRtiCalibrationData(),
				new ArrayList<>(featureSet.getFeatures()));
		rtiCalculationService.filterInvalidRTs(featureSet.getRtiCalibrationData());
	}

	@Autowired
	public void setCalculationService(CalculationService calculationService) {
		this.calculationService = calculationService;
	}

	@Autowired
	public void setRtiCalculationService(RTICalculationService rtiCalculationService) {
		this.rtiCalculationService = rtiCalculationService;
	}

	@Autowired
	public void setFileBasedImporter(List<FileBasedFeatureContentImporter> fileBasedImporter) {
		this.fileBasedImporter = fileBasedImporter;
	}

	@Autowired
	public void setStringBasedImporter(List<StringBasedFeatureContentImporter> stringBasedImporter) {
		this.stringBasedImporter = stringBasedImporter;
	}

	@Autowired
	public void setCalibrationImporter(List<RtiCalibrationImporter> calibrationImporter) {
		this.calibrationImporter = calibrationImporter;
	}
}
