package de.hswt.fi.model;

import java.io.Serializable;

public class RTICalibrationData implements Serializable {

	private static final long serialVersionUID = 1L;

	private String identifier;

	private double logD;

	private double rti;

	//Mean value from the 3 rts
	private double meanRt;

	// Flag if this value will be used in the calibration curve
	private boolean valid;

	public RTICalibrationData(String name, double logD) {
		this.identifier = name;
		this.logD = logD;
		valid = true;
	}

	public RTICalibrationData(String name, double logD, double meanRt) {
		this.identifier = name;
		this.logD = logD;
		this.meanRt = meanRt;
		valid = true;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public double getLogD() {
		return logD;
	}

	public void setLogD(double logD) {
		this.logD = logD;
	}

	public double getRti() {
		return rti;
	}

	public void setRti(double rti) {
		this.rti = rti;
	}

	public double getMeanRt() {
		return meanRt;
	}

		public void setMeanRt(double meanRt) {
		this.meanRt = meanRt;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		long temp;
		temp = Double.doubleToLongBits(logD);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(meanRt);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(rti);
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
		RTICalibrationData other = (RTICalibrationData) obj;
		if (identifier == null) {
			if (other.identifier != null) {
				return false;
			}
		} else if (!identifier.equals(other.identifier)) {
			return false;
		}
		if (Double.doubleToLongBits(logD) != Double.doubleToLongBits(other.logD)) {
			return false;
		}
		if (Double.doubleToLongBits(meanRt) != Double.doubleToLongBits(other.meanRt)) {
			return false;
		}
		if (Double.doubleToLongBits(rti) != Double.doubleToLongBits(other.rti)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "RTICalibrationData [identifier=" + identifier + ", logD=" + logD + ", rti=" + rti
				+ ", meanRt=" + meanRt + "]";
	}

}
