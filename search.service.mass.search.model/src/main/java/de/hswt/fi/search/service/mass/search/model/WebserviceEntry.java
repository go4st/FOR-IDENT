package de.hswt.fi.search.service.mass.search.model;

public class WebserviceEntry {

	private String inchi;
	
	private String inchiKey;
	
	private String monoisotopicMass;
	
	private String elementalFormula;
	
	private String identifer;
	
	private String name;

	private String datasourceName;

	private WebserviceEntry() {
	}
	
	public static WebserviceEntry of(Entry entry) {
		WebserviceEntry webserviceEntry = new WebserviceEntry();
		webserviceEntry.setIdentifer(entry.getPublicID());
		webserviceEntry.setName(entry.getName().getValue());
		webserviceEntry.setMonoisotopicMass(entry.getAccurateMass().getValue().toString());
		webserviceEntry.setInchi(entry.getInchi().getValue());
		webserviceEntry.setInchiKey(entry.getInchiKey().getValue());
		webserviceEntry.setElementalFormula(entry.getElementalFormula().getValue());
		webserviceEntry.setDatasourceName(entry.getDatasourceName());
		return webserviceEntry;
	}

	public String getInchi() {
		return inchi;
	}

	public void setInchi(String inchi) {
		this.inchi = inchi;
	}

	public String getInchiKey() {
		return inchiKey;
	}

	public void setInchiKey(String inchiKey) {
		this.inchiKey = inchiKey;
	}

	public String getMonoisotopicMass() {
		return monoisotopicMass;
	}

	public void setMonoisotopicMass(String monoisotopicMass) {
		this.monoisotopicMass = monoisotopicMass;
	}

	public String getElementalFormula() {
		return elementalFormula;
	}

	public void setElementalFormula(String elementalFormula) {
		this.elementalFormula = elementalFormula;
	}

	public String getIdentifer() {
		return identifer;
	}

	public void setIdentifer(String identifer) {
		this.identifer = identifer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDatasourceName() {
		return datasourceName;
	}

	public void setDatasourceName(String datasourceName) {
		this.datasourceName = datasourceName;
	}
}
