package de.hswt.fi.common;

public enum StationaryPhase {

	C18("C18"),
	C18_POLAR_ENDCAPPED("C18 (polar endcapped)"),
	C18_POLAR_EMBEDDED("C18 (polar embedded)"),
	C8("C8"),
	PHENYL("Phenyl"),
	PFP("PFP"),
	CYANO("Cyano (CN)"),
	AMINO("Amino (NH2)");
	
	private String label;
	
	StationaryPhase(String label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		return label;
	}
	
}
