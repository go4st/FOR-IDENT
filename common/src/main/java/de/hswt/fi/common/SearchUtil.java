package de.hswt.fi.common;

/**
 * The Class SearchUtil is used to calculate the search range for masses out of
 * the ppm value. It also provided the mass values for H-atoms.
 * 
 * @author Marco Luthardt
 */
public final class SearchUtil {

	private SearchUtil() {
		throw new IllegalStateException("Static utility class should not be initialized");
	}

	/** The Constant H_MASS. */
	static final double H_MASS = 1.0078;

	/**
	 * Returns the +/- range value based on the mass and ppm. The value must be
	 * added and subtracted to get the search range.
	 *
	 * @param mass
	 *            the mass to search for
	 * @param ppm
	 *            the ppm value
	 * @return the range from ppm
	 */
	public static synchronized double getRangeFromPPM(double mass, double ppm) {
		return Math.abs(mass * ppm / 1000000.0);
	}

	/**
	 * Returns the mass value of the ionisation (mass of +H, -H or 0).
	 *
	 * @param ionisation
	 *            the ionisation id or constant
	 * @return the ionisation mass value, which is +H, -H or 0, based on the
	 *         given ionisation constant
	 */
	public static synchronized double getIonisation(Ionisation ionisation) {

		if (ionisation == null) {
			return 0.0;
		}
		switch (ionisation) {
		case POSITIVE_IONISATION:
			return -H_MASS;
		case NEGATIVE_IONISATION:
			return H_MASS;
		default:
			return 0.0;
		}
	}
}
