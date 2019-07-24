package de.hswt.fi.database.importer.tp.envipath.model;

public class EnviCompoundPair {

	private String startSmiles;

	private String endSmiles;

	public EnviCompoundPair(String startSmiles, String endSmiles) {
		this.startSmiles = startSmiles;
		this.endSmiles = endSmiles;
	}

	public String getStartSmiles() {
		return startSmiles;
	}

	public void setStartSmiles(String startSmiles) {
		this.startSmiles = startSmiles;
	}

	public String getEndSmiles() {
		return endSmiles;
	}

	public void setEndSmiles(String endSmiles) {
		this.endSmiles = endSmiles;
	}

	@Override
	public String toString() {
		return "CompoundPair [" + (startSmiles != null ? "startSmiles=" + startSmiles + ", " : "")
				+ (endSmiles != null ? "endSmiles=" + endSmiles : "") + "]";
	}

}
