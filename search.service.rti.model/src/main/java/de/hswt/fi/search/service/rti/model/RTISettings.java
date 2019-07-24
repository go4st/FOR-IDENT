package de.hswt.fi.search.service.rti.model;

import de.hswt.fi.common.Ionisation;
import de.hswt.fi.common.StationaryPhase;

import java.io.Serializable;

/**
 * @author Marco Luthardt
 */
public class RTISettings implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final double FORMULA_DERIVED_MASSES_PPM = 0.01;
	
	/** The ph. */
		private double ph;

	/** The ppm. */
		private double ppm;

	/** The ionisation. */
		private Ionisation ionisation = Ionisation.NEUTRAL_IONISATION;

	/** The stationary phase. */
		private StationaryPhase stationaryPhase = StationaryPhase.C18;

	/**
	 * Instantiates a new RTI processing data.
	 *
	 */
	public RTISettings() {
		ppm = 5.0;
		ph = 3.0;
	}

	public double getFormulaDerivedMassesPpm() {
		return FORMULA_DERIVED_MASSES_PPM;
	}

	/**
	 * Gets the ppm.
	 *
	 * @return the ppm
	 */
	public double getPpm() {
		return ppm;
	}

	/**
	 * Sets the ppm.
	 *
	 * @param ppm
	 *            the new ppm
	 */
	public void setPpm(double ppm) {
		this.ppm = ppm;
	}

	/**
	 * Gets the ionisation.
	 *
	 * @return the ionisation
	 */
	public Ionisation getIonisation() {
		return ionisation;
	}

	/**
	 * Sets the ionisation.
	 *
	 * @param ionisation
	 *            the new ionisation
	 */
	public void setIonisation(Ionisation ionisation) {
		this.ionisation = ionisation;
	}

	/**
	 * Gets the pH target level.
	 *
	 * @return the pH target level
	 */
	public double getPh() {
		return ph;
	}

	/**
	 * Sets the pH target level.
	 *
	 * @param ph
	 *            the new pH target level
	 */
	public void setPh(double ph) {
		this.ph = ph;
	}

	/**
	 * Sets the stationary phase.
	 *
	 * @param stationaryPhase
	 *            the new stationary phase
	 */
	public void setStationaryPhase(StationaryPhase stationaryPhase) {
		this.stationaryPhase = stationaryPhase;
	}

	/**
	 * Gets the stationary phase.
	 *
	 * @return the stationary phase
	 */
	public StationaryPhase getStationaryPhase() {
		return stationaryPhase;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ionisation == null) ? 0 : ionisation.hashCode());
		long temp;
		temp = Double.doubleToLongBits(ph);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(ppm);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((stationaryPhase == null) ? 0 : stationaryPhase.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		RTISettings other = (RTISettings) obj;
		if (ionisation == null) {
			if (other.ionisation != null) {
				return false;
			}
		} else if (!ionisation.equals(other.ionisation)) {
			return false;
		}
		if (ppm != other.ppm) {
			return false;
		}
		if (ph != other.ph) {
			return false;
		}
		if (stationaryPhase == null) {
			if (other.stationaryPhase != null) {
				return false;
			}
		} else if (!stationaryPhase.equals(other.stationaryPhase)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "RTIProcessingSettings [ppm=" + ppm + ", ionisation=" + ionisation
				+ ", stationaryPhase=" + stationaryPhase + ", ph=" + ph + "]";
	}
}
