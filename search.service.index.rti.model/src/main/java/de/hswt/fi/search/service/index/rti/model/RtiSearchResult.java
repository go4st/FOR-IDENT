package de.hswt.fi.search.service.index.rti.model;

import de.hswt.fi.beans.annotations.BeanColumn;
import de.hswt.fi.beans.annotations.BeanComponent;
import de.hswt.fi.model.Score;
import de.hswt.fi.search.service.index.model.IndexSearchResult;
import de.hswt.fi.search.service.mass.search.model.Entry;

import java.util.Objects;

/**
 * The Class RTIResultsContainer is a container for a single result of a single
 * target substance. A target can have more than one RTIResultsContainer
 * associated with it. But there can only be one "best match" result substance
 * for a single target.
 * <p>
 * The container contains the DB data of the result substance different
 * additional informations which are needed to get an overview of the quality of
 * the search result, e.g. - "best match" flag - result logD
 * <p>
 * Additional information are contained for easy access when viewing the result,
 * e.g.
 * <p>
 * - the target mass - the target pH level - ...
 *
 * @author Marco Luthardt
 */

@BeanComponent
public class RtiSearchResult implements IndexSearchResult {

	@BeanColumn(selector = true, i18nId = I18nKeys.RTI_MODEL_SCORE)
	private Score score;

	@BeanColumn(selector = true, i18nId = I18nKeys.RTI_MODEL_PH_DEPENDENCY,
			i18nValuePropertyId = "i18nKey")
	private MoleculePhDependency moleculePhDependency;

	@BeanColumn(selector = true, i18nId = I18nKeys.RTI_MODEL_TARGET_IDENTIFIER)
	private String targetIdentifier;

	@BeanColumn(format = "%.2f", selector = true, i18nId = I18nKeys.RTI_MODEL_TARGET_RT)
	private Double targetRetentionTime;

	@BeanColumn(format = "%.4f", selector = true, i18nId = I18nKeys.RTI_MODEL_TARGET_MASS)
	private double targetMass;

	@BeanColumn(format = "%.2f", selector = true, i18nId = I18nKeys.RTI_MODEL_TARGET_LOG_D)
	private Double targetLogD;

	@BeanColumn(format = "%.1f", selector = true, i18nId = I18nKeys.RTI_MODEL_TARGET_PH)
	private double targetPh;

	private boolean calculatedTargetMass;

	@BeanColumn(format = "%.2f", selector = true, i18nId = I18nKeys.RTI_MODEL_RTI)
	private Double rti;

	@BeanColumn(format = "%.2f", selector = true, i18nId = I18nKeys.RTI_MODEL_RESULT_LOG_D)
	private Double resultLogD;

	@BeanColumn(format = "%.4f", selector = true, i18nId = I18nKeys.RTI_MODEL_DELTA_MASS)
	private double deltaMass;

	@BeanColumn(format = "%.2f", selector = true, i18nId = I18nKeys.RTI_MODEL_DELTA_LOG_D)
	private Double deltaLogDRtiDb;

	@BeanColumn(format = "%.2f", selector = true, i18nId = I18nKeys.RTI_MODEL_ADJUSTED_LOG_D)
	private Double adjustedLogD;

	@BeanColumn(format = "%.2f", selector = true, i18nId = I18nKeys.RTI_MODEL_ADJUSTED_DELTA_LOG_D)
	private Double deltaLogDAdjustedDb;

	// Entry is already annotated as BeanColumn in the mandatory processing result MassSearchResult
	// and should not be included here again, to prevent doubled property id's (e.g. columns in export)  
	private Entry entry;

	private double ppm;

	private double ionisation;

	private String stationaryPhase;

	private boolean isFirst;

	private boolean isLast;

	private boolean available;

	/**
	 * Instantiates a new RTI results container.
	 *
	 * @param targetIdentifier     the target identifier
	 * @param entry                the entry
	 * @param targetRetentionTime  the target retention time
	 * @param targetPh             the target ph
	 * @param targetLogD           the target logD
	 * @param targetMass           the target mass
	 * @param calculatedTargetMass the calculated target mass
	 * @param adjustedLogD         the adjusted RTI logD
	 * @param resultLogD           the result logD
	 * @param ppm                  the ppm
	 * @param rti                  the rti value
	 * @param ionisation           the ionisation
	 * @param stationaryPhase      the stationary phase
	 * @param dependency           the dependency
	 */
	public RtiSearchResult(String targetIdentifier, Entry entry, Double targetRetentionTime,
						   Double targetPh, Double targetLogD, Double targetMass, boolean calculatedTargetMass,
						   Double adjustedLogD, Double resultLogD, Double ppm, Double rti, Double ionisation,
						   String stationaryPhase, MoleculePhDependency dependency) {
		this.targetIdentifier = targetIdentifier;
		this.targetRetentionTime = targetRetentionTime;
		this.entry = entry;
		this.targetLogD = targetLogD;
		this.targetMass = targetMass;
		this.calculatedTargetMass = calculatedTargetMass;
		this.adjustedLogD = adjustedLogD;
		this.resultLogD = resultLogD;
		this.ppm = ppm;
		this.targetPh = targetPh;
		this.rti = rti;
		this.ionisation = ionisation;
		this.stationaryPhase = stationaryPhase;
		moleculePhDependency = dependency;

		if (entry != null && entry.getAccurateMass() != null) {
			deltaMass = entry.getAccurateMass().getValue() - targetMass;
		}

		// If no logD is present
		if (this.targetLogD == null) {
			available = false;
		} else {
			available = true;
			deltaLogDRtiDb = targetLogD - resultLogD;
			deltaLogDAdjustedDb = adjustedLogD - resultLogD;
		}
	}

