package de.hswt.fi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Peak implements Serializable {

	private static final long serialVersionUID = 1L;

	private double mz;

	@JsonProperty("absoluteIntensity")
	private double intensity;

	private double relativeIntensity;

	protected Peak() {
	}

	public Peak(double mz, double intensity) {
		this.mz = mz;
		this.intensity = intensity;
	}

	public Peak(double mz, double intensity, double relativeIntensity) {
		this.mz = mz;
		this.intensity = intensity;
		this.relativeIntensity = relativeIntensity;
	}

	public double getMz() {
		return mz;
	}

	public double getIntensity() {
		return intensity;
	}

	public void setIntensity(double intensity) {
		this.intensity = intensity;
	}

	public double getRelativeIntensity() {
		return relativeIntensity;
	}

	public void setRelativeIntensity(double relativeIntensity) {
		this.relativeIntensity = relativeIntensity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
//		temp = Double.doubleToLongBits(intensity);
//		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(mz);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(relativeIntensity);
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
		Peak other = (Peak) obj;
//		if (Double.compare(intensity, other.intensity) != 0) {
//			return false;
//		}
		if (Double.compare(mz, other.mz) != 0) {
			return false;
		}
		if (Double.compare(relativeIntensity, other.relativeIntensity) != 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "Peak[mz=" + mz + ", intensity=" + intensity + ", relativeIntensity=" + relativeIntensity + "]";
	}
}
