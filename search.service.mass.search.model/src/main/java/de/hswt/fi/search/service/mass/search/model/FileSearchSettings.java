package de.hswt.fi.search.service.mass.search.model;

import de.hswt.fi.common.Ionisation;

import java.io.Serializable;

public class FileSearchSettings implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final double FORMULA_DERIVED_MASSES_PPM = 0.01;

	private double ppm;

	private Ionisation ionisation;

	public FileSearchSettings() {
		ppm = 5.0;
		ionisation = Ionisation.NEUTRAL_IONISATION;
	}

	public double getFormulaDerivedMassesPpm() {
		return FORMULA_DERIVED_MASSES_PPM;
	}

	public double getPpm() {
		return ppm;
	}

	public void setPpm(double ppm) {
		this.ppm = ppm;
	}

	public Ionisation getIonisation() {
		return ionisation;
	}

	public void setIonisation(Ionisation ionisation) {
		this.ionisation = ionisation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (ionisation == null ? 0 : ionisation.hashCode());
		long temp;
		temp = Double.doubleToLongBits(ppm);
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
		FileSearchSettings other = (FileSearchSettings) obj;
		if (ionisation == null) {
			if (other.ionisation != null) {
				return false;
			}
		} else if (!ionisation.equals(other.ionisation)) {
			return false;
		}

		return ppm == other.ppm;
	}

	@Override
	public String toString() {
		return "FileSearchSettings [ppm=" + ppm + ", ionisation=" + ionisation + "]";
	}

}
