package de.hswt.fi.search.service.index.model;

import de.hswt.fi.beans.annotations.BeanColumn;
import de.hswt.fi.beans.annotations.BeanComponent;
import de.hswt.fi.model.Score;
import de.hswt.fi.search.service.mass.search.model.Entry;

import java.util.Objects;

@BeanComponent
public class IndexSearchResult {

    @BeanColumn(selector = true, i18nId = I18nKeys.RTI_MODEL_SCORE)
    protected Score score;

    @BeanColumn(selector = true, i18nId = I18nKeys.RTI_MODEL_TARGET_IDENTIFIER)
    protected String targetIdentifier;

    @BeanColumn(format = "%.4f", selector = true, i18nId = I18nKeys.RTI_MODEL_TARGET_MASS)
    protected double targetMass;

    @BeanColumn(format = "%.2f", selector = true, i18nId = I18nKeys.RTI_MODEL_TARGET_RT)
    protected Double retentionTime;

    @BeanColumn(format = "%.2f", selector = true, i18nId = I18nKeys.RTI_MODEL_RTI)
    protected Double retentionTimeIndex;

    @BeanColumn(format = "%.4f", selector = true, i18nId = I18nKeys.RTI_MODEL_DELTA_MASS)
    protected double deltaMass;

    @BeanColumn(selector = true, i18nId = I18nKeys.RTI_MODEL_PH_DEPENDENCY,
            i18nValuePropertyId = "i18nKey")
    private MoleculePhDependency moleculePhDependency;

    @BeanColumn(format = "%.2f", selector = true, i18nId = I18nKeys.RTI_MODEL_TARGET_LOG_D)
    private Double targetLogD;

    @BeanColumn(format = "%.1f", selector = true, i18nId = I18nKeys.RTI_MODEL_TARGET_PH)
    private double targetPh;

    private boolean calculatedTargetMass;

    @BeanColumn(format = "%.2f", selector = true, i18nId = I18nKeys.RTI_MODEL_RESULT_LOG_D)
    private Double resultLogD;

    @BeanColumn(format = "%.2f", selector = true, i18nId = I18nKeys.RTI_MODEL_DELTA_LOG_D)
    private Double deltaLogDRtiDb;

    @BeanColumn(format = "%.2f", selector = true, i18nId = I18nKeys.RTI_MODEL_ADJUSTED_LOG_D)
    private Double adjustedLogD;

    @BeanColumn(format = "%.2f", selector = true, i18nId = I18nKeys.RTI_MODEL_ADJUSTED_DELTA_LOG_D)
    private Double deltaLogDAdjustedDb;

    private double ionisation = 0;

    private String stationaryPhase;

    @BeanColumn(format = "%.2f", selector = true, i18nId = de.hswt.fi.search.service.index.model.I18nKeys.RTI_MODEL_RTI)
    protected Double retentionTimeSignal;

    @BeanColumn(format = "%.2f", selector = true, i18nId = I18nKeys.RTI_MODEL_RTI)
    protected Double deltaRetentionTimeSignal;

    // Entry is already annotated as BeanColumn in the mandatory processing result MassSearchResult
    // and should not be included here again, to prevent doubled property id's (e.g. columns in export)
    protected Entry entry;

    protected double ppm;

    protected boolean isFirst;

    protected boolean isLast;

