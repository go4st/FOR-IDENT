package de.hswt.fi.search.service.tp.model;

import de.hswt.fi.model.Feature;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedHashSet;
import java.util.Set;

@Document
public class Compound {

	@Id
	private String id;

	private Boolean root;

	private String name;

	private String link;

	private String smiles;

	private String inChi;

	private String inChiKey;

	private String formula;

	private Double neutralMass;

	private String additionalInformation;

	private Set<String> transformationIds;

	@Transient
	private Set<Transformation> transformations;

	@Transient
	private Set<Feature> matchingFeatures;

	public Compound() {
		transformationIds = new LinkedHashSet<>();
		transformations = new LinkedHashSet<>();
		matchingFeatures = new LinkedHashSet<>();
		root = false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getRoot() {
		return root;
	}

	public void setRoot(Boolean root) {
		this.root = root;
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

	public String getSmiles() {
		return smiles;
	}

	public void setSmiles(String smiles) {
		this.smiles = smiles;
	}

	public String getInChi() {
		return inChi;
	}

	public void setInChi(String inChi) {
		this.inChi = inChi;
	}

	public String getInChiKey() {
		return inChiKey;
	}

	public void setInChiKey(String inChiKey) {
		this.inChiKey = inChiKey;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public Double getNeutralMass() {
		return neutralMass;
	}

	public void setNeutralMass(Double neutralMass) {
		this.neutralMass = neutralMass;
	}

	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	public Set<String> getTransformationIds() {
		return transformationIds;
	}

	public void setTransformationIds(Set<String> transformationIds) {
		this.transformationIds = transformationIds;
	}

	public void addTransformationId(String transformationId) {
		if (transformationId == null) {
			return;
		}
		transformationIds.add(transformationId);
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

	public Set<Feature> getMatchingFeatures() {
		return matchingFeatures;
	}

	public void setMatchingFeatures(Set<Feature> matchingFeatures) {
		this.matchingFeatures = matchingFeatures;
	}

	public void addMatchingFeature(Feature matchingFeature) {
		if (matchingFeature == null || matchingFeatures.contains(matchingFeature)) {
			return;
		}
		matchingFeatures.add(matchingFeature);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((additionalInformation == null) ? 0 : additionalInformation.hashCode());
		result = prime * result + ((formula == null) ? 0 : formula.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inChi == null) ? 0 : inChi.hashCode());
		result = prime * result + ((inChiKey == null) ? 0 : inChiKey.hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((neutralMass == null) ? 0 : neutralMass.hashCode());
		result = prime * result + ((root == null) ? 0 : root.hashCode());
		result = prime * result + ((smiles == null) ? 0 : smiles.hashCode());
		result = prime * result + ((transformationIds == null) ? 0 : transformationIds.hashCode());
		result = prime * result + ((transformations == null) ? 0 : transformations.hashCode());
		result = prime * result + ((matchingFeatures == null) ? 0 : matchingFeatures.hashCode());
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
		Compound other = (Compound) obj;
		if (additionalInformation == null) {
			if (other.additionalInformation != null) {
				return false;
			}
		} else if (!additionalInformation.equals(other.additionalInformation)) {
			return false;
		}
		if (formula == null) {
			if (other.formula != null) {
				return false;
			}
		} else if (!formula.equals(other.formula)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (inChi == null) {
			if (other.inChi != null) {
				return false;
			}
		} else if (!inChi.equals(other.inChi)) {
			return false;
		}
		if (inChiKey == null) {
			if (other.inChiKey != null) {
				return false;
			}
		} else if (!inChiKey.equals(other.inChiKey)) {
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
		if (neutralMass == null) {
			if (other.neutralMass != null) {
				return false;
			}
		} else if (!neutralMass.equals(other.neutralMass)) {
			return false;
		}
		if (root == null) {
			if (other.root != null) {
				return false;
			}
		} else if (!root.equals(other.root)) {
			return false;
		}
		if (smiles == null) {
			if (other.smiles != null) {
				return false;
			}
		} else if (!smiles.equals(other.smiles)) {
			return false;
		}
		if (transformationIds == null) {
			if (other.transformationIds != null) {
				return false;
			}
		} else if (!transformationIds.equals(other.transformationIds)) {
			return false;
		}
		if (transformations == null) {
			if (other.transformations != null) {
				return false;
			}
		} else if (!transformations.equals(other.transformations)) {
			return false;
		}
		if (matchingFeatures == null) {
			if (other.matchingFeatures != null) {
				return false;
			}
		} else if (!matchingFeatures.equals(other.matchingFeatures)) {
			return false;
		}
		return true;
	}
}
