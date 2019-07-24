package de.hswt.fi.fileimport.service.xml.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class Compound {

	@JacksonXmlProperty(isAttribute = true, localName = "algo")
	private String algorithm;

	@JacksonXmlProperty(localName = "Location")
	private Location location;

	@JacksonXmlProperty(localName = "Results")
	private Results results;

	@JacksonXmlProperty(localName = "Chromatogram")
	private Chromatogram chromatogram;

	@JacksonXmlProperty(localName = "Spectrum")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Spectrum> spectrum;

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Results getResults() {
		return results;
	}

	public void setResults(Results results) {
		this.results = results;
	}

	public Chromatogram getChromatogram() {
		return chromatogram;
	}

	public void setChromatogram(Chromatogram chromatogram) {
		this.chromatogram = chromatogram;
	}

	public void setSpectrum(List<Spectrum> spectrum) {
		this.spectrum = spectrum;
	}

	public List<Spectrum> getSpectrum() {
		return spectrum;
	}

	@Override
	public String toString() {
		return "Compound [" + (algorithm != null ? "algorithm=" + algorithm + ", " : "")
				+ (location != null ? "location=" + location + ", " : "")
				+ (results != null ? "results=" + results + ", " : "")
				+ (chromatogram != null ? "chromatogram=" + chromatogram + ", " : "")
				+ (spectrum != null ? "spectrum=" + spectrum : "") + "]";
	}

}
