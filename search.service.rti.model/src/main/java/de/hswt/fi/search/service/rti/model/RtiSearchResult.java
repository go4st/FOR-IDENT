package de.hswt.fi.search.service.rti.model;

import de.hswt.fi.beans.annotations.BeanColumn;
import de.hswt.fi.beans.annotations.BeanComponent;
import de.hswt.fi.model.Score;
import de.hswt.fi.search.service.mass.search.model.Entry;

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
public class RtiSearchResult {

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

	private boolean rtiAvailable;

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
			rtiAvailable = false;
		} else {
			rtiAvailable = true;
			deltaLogDRtiDb = targetLogD - resultLogD;
			deltaLogDAdjustedDb = adjustedLogD - resultLogD;
		}
	}

	/**
	 * Creates an unique (procssing scope) id from the target identifier and the
	 * entry id
	 *
	 * @return an unique (procssing scope) id of the result
	 */
	public String getID() {
		return targetIdentifier + entry.getPublicID();
	}

	/**
	 * Gets the target identifier.
	 *
	 * @return the target identifier
	 */
	public String getTargetIdentifier() {
		return targetIdentifier;
	}

	/**
	 * Gets the entry.
	 *
	 * @return the entry
	 */
	public Entry getEntry() {
		return entry;
	}

	public Double getTargetRetentionTime() {
		return targetRetentionTime;
	}

	/**
	 * Gets the result log d.
	 *
	 * @return the result log d
	 */
	public Double getResultLogD() {
		return resultLogD;
	}

	/**
	 * Gets the target mass.
	 *
	 * @return the target mass
	 */
	public double getTargetMass() {
		return targetMass;
	}

	/**
	 * Returns delta of result mass and target mass (result - target).
	 *
	 * @return delta of result mass and target mass.
	 */
	public double getDeltaMass() {
		return deltaMass;
	}

	/**
	 * Gets the target log d.
	 *
	 * @return the target log d
	 */
	public Double getTargetLogD() {
		return targetLogD;
	}

	/**
	 * Returns the adjusted logD, derived from RTI target logD. It's modified
	 * based on the ph dependency of the entry.
	 *
	 * @return the adjusted logD
	 */
	public Double getAdjustedLogD() {
		return adjustedLogD;
	}

	/**
	 * Returns delta of RTI logD and entry logD (RTI - result at given ph
	 * value).
	 *
	 * @return logD delta of RTI logD and entry logD
	 */
	public Double getDeltaLogDRtiDb() {
		return deltaLogDRtiDb;
	}

	/**
	 * Returns delta of adjusted RTI logD and entry logD (adjusted RTI - result
	 * at given ph value).
	 *
	 * @return logD delta of adjusted RTI logD and entry logD
	 */
	public Double getDeltaLogDAdjustedDb() {
		return deltaLogDAdjustedDb;
	}

	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}

	/**
	 * Gets the target ph.
	 *
	 * @return the target ph
	 */
	public double getTargetPh() {
		return targetPh;
	}

	/**
	 * Gets the rti.
	 *
	 * @return the rti
	 */
	public Double getRti() {
		return rti;
	}

	/**
	 * Gets the ionisation.
	 *
	 * @return the ionisation
	 */
	public double getIonisation() {
		return ionisation;
	}

	/**
	 * Gets the stationary phase.
	 *
	 * @return the stationary phase
	 */
	public String getStationaryPhase() {
		return stationaryPhase;
	}

	/**
	 * Gets the molecule ph dependency.
	 *
	 * @return the molecule ph dependency
	 */
	public MoleculePhDependency getMoleculePhDependency() {
		return moleculePhDependency;
	}

	/**
	 * Checks if is calculated target mass.
	 *
	 * @return true, if is calculated target mass
	 */
	public boolean isCalculatedTargetMass() {
		return calculatedTargetMass;
	}

	/**
	 * Gets the ppm.
	 *
	 * @return the ppm
	 */
	public double getPpm() {
		return ppm;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public boolean isRtiAvailable() {
		return rtiAvailable;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(adjustedLogD);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (calculatedTargetMass ? 1231 : 1237);
		temp = Double.doubleToLongBits(deltaLogDAdjustedDb);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(deltaLogDRtiDb);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(deltaMass);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((moleculePhDependency == null) ? 0 : moleculePhDependency.hashCode());
		result = prime * result + ((entry == null) ? 0 : entry.hashCode());
		temp = Double.doubleToLongBits(ionisation);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(ppm);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(resultLogD);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(rti);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((score == null) ? 0 : score.hashCode());
		result = prime * result + ((stationaryPhase == null) ? 0 : stationaryPhase.hashCode());
		result = prime * result + ((targetIdentifier == null) ? 0 : targetIdentifier.hashCode());
		temp = Double.doubleToLongBits(targetLogD);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(targetMass);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(targetPh);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(targetRetentionTime);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		RtiSearchResult other = (RtiSearchResult) obj;
		if (Double.doubleToLongBits(adjustedLogD) != Double.doubleToLongBits(other.adjustedLogD)) {
			return false;
		}
		if (calculatedTargetMass != other.calculatedTargetMass) {
			return false;
		}
		if (Double.doubleToLongBits(deltaLogDAdjustedDb) != Double
				.doubleToLongBits(other.deltaLogDAdjustedDb)) {
			return false;
		}
		if (Double.doubleToLongBits(deltaLogDRtiDb) != Double
				.doubleToLongBits(other.deltaLogDRtiDb)) {
			return false;
		}
		if (Double.doubleToLongBits(deltaMass) != Double.doubleToLongBits(other.deltaMass)) {
			return false;
		}
		if (moleculePhDependency != other.moleculePhDependency) {
			return false;
		}
		if (entry == null) {
			if (other.entry != null) {
				return false;
			}
		} else if (!entry.equals(other.entry)) {
			return false;
		}
		if (Double.doubleToLongBits(ionisation) != Double.doubleToLongBits(other.ionisation)) {
			return false;
		}
		if (Double.doubleToLongBits(ppm) != Double.doubleToLongBits(other.ppm)) {
			return false;
		}
		if (Double.doubleToLongBits(resultLogD) != Double.doubleToLongBits(other.resultLogD)) {
			return false;
		}
		if (Double.doubleToLongBits(rti) != Double.doubleToLongBits(other.rti)) {
			return false;
		}
		if (score == null) {
			if (other.score != null) {
				return false;
			}
		} else if (!score.equals(other.score)) {
			return false;
		}
		if (stationaryPhase == null) {
			if (other.stationaryPhase != null) {
				return false;
			}
		} else if (!stationaryPhase.equals(other.stationaryPhase)) {
			return false;
		}
		if (targetIdentifier == null) {
			if (other.targetIdentifier != null) {
				return false;
			}
		} else if (!targetIdentifier.equals(other.targetIdentifier)) {
			return false;
		}
		if (Double.doubleToLongBits(targetLogD) != Double.doubleToLongBits(other.targetLogD)) {
			return false;
		}
		if (Double.doubleToLongBits(targetMass) != Double.doubleToLongBits(other.targetMass)) {
			return false;
		}
		if (Double.doubleToLongBits(targetPh) != Double.doubleToLongBits(other.targetPh)) {
			return false;
		}
		if (Double.doubleToLongBits(targetRetentionTime) != Double.doubleToLongBits(other.targetRetentionTime)) {
			return false;
		}
		return true;
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
