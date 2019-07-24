package de.hswt.fi.search.service.tp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document
public class Pathway {

	@Id
	private String id;

	private String name;

	private String link;

	private Set<Compound> compounds;

	private Set<Transformation> transformations;

	private String rootId;

	@Transient
	private Compound root;

	public Pathway() {
		compounds = new HashSet<>();
		transformations = new HashSet<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Set<Compound> getCompounds() {
		return compounds;
	}

	public void setCompounds(Set<Compound> compounds) {
		this.compounds = compounds;
	}

	public void addCompound(Compound compound) {
		if (compound == null) {
			return;
		}
		compounds.add(compound);
	}

	public Set<Transformation> getTransformations() {
		return transformations;
	}

	public void setTransformations(Set<Transformation> transformations) {
		this.transformations = transformations;
	}

	public void addTransformation(Transformation transformation) {
		if (transformation == null) {
			return;
		}
		transformations.add(transformation);
	}

	public String getRootId() {
		return rootId;
	}

	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

	public Compound getRoot() {
		return root;
	}

	public void setRoot(Compound root) {
		this.root = root;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((compounds == null) ? 0 : compounds.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((root == null) ? 0 : root.hashCode());
		result = prime * result + ((rootId == null) ? 0 : rootId.hashCode());
		result = prime * result + ((transformations == null) ? 0 : transformations.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Pathway other = (Pathway) obj;
		if (compounds == null) {
			if (other.compounds != null) {
				return false;
			}
		} else if (!compounds.equals(other.compounds)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (link == null) {
			if (other.link != null) {
				return false;
			}
		} else if (!link.equals(other.link)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (root == null) {
			if (other.root != null) {
				return false;
			}
		} else if (!root.equals(other.root)) {
			return false;
		}
		if (rootId == null) {
			if (other.rootId != null) {
				return false;
			}
		} else if (!rootId.equals(other.rootId)) {
			return false;
		}
		if (transformations == null) {
			if (other.transformations != null) {
				return false;
			}
		} else if (!transformations.equals(other.transformations)) {
			return false;
		}
		return true;
	}
}
