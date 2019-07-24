package de.hswt.fi.database.importer.tp.envipath.model;

public class EnviNode {

	private String name;

	private String smiles;

	private String link;

	private String additionalInformation;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSmiles() {
		return smiles;
	}

	public void setSmiles(String smiles) {
		this.smiles = smiles;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	@Override
	public String toString() {
		return "Node [" + (name != null ? "name=" + name + ", " : "")
				+ (smiles != null ? "smiles=" + smiles + ", " : "")
				+ (link != null ? "link=" + link + ", " : "") + (additionalInformation != null
						? "additionalInformation=" + additionalInformation : "")
				+ "]";
	}

}