    protected boolean available;

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
    public IndexSearchResult(String targetIdentifier, Entry entry, Double targetRetentionTime,
                             Double targetPh, Double targetLogD, Double targetMass, boolean calculatedTargetMass,
                             Double adjustedLogD, Double resultLogD, Double ppm, Double rti, Double ionisation,
                             String stationaryPhase, MoleculePhDependency dependency) {
        Objects.requireNonNull(entry);

        this.targetIdentifier = targetIdentifier;
        this.retentionTime = targetRetentionTime;
        this.entry = entry;
        this.targetLogD = targetLogD;
        this.targetMass = targetMass;
        this.calculatedTargetMass = calculatedTargetMass;
        this.adjustedLogD = adjustedLogD;
        this.resultLogD = resultLogD;
        this.ppm = ppm;
        this.targetPh = targetPh;
        this.retentionTimeIndex = rti;
        this.ionisation = ionisation;
        this.stationaryPhase = stationaryPhase;
        this.targetIdentifier = targetIdentifier;

        moleculePhDependency = dependency;

        if (entry.getAccurateMass() != null) {
            deltaMass = entry.getAccurateMass().getValue() - targetMass;
        }

        if (entry.getHenryBond() != null && entry.getHenryBond().getValue() != null && retentionTimeSignal != null) {
            deltaRetentionTimeSignal = entry.getHenryBond().getValue() - retentionTimeSignal;
            available = true;
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

    // Constructor for Henry bond variant
    public IndexSearchResult(String targetIdentifier, Entry entry, Double retentionTime,
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

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public String getTargetIdentifier() {
        return targetIdentifier;
    }

    public void setTargetIdentifier(String targetIdentifier) {
        this.targetIdentifier = targetIdentifier;
    }

    public double getTargetMass() {
        return targetMass;
    }

    public void setTargetMass(double targetMass) {
        this.targetMass = targetMass;
    }

    public Double getRetentionTime() {
        return retentionTime;
    }

    public void setRetentionTime(Double retentionTime) {
        this.retentionTime = retentionTime;
    }

    public Double getRetentionTimeIndex() {
        return retentionTimeIndex;
    }

    public void setRetentionTimeIndex(Double retentionTimeIndex) {
        this.retentionTimeIndex = retentionTimeIndex;
    }

    public double getDeltaMass() {
        return deltaMass;
    }

    public void setDeltaMass(double deltaMass) {
        this.deltaMass = deltaMass;
    }

    public MoleculePhDependency getMoleculePhDependency() {
        return moleculePhDependency;
    }

    public void setMoleculePhDependency(MoleculePhDependency moleculePhDependency) {
        this.moleculePhDependency = moleculePhDependency;
    }

    public Double getTargetLogD() {
        return targetLogD;
    }

    public void setTargetLogD(Double targetLogD) {
        this.targetLogD = targetLogD;
    }

    public double getTargetPh() {
        return targetPh;
    }

    public void setTargetPh(double targetPh) {
        this.targetPh = targetPh;
    }

    public boolean isCalculatedTargetMass() {
        return calculatedTargetMass;
    }

    public void setCalculatedTargetMass(boolean calculatedTargetMass) {
        this.calculatedTargetMass = calculatedTargetMass;
    }

    public Double getResultLogD() {
        return resultLogD;
    }

    public void setResultLogD(Double resultLogD) {
        this.resultLogD = resultLogD;
    }

    public Double getDeltaLogDRtiDb() {
        return deltaLogDRtiDb;
    }

    public void setDeltaLogDRtiDb(Double deltaLogDRtiDb) {
        this.deltaLogDRtiDb = deltaLogDRtiDb;
    }

    public Double getAdjustedLogD() {
        return adjustedLogD;
    }

    public void setAdjustedLogD(Double adjustedLogD) {
        this.adjustedLogD = adjustedLogD;
    }

    public Double getDeltaLogDAdjustedDb() {
        return deltaLogDAdjustedDb;
    }

    public void setDeltaLogDAdjustedDb(Double deltaLogDAdjustedDb) {
        this.deltaLogDAdjustedDb = deltaLogDAdjustedDb;
    }

    public double getIonisation() {
        return ionisation;
    }

    public void setIonisation(double ionisation) {
        this.ionisation = ionisation;
    }

    public String getStationaryPhase() {
        return stationaryPhase;
    }

    public void setStationaryPhase(String stationaryPhase) {
        this.stationaryPhase = stationaryPhase;
    }

    public Double getRetentionTimeSignal() {
        return retentionTimeSignal;
    }

    public void setRetentionTimeSignal(Double retentionTimeSignal) {
        this.retentionTimeSignal = retentionTimeSignal;
    }

    public Double getDeltaRetentionTimeSignal() {
        return deltaRetentionTimeSignal;
    }

    public void setDeltaRetentionTimeSignal(Double deltaRetentionTimeSignal) {
        this.deltaRetentionTimeSignal = deltaRetentionTimeSignal;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public double getPpm() {
        return ppm;
    }

    public void setPpm(double ppm) {
        this.ppm = ppm;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexSearchResult that = (IndexSearchResult) o;
        return Double.compare(that.targetMass, targetMass) == 0 &&
                Double.compare(that.deltaMass, deltaMass) == 0 &&
                Double.compare(that.targetPh, targetPh) == 0 &&
                calculatedTargetMass == that.calculatedTargetMass &&
                Double.compare(that.ionisation, ionisation) == 0 &&
                Double.compare(that.ppm, ppm) == 0 &&
                isFirst == that.isFirst &&
                isLast == that.isLast &&
                available == that.available &&
                Objects.equals(score, that.score) &&
                Objects.equals(targetIdentifier, that.targetIdentifier) &&
                Objects.equals(retentionTime, that.retentionTime) &&
                Objects.equals(retentionTimeIndex, that.retentionTimeIndex) &&
                moleculePhDependency == that.moleculePhDependency &&
                Objects.equals(targetLogD, that.targetLogD) &&
                Objects.equals(resultLogD, that.resultLogD) &&
                Objects.equals(deltaLogDRtiDb, that.deltaLogDRtiDb) &&
                Objects.equals(adjustedLogD, that.adjustedLogD) &&
                Objects.equals(deltaLogDAdjustedDb, that.deltaLogDAdjustedDb) &&
                Objects.equals(stationaryPhase, that.stationaryPhase) &&
                Objects.equals(retentionTimeSignal, that.retentionTimeSignal) &&
                Objects.equals(deltaRetentionTimeSignal, that.deltaRetentionTimeSignal) &&
                Objects.equals(entry, that.entry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(score, targetIdentifier, targetMass, retentionTime, retentionTimeIndex, deltaMass, moleculePhDependency, targetLogD, targetPh, calculatedTargetMass, resultLogD, deltaLogDRtiDb, adjustedLogD, deltaLogDAdjustedDb, ionisation, stationaryPhase, retentionTimeSignal, deltaRetentionTimeSignal, entry, ppm, isFirst, isLast, available);
    }

    @Override
    public String toString() {
        return "IndexSearchResult{" +
                "score=" + score +
                ", targetIdentifier='" + targetIdentifier + '\'' +
                ", targetMass=" + targetMass +
                ", retentionTime=" + retentionTime +
                ", retentionTimeIndex=" + retentionTimeIndex +
                ", deltaMass=" + deltaMass +
                ", moleculePhDependency=" + moleculePhDependency +
                ", targetLogD=" + targetLogD +
                ", targetPh=" + targetPh +
                ", calculatedTargetMass=" + calculatedTargetMass +
                ", resultLogD=" + resultLogD +
                ", deltaLogDRtiDb=" + deltaLogDRtiDb +
                ", adjustedLogD=" + adjustedLogD +
                ", deltaLogDAdjustedDb=" + deltaLogDAdjustedDb +
                ", ionisation=" + ionisation +
                ", stationaryPhase='" + stationaryPhase + '\'' +
                ", retentionTimeSignal=" + retentionTimeSignal +
                ", deltaRetentionTimeSignal=" + deltaRetentionTimeSignal +
                ", entry=" + entry +
                ", ppm=" + ppm +
                ", isFirst=" + isFirst +
                ", isLast=" + isLast +
                ", available=" + available +
                '}';
    }
}
