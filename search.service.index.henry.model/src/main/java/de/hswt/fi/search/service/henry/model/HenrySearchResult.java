package de.hswt.fi.search.service.henry.model;

import de.hswt.fi.beans.annotations.BeanColumn;
import de.hswt.fi.beans.annotations.BeanComponent;
import de.hswt.fi.model.Score;
import de.hswt.fi.search.service.index.model.IndexSearchResult;
import de.hswt.fi.search.service.mass.search.model.Entry;

import java.util.Objects;

@BeanComponent
public class HenrySearchResult implements IndexSearchResult {

	@BeanColumn(selector = true, i18nId = I18nKeys.RTI_MODEL_SCORE)
	private Score score;

	@BeanColumn(selector = true, i18nId = I18nKeys.RTI_MODEL_TARGET_IDENTIFIER)
	private String targetIdentifier;

	@BeanColumn(format = "%.4f", selector = true, i18nId = I18nKeys.RTI_MODEL_TARGET_MASS)
	private double targetMass;

	@BeanColumn(format = "%.2f", selector = true, i18nId = I18nKeys.RTI_MODEL_TARGET_RT)
	private Double retentionTime;

	@BeanColumn(format = "%.2f", selector = true, i18nId = I18nKeys.RTI_MODEL_RTI)
	private Double retentionTimeIndex;

	@BeanColumn(format = "%.2f", selector = true, i18nId = I18nKeys.RTI_MODEL_RTI)
	private Double retentionTimeSignal;

	@BeanColumn(format = "%.2f", selector = true, i18nId = I18nKeys.RTI_MODEL_RTI)
	private Double deltaRetentionTimeSignal;

	@BeanColumn(format = "%.4f", selector = true, i18nId = I18nKeys.RTI_MODEL_DELTA_MASS)
	private double deltaMass;

	// Entry is already annotated as BeanColumn in the mandatory processing result MassSearchResult
	// and should not be included here again, to prevent doubled property id's (e.g. columns in export)
	private Entry entry;

	private double ppm;

	private boolean isFirst;

	private boolean isLast;

	private boolean available;

	/**
	 * Instantiates a new RTI results container.
	 *
	 * @param targetIdentifier     the target identifier
	 * @param entry                the entry
	 * @param retentionTime  the target retention time
	 * @param targetMass           the target mass
	 * @param ppm                  the ppm
	 * @param retentionTimeIndex                  the rti value
	 */
	public HenrySearchResult(String targetIdentifier, Entry entry, Double retentionTime,
						    Double targetMass, Double ppm, Double retentionTimeIndex, Double retentionTimeSignal) {


		Objects.requireNonNull(entry);

		this.targetIdentifier = targetIdentifier;
		this.retentionTime = retentionTime;
		this.entry = entry;
		this.targetMass = targetMass;
		this.ppm = ppm;
		this.retentionTimeIndex = retentionTimeIndex;
		this.retentionTimeSignal = retentionTimeSignal;

		if (entry.getAccurateMass() != null) {
			deltaMass = entry.getAccurateMass().getValue() - targetMass;
		}

		if (entry.getHenryBond() != null && entry.getHenryBond().getValue() != null && retentionTimeSignal != null) {
			deltaRetentionTimeSignal = entry.getHenryBond().getValue() - retentionTimeSignal;
			available = true;
		}
	}

	public String getID() {
		return targetIdentifier + entry.getPublicID();
	}

	public String getTargetIdentifier() {
		return targetIdentifier;
	}

	@Override
	public void setTargetIdentifier(String targetIdentifier) {

	}

	@Override
	public Entry getEntry() {
		return entry;
	}

	@Override
	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	public Double getRetentionTime() {
		return retentionTime;
	}

	@Override
	public void setRetentionTime(Double retentionTime) {
		this.retentionTime = retentionTime;
	}

	public double getTargetMass() {
		return targetMass;
	}

	public double getDeltaMass() {
		return deltaMass;
	}

	@Override
	public Double getRetentionTimeIndex() {
		return retentionTimeIndex;
	}

	@Override
	public void setRetentionTimeIndex(Double retentionTimeIndex) {
		this.retentionTimeIndex = retentionTimeIndex;
	}

	@Override
	public Double getRetentionTimeSignal() {
		return retentionTimeSignal;
	}

	@Override
	public void setRetentionTimeSignal(Double retentionTimeSignal) {
		this.retentionTimeSignal = retentionTimeSignal;
	}

	@Override
	public Double getDeltaRetentionTimeSignal() {
		return deltaRetentionTimeSignal;
	}

	@Override
	public void setDeltaRetentionTimeSignal(Double deltaRetentionTimeSignal) {
		this.deltaRetentionTimeSignal = deltaRetentionTimeSignal;
	}

	@Override
	public Score getScore() {
		return score;
	}

	@Override
	public void setScore(Score score) {
		this.score = score;
	}

	@Override
	public double getIonisation() {
		return 0;
	}

	@Override
	public double getPpm() {
		return ppm;
	}

	@Override
	public boolean isFirst() {
		return isFirst;
	}

	@Override
	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	@Override
	public boolean isLast() {
		return isLast;
	}

	@Override
	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	@Override
	public boolean isAvailable() {
		return available;
	}


}
