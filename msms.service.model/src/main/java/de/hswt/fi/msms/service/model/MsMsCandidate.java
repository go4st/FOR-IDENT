package de.hswt.fi.msms.service.model;

import de.hswt.fi.beans.annotations.BeanColumn;
import de.hswt.fi.model.Feature;
import de.hswt.fi.model.Peak;
import de.hswt.fi.model.Score;

import java.io.Serializable;
import java.util.List;

public class MsMsCandidate implements Serializable {

	private static final long serialVersionUID = 1L;

	private Feature feature;

	@BeanColumn(selector = true, i18nId = I18nKeys.MSMS_SCORE)
	private Score score;

	private String identifier;

	private String smiles;

	private String formula;

	private String inChi;

	private String inChiKey1;

	private String inChiKey2;

	private double fragmenterScore;

	private String fragmenterScoreValues;

	private List<Peak> targetPeaks;

	private List<MsMsCandidateFragment> fragments;

	public MsMsCandidate(Feature feature, Score score, String identifier, String smiles,
						 String formula, String inChi, String inChiKey1, String inChiKey2,
						 double fragmenterScore, String fragmenterScoreValues, List<Peak> targetPeaks,
						 List<MsMsCandidateFragment> fragments) {
		this.feature = feature;
		this.score = score;
		this.identifier = identifier;
		this.smiles = smiles;
		this.formula = formula;
		this.inChi = inChi;
		this.inChiKey1 = inChiKey1;
		this.inChiKey2 = inChiKey2;
		this.fragmenterScore = fragmenterScore;
		this.fragmenterScoreValues = fragmenterScoreValues;
		this.targetPeaks = targetPeaks;
		this.fragments = fragments;
	}

	public Score getScore() {
		return score;
	}

	public String getSmiles() {
		return smiles;
	}

	public String getFormula() {
		return formula;
	}

	public double getFragmenterScore() {
		return fragmenterScore;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getInChi() {
		return inChi;
	}

	public String getInChiKey1() {
		return inChiKey1;
	}

	public String getInChiKey2() {
		return inChiKey2;
	}

	public List<Peak> getTargetPeaks() {
		return targetPeaks;
	}

	public List<MsMsCandidateFragment> getFragments() {
		return fragments;
	}

	public String getFragmenterScoreValues() {
		return fragmenterScoreValues;
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	public void setScore(Score score) {
		this.score = score;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public void setInChi(String inChi) {
		this.inChi = inChi;
	}

	public void setInChiKey1(String inChiKey1) {
		this.inChiKey1 = inChiKey1;
	}

	public void setInChiKey2(String inChiKey2) {
		this.inChiKey2 = inChiKey2;
	}

	public void setFragmenterScore(double fragmenterScore) {
		this.fragmenterScore = fragmenterScore;
	}

	public void setFragmenterScoreValues(String fragmenterScoreValues) {
		this.fragmenterScoreValues = fragmenterScoreValues;
	}

	public void setTargetPeaks(List<Peak> targetPeaks) {
		this.targetPeaks = targetPeaks;
	}

	public void setFragments(List<MsMsCandidateFragment> fragments) {
		this.fragments = fragments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((feature == null) ? 0 : feature.hashCode());
		result = prime * result + ((formula == null) ? 0 : formula.hashCode());
		long temp;
		temp = Double.doubleToLongBits(fragmenterScore);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((fragmenterScoreValues == null) ? 0 : fragmenterScoreValues.hashCode());
		result = prime * result + ((fragments == null) ? 0 : fragments.hashCode());
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((inChi == null) ? 0 : inChi.hashCode());
		result = prime * result + ((inChiKey1 == null) ? 0 : inChiKey1.hashCode());
		result = prime * result + ((inChiKey2 == null) ? 0 : inChiKey2.hashCode());
		result = prime * result + ((score == null) ? 0 : score.hashCode());
		result = prime * result + ((targetPeaks == null) ? 0 : targetPeaks.hashCode());
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
		MsMsCandidate other = (MsMsCandidate) obj;
		if (feature == null) {
			if (other.feature != null) {
				return false;
			}
		} else if (!feature.equals(other.feature)) {
			return false;
		}
		if (formula == null) {
			if (other.formula != null) {
				return false;
			}
		} else if (!formula.equals(other.formula)) {
			return false;
		}
		if (Double.doubleToLongBits(fragmenterScore) != Double
				.doubleToLongBits(other.fragmenterScore)) {
			return false;
		}
		if (fragmenterScoreValues == null) {
			if (other.fragmenterScoreValues != null) {
				return false;
			}
		} else if (!fragmenterScoreValues.equals(other.fragmenterScoreValues)) {
			return false;
		}
		if (fragments == null) {
			if (other.fragments != null) {
				return false;
			}
		} else if (!fragments.equals(other.fragments)) {
			return false;
		}
		if (identifier == null) {
			if (other.identifier != null) {
				return false;
			}
		} else if (!identifier.equals(other.identifier)) {
			return false;
		}
		if (inChi == null) {
			if (other.inChi != null) {
				return false;
			}
		} else if (!inChi.equals(other.inChi)) {
			return false;
		}
		if (inChiKey1 == null) {
			if (other.inChiKey1 != null) {
				return false;
			}
		} else if (!inChiKey1.equals(other.inChiKey1)) {
			return false;
		}
		if (inChiKey2 == null) {
			if (other.inChiKey2 != null) {
				return false;
			}
		} else if (!inChiKey2.equals(other.inChiKey2)) {
			return false;
		}
		if (score == null) {
			if (other.score != null) {
				return false;
			}
		} else if (!score.equals(other.score)) {
			return false;
		}
		if (targetPeaks == null) {
			if (other.targetPeaks != null) {
				return false;
			}
		} else if (!targetPeaks.equals(other.targetPeaks)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MsMsCandidate [feature=");
		builder.append(feature);
		builder.append(", score=");
		builder.append(score);
		builder.append(", identifier=");
		builder.append(identifier);
		builder.append(", smiles=");
		builder.append(smiles);
		builder.append(", formula=");
		builder.append(formula);
		builder.append(", inChi=");
		builder.append(inChi);
		builder.append(", inChiKey1=");
		builder.append(inChiKey1);
		builder.append(", inChiKey2=");
		builder.append(inChiKey2);
		builder.append(", fragmenterScore=");
		builder.append(fragmenterScore);
		builder.append(", fragmenterScoreValues=");
		builder.append(fragmenterScoreValues);
		builder.append(", targetPeaks=");
		builder.append(targetPeaks);
		builder.append(", fragments=");
		builder.append(fragments);
		builder.append("]");
		return builder.toString();
	}

}
