package de.hswt.fi.common;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * The Class RangeParser parses a given string of a given format, so that there
 * is a range with min and max value and a median value. The following formats
 * are supported are:
 *
 * 1,23 -1,23 &gt;1,23 &lt;1,23 1,23-3,21
 *
 * Integer values are also possible. Commas are replaced with points.
 *
 * @author Marco Luthardt
 */
public class RangeParser {

	/** The String to parse. */
	private String toParse;

	/** The min value. */
	private double lower;

	/** The max value. */
	private double upper;

	/** The median value. */
	private double median;

	/** The delta value. */
	private double delta;

	/** The valid value which is true if the string is valid. */
	private boolean isValid = false;

	/**
	 * Instantiates a new range parser.
	 */
	public RangeParser() {
	}

	/**
	 * Parses the given String.
	 *
	 * @param toParse
	 *            the string to parse
	 * @return true, if parsing was successful, otherwise false
	 */
	public boolean parse(String toParse) {
		if (toParse == null) {
			throw new NullPointerException("String to parse is null.");
		}
		this.toParse = toParse.replace(" ", "").replace(",", ".");
		parse();
		return isValid;
	}

	/**
	 * Internal method to parse the string. Initializes variables and calls
	 * other internal parse methods based on the given string.
	 */
	private void parse() {
		lower = 0.0;
		upper = 0.0;
		median = 0.0;
		delta = 0.0;
		isValid = false;
		if (toParse.startsWith(">")) {
			parseGreaterThen();
		} else if (toParse.startsWith("<")) {
			parseLowerThen();
		} else if (toParse.contains("-")) {
			parseRange();
		}
	}

	/**
	 * Parses lower then strings.
	 */
	private void parseLowerThen() {
		String post = toParse.replace("<", "");

		if (!NumberUtils.isCreatable(post)) {
			isValid = false;
			return;
		}

		double value = NumberUtils.toDouble(post) - Double.MIN_VALUE;

		median = value / 2;
		lower = 0.0;
		delta = median;
		upper = median + delta;
		isValid = true;
	}

	/**
	 * Parses greater then strings.
	 */
	private void parseGreaterThen() {
		String post = toParse.replace(">", "");

		if (!NumberUtils.isCreatable(post)) {
			isValid = false;
			return;
		}

		double value = NumberUtils.toDouble(post) + Double.MIN_VALUE;

		delta = (Double.MAX_VALUE / 2 - value) / 2;
		median = value + delta;
		lower = value;
		upper = median + delta;
		isValid = true;
	}

	/**
	 * Parses range strings.
	 */
	private void parseRange() {

		boolean isNegative = false;
		if (toParse.startsWith("-")) {
			toParse = toParse.replaceFirst("-", "");
			isNegative = true;
		}

		String[] strings = toParse.split("-");

		if (strings.length != 2 || !NumberUtils.isCreatable(strings[0])
				|| !NumberUtils.isCreatable(strings[1])) {
			isValid = false;
			return;
		}

		double lower = NumberUtils.toDouble(strings[0]);
		double upper = NumberUtils.toDouble(strings[1]);

		if (!isNegative && lower >= upper) {
			isValid = false;
			return;
		}

		this.lower = isNegative ? -lower : lower;
		this.upper = upper;
		delta = (upper - lower) / 2;
		median = lower + delta;

		isValid = true;
	}

	/**
	 * Gets the min value.
	 *
	 * @return the min value
	 */
	public double getMin() {
		return lower;
	}

	/**
	 * Gets the max value.
	 *
	 * @return the max value
	 */
	public double getMax() {
		return upper;
	}

	/**
	 * Gets the median value.
	 *
	 * @return the median value
	 */
	public double getMedian() {
		return median;
	}

	/**
	 * Gets the delta value which is the difference of the median value and the
	 * min or max value.
	 *
	 * @return the delta value
	 */
	public double getDelta() {
		return delta;
	}

	/**
	 * Checks if the parsed string is valid.
	 *
	 * @return true, if the parsed string is valid, otherwise false
	 */
	public boolean isValid() {
		return isValid;
	}
}
