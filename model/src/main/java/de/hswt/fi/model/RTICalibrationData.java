package de.hswt.fi.model;

import java.io.Serializable;
import java.util.Objects;

public class RTICalibrationData implements Serializable {

	private static final long serialVersionUID = 1L;

	private String identifier;

	private double signal;

	private double rti;

	//Mean value from the 3 rts
	private double meanRt;

	// Flag if this value will be used in the calibration curve
	private boolean valid;

	public RTICalibrationData(String name, double signal) {
		this.identifier = name;
		this.signal = signal;
		valid = true;
	}

	public RTICalibrationData(String name, double signal, double meanRt) {
		this.identifier = name;
		this.signal = signal;
		this.meanRt = meanRt;
		valid = true;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public double getSignal() {
		return signal;
	}

	public void setSignal(double signal) {
		this.signal = signal;
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RTICalibrationData that = (RTICalibrationData) o;
		return Double.compare(that.signal, signal) == 0 &&
				Double.compare(that.rti, rti) == 0 &&
				Double.compare(that.meanRt, meanRt) == 0 &&
				valid == that.valid &&
				Objects.equals(identifier, that.identifier);
	}

	@Override
	public int hashCode() {
		return Objects.hash(identifier, signal, rti, meanRt, valid);
	}

	@Override
	public String toString() {
		return "RTICalibrationData [identifier=" + identifier + ", signal=" + signal + ", rti=" + rti
				+ ", meanRt=" + meanRt + "]";
	}

}
