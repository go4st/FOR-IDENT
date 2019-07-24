package de.hswt.fi.calculation.service.rti.api;

import de.hswt.fi.model.Feature;
import de.hswt.fi.model.RTICalibrationData;

import java.util.List;

/**
 * The Interface RTICalculationService defines methods to execute calculations
 * for the RTI algorithm.
 * 
 * @author Marco Luthardt
 */
public interface RTICalculationService {

	/**
	 * Interpolate a RTI value from rt (retention time) derived from the given
	 * calibration data.
	 *
	 * @param rt
	 *            the retention time
	 * @param calibration
	 *            the calibration data used to interpolate a RTI value
	 * @return the RTI based on the rt and the calibration data, or null if the
	 *         interpolation could not be performed
	 */
	 Double interpolateRTIfromRT(Double rt, List<RTICalibrationData> calibration);

	/**
	 * Interpolate a logD value from rt (retention time) derived from the given
	 * calibration data.
	 *
	 * @param rt
	 *            the retention time
	 * @param calibration
	 *            the calibration data used to interpolate a logD value
	 * @return the logD based on the rt and the calibration data, or null if the
	 *         interpolation could not be performed
	 */
	 Double interpolateLogD(Double rt, List<RTICalibrationData> calibration);

	/**
	 * Interpolate a value for yt, x1/y1 and x2/y2 define two points in a 2D
	 * space, where xt is a value on the line between this two points. The
	 * method returns the corresponding yt value, so that the point xt/yt is on
	 * the line.
	 *
	 * @param x1
	 *            the x coordinate for point 1
	 * @param x2
	 *            the x coordinate for point 2
	 * @param xt
	 *            the x coordinate for target point
	 * @param y1
	 *            the y coordinate for point 1
	 * @param y2
	 *            the y coordinate for point 2
	 * @return the y coordinate for the target point
	 */
	 Double interpolate(double x1, double x2, double xt, double y1, double y2);

	/**
	 * Calculates (predicts) logD value in the targets, based on the
	 * calibrations list. Therefore the RTI for each target is computed. From
	 * this RTI value the logD value of each target is predicted.
	 *
	 * @param calibrations
	 *            the calibrations on which the calculations are based
	 * @param targets
	 *            the targets to calculate the RTI and logD
	 */
	 void calculateLogD(List<RTICalibrationData> calibrations, List<Feature> targets);

	/**
	 * Derive (calculates) a logD value based on a RTI value and the calibration
	 * data.
	 *
	 * @param rti
	 *            the RTI value
	 * @param calibrations
	 *            the calibrations on which the calculations are based
	 * @return the derived logD value
	 */
	 Double deriveLogDFromRTI(Double rti, List<RTICalibrationData> calibrations);

	/**
	 * Calculates the RTI (Retention Time index) of the calibrations data. For
	 * the calculation, the min and max retention time of the calibrations data
	 * is used to define a line in a 2D space. The two calibrations data gets
	 * the min and max RTI value assigned. The other calibrations data are
	 * filled with the interpolated RTI values.
	 *
	 * @param calibrations
	 *            the calibrations data to calculate the RTI for
	 */
	 void calculateRetentionTimeIndex(List<RTICalibrationData> calibrations);

	/**
	 * Invalidates the retention time values for the set of calibration substances
	 * where the retention time of a substance is lower than the retention time of
	 * a substance with lower logD value.
	 *
	 * @param calibrations
	 * 				the calibrations data to be filtered by invalid retention times
	 */
	void filterInvalidRTs(List<RTICalibrationData> calibrations);
}
