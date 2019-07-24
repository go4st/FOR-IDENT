package de.hswt.fi.fileimport.service.mzml;

import de.hswt.fi.fileimport.service.api.FileBasedFeatureContentImporter;
import de.hswt.fi.model.Feature;
import de.hswt.fi.model.FeatureSet;
import de.hswt.fi.model.Peak;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import uk.ac.ebi.jmzml.model.mzml.*;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshaller;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshallerException;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Scope("prototype")
public class MzmlFeatureContentImporter implements FileBasedFeatureContentImporter {

	private static final Logger LOGGER = LoggerFactory.getLogger(MzmlFeatureContentImporter.class);

	@Override
	public FeatureSet importFromFile(Path contentPath) {

		List<Feature> features = new ArrayList<>();

		MzMLUnmarshaller reader = new MzMLUnmarshaller(contentPath.toFile());
		Set<String> ids = reader.getSpectrumIDs();

		for (String id : ids) {

			// Fetch feature id
			String spectrumId = extractIdentifier(reader, id);

			// Fetch retention time
			Double rt = extractRetentionTime(reader, id);

			// Fetch Mass, intensity and charge
			Double mass = extractMass(reader, id);

			List<Peak> peaks = extractSpectra(reader, id);

			// Cases occured where ms/ms data was present but no mass was given
			// (Unif mzML Files\Intact Protein Analysis MS Data\)
			if (mass != null) {
				features.add(new Feature.Builder(spectrumId, mass)
						.withRetentionTime(rt)
						.withPeaks(peaks).build());
			}
		}

		return new FeatureSet(contentPath, features);
	}

	@Override
	public boolean canHandle(Path path) {
		//TODO Improve mzml file detection
		return path.toString().toLowerCase().endsWith("mzml");

//		try {
//			return Files.lines(path).limit(2).filter(s -> s.contains("mzML")).count() == 1;
//		} catch (IOException e) {
//			LOGGER.error("An error occured", e);
//		}
//		return false;
	}

	private Double extractMass(MzMLUnmarshaller reader, String id) {

		try {
			if (reader.getSpectrumById(id).getPrecursorList() != null
					&& reader.getSpectrumById(id).getPrecursorList().getPrecursor() != null) {
				List<Precursor> precursors = reader.getSpectrumById(id).getPrecursorList()
						.getPrecursor();

				Optional<CVParam> precursorMass = precursors.stream()
						.map(precursor -> precursor.getSelectedIonList().getSelectedIon())
						.flatMap(List::stream)
						.map(ParamGroup::getCvParam)
						.flatMap(List::stream)
						.filter(cvParam -> cvParam.getUnitName().equals("m/z"))
						.findFirst();

				if (precursorMass.isPresent()) {
					return Double.valueOf(precursorMass.get().getValue());
				}
			}
		} catch (MzMLUnmarshallerException e) {
			LOGGER.error(e.getMessage());
		}
		return null;
	}

	private String extractIdentifier(MzMLUnmarshaller reader, String id) {
		String spectrumId = "";
		try {
			spectrumId = reader.getSpectrumById(id).getId();
		} catch (MzMLUnmarshallerException e) {
			LOGGER.error(e.getMessage());
		}
		return spectrumId;
	}

	private Double extractRetentionTime(MzMLUnmarshaller reader, String id) {
		Double rt = null;
		try {
			List<Scan> scans = reader.getSpectrumById(id).getScanList().getScan();
			for (Scan scan : scans) {
				List<CVParam> params = scan.getCvParam();
				for (CVParam param : params) {
					if (param.getName().equals("scan start time")) {
						rt = Double.valueOf(param.getValue());
					}
				}
			}
		} catch (MzMLUnmarshallerException e) {
			LOGGER.error(e.getMessage());
		}
		return rt;
	}

	private List<Peak> extractSpectra(MzMLUnmarshaller reader, String id) {

		List<Peak> peaks = new ArrayList<>();

		try {
			if (reader.getSpectrumById(id) != null
					&& reader.getSpectrumById(id).getBinaryDataArrayList() != null) {
				List<BinaryDataArray> binaries = reader.getSpectrumById(id).getBinaryDataArrayList()
						.getBinaryDataArray();

				if (binaries != null) {
					Number[] mzs = binaries.get(0).getBinaryDataAsNumberArray();
					Number[] intensities = binaries.get(1).getBinaryDataAsNumberArray();

					for (int i = 0; i < mzs.length && i < intensities.length; i++) {
						peaks.add(new Peak(mzs[i].doubleValue(), intensities[i].doubleValue()));
					}
				}
			}
		} catch (MzMLUnmarshallerException e) {
			LOGGER.error(e.getMessage());
		}
		return peaks;
	}

}
