package de.hswt.fi.msms.service.model;

import de.hswt.fi.common.Ionisation;

import java.io.Serializable;
import java.util.function.Consumer;

public class MsMsSettings implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean useLocalCandidates;

	private double ppm = 5;

	private double ppmFragments = 5;

	private Ionisation ionisation = Ionisation.NEUTRAL_IONISATION;

	private boolean ready = false;

	private Consumer<MsMsSettings> readyCallback;

	public double getPpm() {
		return ppm;
	}

	public void setPpm(double ppm) {
		this.ppm = ppm;
	}

	public double getPpmFragments() {
		return ppmFragments;
	}

	public void setPpmFragments(double ppmFragments) {
		this.ppmFragments = ppmFragments;
	}

	public Ionisation getIonisation() {
		return ionisation;
	}

	public void setIonisation(Ionisation ionisation) {
		this.ionisation = ionisation;
	}

	public boolean getUseLocalCandidates() {
		return useLocalCandidates;
	}

	public void setUseLocalCandidates(boolean useLocalCandidates) {
		this.useLocalCandidates = useLocalCandidates;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
		if (ready && readyCallback != null) {
			readyCallback.accept(this);
		}
	}

	public void setReadyCallback(Consumer<MsMsSettings> readyCallback) {
		this.readyCallback = readyCallback;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ionisation == null) ? 0 : ionisation.hashCode());
		long temp;
		temp = Double.doubleToLongBits(ppm);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (useLocalCandidates ? 1231 : 1237);
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
		MsMsSettings other = (MsMsSettings) obj;
		if (ionisation != other.ionisation) {
			return false;
		}
		if (ppm != other.ppm) {
			return false;
		}

		return useLocalCandidates == other.useLocalCandidates;
	}

	@Override
	public String toString() {
		return "MsMsData [useLocalCandidates=" + useLocalCandidates + ", ppm=" + ppm
				+ ", ionisation=" + ionisation + ", ready=" + ready + "]";
	}

}
