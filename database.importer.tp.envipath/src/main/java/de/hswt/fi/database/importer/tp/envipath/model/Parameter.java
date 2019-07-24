package de.hswt.fi.database.importer.tp.envipath.model;

public class Parameter {

	private String packageId;

	private String pathwayId;

	public Parameter() {
	}

	public Parameter(Parameter other) {
		packageId = other.packageId;
		pathwayId = other.pathwayId;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getPathwayId() {
		return pathwayId;
	}

	public void setPathwayId(String pathwayId) {
		this.pathwayId = pathwayId;
	}

	@Override
	public String toString() {
		return "Parameter [" + (packageId != null ? "packageId=" + packageId + ", " : "")
				+ (pathwayId != null ? "pathwayId=" + pathwayId : "") + "]";
	}

}
