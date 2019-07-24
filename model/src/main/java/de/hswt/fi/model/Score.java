package de.hswt.fi.model;

import de.hswt.fi.beans.annotations.BeanColumn;
import de.hswt.fi.beans.annotations.BeanComponent;

import java.io.Serializable;

@BeanComponent
public class Score implements Serializable {

	private static final long serialVersionUID = 1L;

	@BeanColumn(format = "%.2f", i18nId = I18nKeys.SCORE_SCORE)
	private Double scoreValue;

	@BeanColumn(format = "%.2f", i18nId = I18nKeys.SCORE_WEIGHT)
	private double weight;

	@BeanColumn(format = "%.2f", i18nId = I18nKeys.SCORE_WEIGHTED_SCORE)
	private double weightedValue;

	public Score() {
		scoreValue = null;
		weight = -1;
		weightedValue = -1;
	}

	public Score(double scoreValue, double weight, double weightedValue) {
		this.scoreValue = scoreValue;
		this.weight = weight;
		this.weightedValue = weightedValue;
	}

	public Double getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(Double scoreValue) {
		this.scoreValue = scoreValue;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getWeightedValue() {
		return weightedValue;
	}

	public void setWeightedValue(double weightedValue) {
		this.weightedValue = weightedValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((scoreValue == null) ? 0 : scoreValue.hashCode());
		long temp;
		temp = Double.doubleToLongBits(weight);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(weightedValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Score other = (Score) obj;
		if (scoreValue == null) {
			if (other.scoreValue != null)
				return false;
		} else if (!scoreValue.equals(other.scoreValue))
			return false;
		if (Double.doubleToLongBits(weight) != Double.doubleToLongBits(other.weight))
			return false;
		if (Double.doubleToLongBits(weightedValue) != Double.doubleToLongBits(other.weightedValue))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Score [value=" + scoreValue + ", weight=" + weight + ", weightedValue=" + weightedValue
				+ "]";
	}
}