package de.hswt.fi.search.service.mass.search.model;


import de.hswt.fi.common.Ionisation;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SearchParameter implements Serializable {

	private static final long serialVersionUID = 4413546777204026611L;
	private String cas;
	private String name;
	private String elementalFormula;
	private String iupac;
	private String smiles;
	private Double accurateMass;
	private Double accurateMassRangeMin;
	private Double accurateMassRangeMax;
	private Double ppm;
	private Ionisation ionisation;
	private Double logP;
	private Double logPDelta;
	private Double logPRangeMin;
	private Double logPRangeMax;
	private Double logD;
	private Double logDDelta;
	private Set<String> halogens;
	private char indexChar;
	private String publicID;
	private String inchiKey;
	private List<String> datasourceNames;

	public SearchParameter() {

		ppm = 0.0;
		ionisation = Ionisation.NEUTRAL_IONISATION;
		logPDelta = 0.0;
		logDDelta = 0.0;

		accurateMassRangeMax = Double.MAX_VALUE;
		accurateMassRangeMin = Double.MIN_VALUE;

		logPRangeMin = Double.MIN_VALUE;
		logPRangeMax = Double.MAX_VALUE;

		halogens = java.util.Collections.emptySet();

		indexChar = ' ';
	}

	public SearchParameter(char indexChar) {
		this.indexChar = indexChar;
	}

	public String getCas() {
		return cas;
	}

	public void setCas(String cas) {
		this.cas = cas == null ? "" : cas.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? "" : name.trim();
	}

	public String getElementalFormula() {
		return elementalFormula;
	}

	public void setElementalFormula(String elementalFormula) {
		this.elementalFormula = elementalFormula == null ? "" : elementalFormula.trim();
	}

	public String getIupac() {
		return iupac;
	}

	public void setIupac(String iupac) {
		this.iupac = iupac == null ? "" : iupac.trim();
	}

	public String getSmiles() {
		return smiles;
	}

	public void setSmiles(String smiles) {
		this.smiles = smiles == null ? "" : smiles.trim();
	}

	public Double getAccurateMass() {
		return accurateMass;
	}

	public void setAccurateMass(Double accurateMass) {
		this.accurateMass = accurateMass;
	}

	public Double getPpm() {
		return ppm;
	}

	public void setPpm(Double ppm) {
		this.ppm = ppm;
	}

	public Double getAccurateMassRangeMin() {
		return accurateMassRangeMin;
	}

	public void setAccurateMassRangeMin(Double accurateMassRangeMin) {
		this.accurateMassRangeMin = accurateMassRangeMin;
	}

	public Double getAccurateMassRangeMax() {
		return accurateMassRangeMax;
	}

	public void setAccurateMassRangeMax(Double accurateMassRangeMax) {
		this.accurateMassRangeMax = accurateMassRangeMax;
	}

	public Ionisation getIonisation() {
		return ionisation;
	}

	public void setIonisation(Ionisation ionisation) {
		this.ionisation = ionisation;
	}

	public Double getLogP() {
		return logP;
	}

	public void setLogP(Double logP) {
		this.logP = logP;
	}

	public Double getLogPDelta() {
		return logPDelta;
	}

	public void setLogPDelta(Double logPDelta) {
		this.logPDelta = logPDelta;
	}

	public Double getLogPRangeMin() {
		return logPRangeMin;
	}

	public void setLogPRangeMin(Double logPRangeMin) {
		this.logPRangeMin = logPRangeMin;
	}

	public Double getLogPRangeMax() {
		return logPRangeMax;
	}

	public void setLogPRangeMax(Double logPRangeMax) {
		this.logPRangeMax = logPRangeMax;
	}

	public Double getLogD() {
		return logD;
	}

	public void setLogD(Double logD) {
		this.logD = logD;
	}

	public Double getLogDDelta() {
		return logDDelta;
	}

	public Set<String> getHalogens() {
		return halogens;
	}

	public void setHalogens(Set<String> halogens) {
		if (halogens == null) {
			return;
		}
		this.halogens = halogens;
	}

	public char getIndexChar() {
		return indexChar;
	}

	public void setIndexChar(char indexChar) {
		this.indexChar = indexChar;
	}

	public String getPublicID() {
		return publicID;
	}

	public void setPublicID(String publicID) {
		this.publicID = publicID;
	}

	public String getInchiKey() {
		return inchiKey;
	}

	public void setInchiKey(String inchiKey) {
		this.inchiKey = inchiKey;
	}

	public List<String> getDatasourceNames() {
		return datasourceNames;
	}

	public void setDatasourceNames(List<String> datasourceNames) {
		this.datasourceNames = datasourceNames;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SearchParameter that = (SearchParameter) o;
		return indexChar == that.indexChar &&
				Objects.equals(cas, that.cas) &&
				Objects.equals(name, that.name) &&
				Objects.equals(elementalFormula, that.elementalFormula) &&
				Objects.equals(iupac, that.iupac) &&
				Objects.equals(smiles, that.smiles) &&
				Objects.equals(accurateMass, that.accurateMass) &&
				Objects.equals(accurateMassRangeMin, that.accurateMassRangeMin) &&
				Objects.equals(accurateMassRangeMax, that.accurateMassRangeMax) &&
				Objects.equals(ppm, that.ppm) &&
				ionisation == that.ionisation &&
				Objects.equals(logP, that.logP) &&
				Objects.equals(logPDelta, that.logPDelta) &&
				Objects.equals(logPRangeMin, that.logPRangeMin) &&
				Objects.equals(logPRangeMax, that.logPRangeMax) &&
				Objects.equals(logD, that.logD) &&
				Objects.equals(logDDelta, that.logDDelta) &&
				Objects.equals(halogens, that.halogens) &&
				Objects.equals(publicID, that.publicID) &&
				Objects.equals(inchiKey, that.inchiKey) &&
				Objects.equals(datasourceNames, that.datasourceNames);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cas, name, elementalFormula, iupac, smiles, accurateMass, accurateMassRangeMin, accurateMassRangeMax, ppm, ionisation, logP, logPDelta, logPRangeMin, logPRangeMax, logD, logDDelta, halogens, indexChar, publicID, inchiKey, datasourceNames);
	}

	@Override
	public String toString() {
		return "SearchParameter{" +
				"cas='" + cas + '\'' +
				", name='" + name + '\'' +
				", elementalFormula='" + elementalFormula + '\'' +
				", iupac='" + iupac + '\'' +
				", smiles='" + smiles + '\'' +
				", accurateMass=" + accurateMass +
				", accurateMassRangeMin=" + accurateMassRangeMin +
				", accurateMassRangeMax=" + accurateMassRangeMax +
				", ppm=" + ppm +
				", ionisation=" + ionisation +
				", logP=" + logP +
				", logPDelta=" + logPDelta +
				", logPRangeMin=" + logPRangeMin +
				", logPRangeMax=" + logPRangeMax +
				", logD=" + logD +
				", logDDelta=" + logDDelta +
				", halogens=" + halogens +
				", indexChar=" + indexChar +
				", publicID='" + publicID + '\'' +
				", inchiKey='" + inchiKey + '\'' +
				", datasourceNames=" + datasourceNames +
				'}';
	}
}
