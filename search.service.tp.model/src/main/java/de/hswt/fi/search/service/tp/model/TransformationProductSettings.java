package de.hswt.fi.search.service.tp.model;

import java.io.Serializable;

public class TransformationProductSettings implements Serializable {

	private static final long serialVersionUID = 1L;

		private double ppm;

	public double getPpm() {
		return ppm;
	}

	public void setPpm(double ppm) {
		this.ppm = ppm;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		TransformationProductSettings other = (TransformationProductSettings) obj;

		return Double.doubleToLongBits(ppm) == Double.doubleToLongBits(other.ppm);
	}

}
