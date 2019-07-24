package de.hswt.fi.processing.service.model;

import de.hswt.fi.beans.annotations.BeanColumn;
import de.hswt.fi.beans.annotations.BeanComponent;
import de.hswt.fi.common.Ionisation;
import de.hswt.fi.common.StationaryPhase;

import java.io.Serializable;

@BeanComponent
public class ProcessingSettings implements Serializable {

	private static final long serialVersionUID = 1L;

	@BeanColumn(selector = true, i18nId = I18nKeys.TARGET_PH, format = "%.4f")
	private double ph;

	@BeanColumn(selector = true, i18nId = I18nKeys.PPM, format = "%.4f")
	private double precursorPpm;

	// Bean column needs setter, therefor this constant is not declared static final
	@BeanColumn(selector = true, i18nId = I18nKeys.FORMULA_DERIVED_MASSES_PPM, format = "%.4f")
	private double formulaDerivedMassesPpm = 0.01;

	private double requestedPrecursorPpm;

	@BeanColumn(selector = true, i18nId = I18nKeys.PPM_FRAGMENTS, format = "%.4f")
	private double ppmFragments;

	@BeanColumn(selector = true, i18nId = I18nKeys.INTENSITY_THRESHOLD, format = "%.4f")
	private double intensityThreshold;

	@BeanColumn(selector = true, i18nId = I18nKeys.IONISATION)
	private Ionisation ionisation = Ionisation.NEUTRAL_IONISATION;

	@BeanColumn(selector = true, i18nId = I18nKeys.STATIONARY_PHASE)
	private StationaryPhase stationaryPhase = StationaryPhase.C18;

	private ScoreSettings scoreSettings;

	public ProcessingSettings() {
		precursorPpm = 0.01;
		requestedPrecursorPpm = 5;
		ppmFragments = 5;
		intensityThreshold = 0;
		ph = 3;
		scoreSettings = new ScoreSettings();
	}

	public double getPrecursorPpm() {
		return precursorPpm;
	}

	public double getFormulaDerivedMassesPpm() {
		return formulaDerivedMassesPpm;
	}

	public void setFormulaDerivedMassesPpm(double ppm) {
		this.formulaDerivedMassesPpm = ppm;
	}

	public void setPrecursorPpm(double precursorPpm) {
		this.precursorPpm = precursorPpm;
	}

	public double getRequestedPrecursorPpm() {
		return requestedPrecursorPpm;
	}

	public void setRequestedPrecursorPpm(double requestedPrecursoPpm) {
		this.requestedPrecursorPpm = requestedPrecursoPpm;
	}

	public double getPpmFragments() {
		return ppmFragments;
	}

	public void setPpmFragments(double ppm) {
		ppmFragments = ppm;
	}

	public double getIntensityThreshold() {
		return intensityThreshold;
	}

	public void setIntensityThreshold(double intensityThreshold) {
		this.intensityThreshold = intensityThreshold;
	}

	public Ionisation getIonisation() {
		return ionisation;
	}

	public void setIonisation(Ionisation ionisation) {
		this.ionisation = ionisation;
	}

	public double getPh() {
		return ph;
	}

	public void setPh(double ph) {
		this.ph = ph;
	}

	public StationaryPhase getStationaryPhase() {
		return stationaryPhase;
	}

	public void setStationaryPhase(StationaryPhase stationaryPhase) {
		this.stationaryPhase = stationaryPhase;
	}

	public ScoreSettings getScoreSettings() {
		return scoreSettings;
	}

	public void setScoreSettings(ScoreSettings scoreSettings) {
		this.scoreSettings = scoreSettings;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(formulaDerivedMassesPpm);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((ionisation == null) ? 0 : ionisation.hashCode());
		temp = Double.doubleToLongBits(ph);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(ppmFragments);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(precursorPpm);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(requestedPrecursorPpm);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((scoreSettings == null) ? 0 : scoreSettings.hashCode());
		result = prime * result + ((stationaryPhase == null) ? 0 : stationaryPhase.hashCode());
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
		ProcessingSettings other = (ProcessingSettings) obj;
		if (Double.doubleToLongBits(formulaDerivedMassesPpm) != Double
				.doubleToLongBits(other.formulaDerivedMassesPpm))
			return false;
		if (ionisation != other.ionisation)
			return false;
		if (Double.doubleToLongBits(ph) != Double.doubleToLongBits(other.ph))
			return false;
		if (Double.doubleToLongBits(ppmFragments) != Double.doubleToLongBits(other.ppmFragments))
			return false;
		if (Double.doubleToLongBits(precursorPpm) != Double.doubleToLongBits(other.precursorPpm))
			return false;
		if (Double.doubleToLongBits(requestedPrecursorPpm) != Double
				.doubleToLongBits(other.requestedPrecursorPpm))
			return false;
		if (scoreSettings == null) {
			if (other.scoreSettings != null)
				return false;
		} else if (!scoreSettings.equals(other.scoreSettings))
			return false;

		return stationaryPhase == other.stationaryPhase;
	}

	@Override
	public String toString() {
		return "ProcessSettings [precursorPpm=" + precursorPpm + ", requestedPrecursorPpm="
				+ requestedPrecursorPpm + ", ppmFragments=" + ppmFragments + ", "
				+ (ionisation != null ? "ionisation=" + ionisation + ", " : "") + "ph=" + ph + ", "
				+ (stationaryPhase != null ? "stationaryPhase=" + stationaryPhase + ", " : "")
				+ (scoreSettings != null ? "scoreSettings=" + scoreSettings : "") + "]";
	}
}
