package de.hswt.fi.database.importer.tp.envipath.model;

public class EnviPathway {

	private String name;

	private String id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Pathway [" + (name != null ? "name=" + name + ", " : "")
				+ (id != null ? "id=" + id : "") + "]";
	}

}
