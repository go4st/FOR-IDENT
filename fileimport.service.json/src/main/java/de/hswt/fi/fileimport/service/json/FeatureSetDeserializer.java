package de.hswt.fi.fileimport.service.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import de.hswt.fi.model.Feature;
import de.hswt.fi.model.FeatureSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FeatureSetDeserializer extends JsonDeserializer<FeatureSet> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeatureSet.class);

	private static final String NODE_IDENTIFIER = "identifier";

	private static final String NODE_FEATURES = "features";

	@Override
	public FeatureSet deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {

		JsonNode jsonNode;

		try {
			jsonNode = jsonParser.getCodec().readTree(jsonParser);
		} catch (JsonProcessingException e) {
			LOGGER.error("An error occured trying to parse input", e);
			return null;
		}

		String identifier = jsonNode.get(NODE_IDENTIFIER).asText();

		List<Feature> features = getFeatures(jsonNode);
		if (features.isEmpty()) {
			return null;
		}

		return new FeatureSet(identifier, features);
	}

	private List<Feature> getFeatures(JsonNode jsonNode) throws IOException {

		ObjectMapper objectMapper = new ObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

		JsonNode featuresJsonNode = jsonNode.get(NODE_FEATURES);
		if (featuresJsonNode == null) {
			return Collections.emptyList();
		}

		List<Feature> features = objectMapper.readValue(featuresJsonNode.traverse(),
				new TypeReference<List<Feature>>() {
				});

		removeFeaturesWithoutMassAndFormula(features);

		return features;
	}

	private void removeFeaturesWithoutMassAndFormula(List<Feature> features) {
		features
				.removeAll(features.stream()
						.filter(f -> f.getPrecursorMass() == null)
						.filter(f -> f.getNeutralFormula() == null)
						.collect(Collectors.toList()));
	}

}
