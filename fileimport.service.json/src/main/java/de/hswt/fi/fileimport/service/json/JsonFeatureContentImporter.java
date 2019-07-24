package de.hswt.fi.fileimport.service.json;

import de.hswt.fi.fileimport.service.api.StringBasedFeatureContentImporter;
import de.hswt.fi.model.FeatureSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@Scope("prototype")
public class JsonFeatureContentImporter implements StringBasedFeatureContentImporter {
	
	private JsonReader jsonReader;
	
	@Autowired
	public JsonFeatureContentImporter(JsonReader jsonReader) {
		this.jsonReader = jsonReader;
	}

	@Override
	public FeatureSet importFromFile(Path contentPath) {
		return jsonReader.parseFile(contentPath);
	}

	@Override
	public FeatureSet importContent(String content) {
		return jsonReader.parseString(content);
	}

	@Override
	public boolean canHandle(Path path) {
		return jsonReader.isValidJson(path);
	}

	@Override
	public boolean canHandle(String content) {
		return jsonReader.isValidJson(content);
	}
}
