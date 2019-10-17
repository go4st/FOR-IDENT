package de.hswt.fi.calculation.service.rti.def;

import de.hswt.fi.calculation.service.rti.api.RTICalculationService;
import de.hswt.fi.model.Feature;
import de.hswt.fi.model.RTICalibrationData;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Class DefaultRTICalculationServiceImpl is an implementation of the RTI
 * calculation service.
 * 
 * @author Marco Luthardt
 */
@Component
@Scope("prototype")
public class DefaultRTICalculationService implements RTICalculationService {

	/** The Constant minimum RTI value. */
	private static final double MIN_INDEX = 50.0;

	/** The Constant maximum RTI value. */
	private static final double MAX_INDEX = 150.0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hswt.riskident.rti.algo.api.RTICalculationService#interpolateRTIfromRT
	 * (double, java.util.List)
	 */
	@Override
	public Double interpolateRTIfromRT(Double rt, List<RTICalibrationData> calibration) {

		if(rt == null) {
			return null;
		}
		
		double lastValue = 0.0;
		int lowerIndex;
		int upperIndex = 0;
		for (int i = 0; i < calibration.size(); i++) {
			double rtValue = calibration.get(i).getMeanRt();
			if (Double.compare(rtValue, 0.0) == 0
					|| Double.compare(rtValue, Double.MAX_VALUE) == 0) {
				continue;
			} else if (lastValue >= rtValue) {
				continue;
			} else {
				lastValue = rtValue;
				lowerIndex = upperIndex;
				upperIndex = i;
			}
			if (calibration.get(lowerIndex).getMeanRt() <= rt
					&& rt <= calibration.get(upperIndex).getMeanRt()) {
				return interpolate(calibration.get(lowerIndex).getMeanRt(),
						calibration.get(upperIndex).getMeanRt(), rt,
						calibration.get(lowerIndex).getRti(), calibration.get(upperIndex).getRti());
			}
		}

		return null;
	}

	@Override
	public Double interpolateSignal(Double rt, List<RTICalibrationData> calibration) {
		
		if(rt == null) {
			return null;
		}
		
		double lastValue = 0.0;
		int lowerIndex;
		int upperIndex = 0;
		for (int i = 0; i < calibration.size(); i++) {
			double logPValue = calibration.get(i).getSignal();
			double rtValue = calibration.get(i).getMeanRt();
			if (Double.compare(logPValue, 0.0) == 0
					|| Double.compare(logPValue, Double.MAX_VALUE) == 0
					|| Double.compare(rtValue, 0.0) == 0
					|| Double.compare(rtValue, Double.MAX_VALUE) == 0) {
				continue;
			} else if (lastValue >= rtValue) {
				continue;
			} else {
				lastValue = rtValue;
				lowerIndex = upperIndex;
				upperIndex = i;
			}
			if (calibration.get(lowerIndex).getMeanRt() <= rt
					&& rt <= calibration.get(upperIndex).getMeanRt()) {
				return interpolate(calibration.get(lowerIndex).getMeanRt(),
						calibration.get(upperIndex).getMeanRt(), rt,
						calibration.get(lowerIndex).getSignal(),
						calibration.get(upperIndex).getSignal());
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hswt.riskident.rti.algo.api.RTICalculationService#interpolate(double,
	 * double, double, double, double)
	 */
	@Override
	public Double interpolate(double x1, double x2, double xt, double y1, double y2) {
		double xdiff = x2 - x1;
		if (xdiff == 0) {
			return y1;
		}
		double xtdiff = xt - x1;
		double r = xtdiff / xdiff;

		double ydiff = y2 - y1;

		return r * ydiff + y1;
	}

	@Override
	public void calculateSignal(List<RTICalibrationData> calibrations, List<Feature> targets) {
		for (Feature target : targets) {
			Double rti = interpolateRTIfromRT(target.getRetentionTime(), calibrations);
			if (rti != null) {
				target.setRetentionTimeIndex(rti);
				target.setHenryConstant(deriveSignalFromRTI(rti, calibrations));
			}
		}
	}

	@Override
	public Double deriveSignalFromRTI(Double rti, List<RTICalibrationData> calibrations) {
		
		if(rti == null) {
			return null;
		}
		
		ArrayList<Double> logPs = new ArrayList<>();
		for (RTICalibrationData calibration : calibrations) {
			logPs.add(calibration.getSignal());
		}
		double min = Collections.min(logPs);
		double max = Collections.max(logPs);

		return min + (rti - MIN_INDEX) / (MAX_INDEX - MIN_INDEX) * (max - min);
	}

	@Override
	public void calculateRetentionTimeIndex(List<RTICalibrationData> calibrations) {
		if (calibrations.isEmpty()) {
			return;
		}

		double min = calibrations.get(0).getSignal();
		double max = min;
		for (int i = 1; i < calibrations.size(); i++) {
			double logD = calibrations.get(i).getSignal();
			if (logD < min) {
				min = logD;
			} else if (logD > max) {
				max = logD;
			}
		}

		for (RTICalibrationData data : calibrations) {
			if (Double.compare(data.getSignal(), min) == 0) {
				data.setRti(MIN_INDEX);
				continue;
			} else if (Double.compare(data.getSignal(), max) == 0) {
				data.setRti(MAX_INDEX);
				continue;
			}
			data.setRti(MIN_INDEX + (data.getSignal() - min) / (max - min) * (MAX_INDEX - MIN_INDEX));
		}
	}

	@Override
	public void filterInvalidRTs(List<RTICalibrationData> calibrations) {

		List<RTICalibrationData> sortedByLogD = calibrations.stream()
				.sorted(Comparator.comparingDouble(RTICalibrationData::getSignal))
				.collect(Collectors.toList());

		// Each retention time greater than the previous one will be discarded (invalidated)
		for(int i = 1; i < sortedByLogD.size(); i++) {
			if(sortedByLogD.get(i).getMeanRt() <= sortedByLogD.get(i-1).getMeanRt()) {
				sortedByLogD.get(i).setValid(false);
			}
		}
	}
}
