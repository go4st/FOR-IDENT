package de.hswt.fi.fileimport.service.xml.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName="CEF")
public class CefFile {
	
	@JacksonXmlProperty(isAttribute = true)
	private String version;
	
	@JacksonXmlProperty(localName = "CompoundList")
	private List<Compound> compounds;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<Compound> getCompounds() {
		return compounds;
	}

	public void setCompounds(List<Compound> compounds) {
		this.compounds = compounds;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("CefFile [");
		if (version != null) {
			builder.append("version=");
			builder.append(version);
			builder.append(", ");
		}
		if (compounds != null) {
			builder.append("compounds=");
			builder.append(compounds.subList(0, Math.min(compounds.size(), maxLen)));
		}
		builder.append("]");
		return builder.toString();
	}
	
	

}
