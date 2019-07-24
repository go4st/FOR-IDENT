package de.hswt.fi.search.service.mass.search.model;

import de.hswt.fi.beans.annotations.BeanColumn;
import de.hswt.fi.beans.annotations.BeanComponent;
import de.hswt.fi.model.Score;

@BeanComponent
public class MassSearchResult {

	@BeanColumn(selector = true, i18nId = I18nKeys.SI_MODEL_SCORE)
	private Score score;

	@BeanColumn(selector = true, i18nId = I18nKeys.SI_MODEL_TARGET_IDENTIFIER)
	private String targetIdentifier;

	@BeanColumn(selector = true, i18nId = I18nKeys.SI_MODEL_TARGET_RETENTION_TIME, format = "%.2f")
	private Double targetRetentionTime;

	@BeanColumn(selector = true, i18nId = I18nKeys.SI_MODEL_TARGET_MASS, format = "%.4f")
	private double targetMass;

	@BeanColumn(selector = true, i18nId = I18nKeys.SI_MODEL_DELTA_MASS, format = "%.4f")
	private double deltaMass;

	@BeanColumn
	private Entry entry;

	public MassSearchResult(String targetIdentifier, Entry entry, double targetMass,
							double deltaMass, Double targetRetentionTime) {
		this.targetIdentifier = targetIdentifier;
		this.entry = entry;
		this.targetMass = targetMass;
		this.deltaMass = deltaMass;
		this.targetRetentionTime = targetRetentionTime;

		score = new Score();
	}

	public String getID() {
		return targetIdentifier + entry.getPublicID();
	}

	public String getTargetIdentifier() {
		return targetIdentifier;
	}

	public Entry getEntry() {
		return entry;
	}

	public double getTargetMass() {
		return targetMass;
	}

	public double getDeltaMass() {
		return deltaMass;
	}

	public Double getTargetRetentionTime() {
		return targetRetentionTime;
	}

	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "MassSearchResult [targetIdentifier=" + targetIdentifier + ", entry="
				+ entry + ", targetMass=" + targetMass + ", deltaMass=" + deltaMass
				+ "targetRetentionTime=" + targetRetentionTime + "]";
	}
}