	public String getID() {
		return targetIdentifier + entry.getPublicID();
	}

	public String getTargetIdentifier() {
		return targetIdentifier;
	}

	@Override
	public Entry getEntry() {
		return entry;
	}

	@Override
	public Double getTargetRetentionTime() {
		return targetRetentionTime;
	}

	public Double getResultLogD() {
		return resultLogD;
	}

	@Override
	public double getTargetMass() {
		return targetMass;
	}

	@Override
	public double getDeltaMass() {
		return deltaMass;
	}

	public Double getTargetLogD() {
		return targetLogD;
	}

	public Double getAdjustedLogD() {
		return adjustedLogD;
	}

	public Double getDeltaLogDRtiDb() {
		return deltaLogDRtiDb;
	}

	public Double getDeltaLogDAdjustedDb() {
		return deltaLogDAdjustedDb;
	}

	@Override
	public Score getScore() {
		return score;
	}

	@Override
	public void setScore(Score score) {
		this.score = score;
	}

	public double getTargetPh() {
		return targetPh;
	}

	public Double getRti() {
		return rti;
	}

	@Override
	public double getIonisation() {
		return ionisation;
	}

	@Override
	public String getStationaryPhase() {
		return stationaryPhase;
	}

	public MoleculePhDependency getMoleculePhDependency() {
		return moleculePhDependency;
	}

	@Override
	public boolean isCalculatedTargetMass() {
		return calculatedTargetMass;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RtiSearchResult that = (RtiSearchResult) o;
		return Double.compare(that.targetMass, targetMass) == 0 &&
				Double.compare(that.targetPh, targetPh) == 0 &&
				calculatedTargetMass == that.calculatedTargetMass &&
				Double.compare(that.deltaMass, deltaMass) == 0 &&
				Double.compare(that.ppm, ppm) == 0 &&
				Double.compare(that.ionisation, ionisation) == 0 &&
				isFirst == that.isFirst &&
				isLast == that.isLast &&
				available == that.available &&
				Objects.equals(score, that.score) &&
				moleculePhDependency == that.moleculePhDependency &&
				Objects.equals(targetIdentifier, that.targetIdentifier) &&
				Objects.equals(targetRetentionTime, that.targetRetentionTime) &&
				Objects.equals(targetLogD, that.targetLogD) &&
				Objects.equals(rti, that.rti) &&
				Objects.equals(resultLogD, that.resultLogD) &&
				Objects.equals(deltaLogDRtiDb, that.deltaLogDRtiDb) &&
				Objects.equals(adjustedLogD, that.adjustedLogD) &&
				Objects.equals(deltaLogDAdjustedDb, that.deltaLogDAdjustedDb) &&
				Objects.equals(entry, that.entry) &&
				Objects.equals(stationaryPhase, that.stationaryPhase);
	}

	@Override
	public int hashCode() {
		return Objects.hash(score, moleculePhDependency, targetIdentifier, targetRetentionTime, targetMass, targetLogD, targetPh, calculatedTargetMass, rti, resultLogD, deltaMass, deltaLogDRtiDb, adjustedLogD, deltaLogDAdjustedDb, entry, ppm, ionisation, stationaryPhase, isFirst, isLast, available);
	}

	@Override
	public String toString() {
		return "RTISearchResult [targetIdentifier=" + targetIdentifier + ", entry=" + entry
				+ ", targetRt=" + targetRetentionTime + ", targetMass=" + targetMass + ", targetLogD="
				+ targetLogD + ", targetPh=" + targetPh + ", resultLogD=" + resultLogD
				+ ", calculatedTargetMass=" + calculatedTargetMass + ", rti=" + rti + ", ppm=" + ppm
				+ ", ionisation=" + ionisation + ", stationaryPhase=" + stationaryPhase
				+ ", dependency=" + moleculePhDependency + ", score="
				+ score + ", deltaMass=" + deltaMass + ", deltaLogDRtiDb=" + deltaLogDRtiDb
				+ ", adjustedLogD=" + adjustedLogD + ", deltaLogDAdjustedDb=" + deltaLogDAdjustedDb
				+ "]";
	}

}
