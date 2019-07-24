package de.hswt.fi.common;

import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

/**
 * The Class inputFormatter provides several decimal formatter to provide
 * standard formats of number inputs.
 *
 * @author Marco Luthardt
 * @author Tobias Placht
 * @author August Gilg
 */
public final class ValueFormatUtil {

	private static final String ACCURATE_MASS_PATTERN = "#0.0000";

	private static final String LOG_P_FORMAT = "#0.00";

	private static final String PH_FORMAT = "#0.0";

	private static final String RTI_FORMAT = "#0.0";

	private static final String RT_FORMAT = "#0.0";

	private ValueFormatUtil() {
		throw new IllegalStateException("Static utility class should not be initialized");
	}

	public static String formatForMass(Double input) {
		if (input == null) {
			return "";
		}
		return formatForAccurateMass(input, Locale.getDefault());
	}

	public static String formatForAccurateMass(Double value, Locale locale) {
		validateInput(value, locale);
		return formatFor(value, locale, ACCURATE_MASS_PATTERN);
	}

	public static String formatForLog(Double input, Locale locale) {
		validateInput(input, locale);
		return formatFor(input, locale, LOG_P_FORMAT);
	}

	public static String formatForPh(Double input, Locale locale) {
		validateInput(input, locale);
		return formatFor(input, locale, PH_FORMAT);
	}

	public static String formatForRti(Double input, Locale locale) {
		validateInput(input, locale);
		return formatFor(input, locale, RTI_FORMAT);
	}

	public static String formatForRt(Double input, Locale locale) {
		validateInput(input, locale);
		return formatFor(input, locale, RT_FORMAT);
	}


	private static String formatFor(Double input, Locale locale, String pattern) {
		NumberFormat numberFormat = NumberFormat.getInstance(locale);
		DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
		decimalFormat.applyPattern(pattern);
		return decimalFormat.format(input);
	}

	/** Method to roundMass Double values precisely to a certain amount of floating point digits.
	 * 
	 * @param value Double value to be rounded
	 * @param maxDigits Number of digits to roundMass
	 * @return the rounded value
	 */
	public static Double round(Double value, int maxDigits) {
		if(value == null || value.isNaN() || maxDigits < 1) {
			return value;
		}
		return new BigDecimal(value.toString()).setScale(maxDigits,RoundingMode.HALF_UP).doubleValue();
	}
	
	public static Double roundMass(Double value) {
		return round(value, 4);
	}

	public static Double roundRT(Double value) {
		return round(value, 2);
	}

	public static Double roundLogD(Double value) {
		return round(value, 3);
	}

	private static void validateInput(Double input, Locale locale) {
		Assert.notNull(input, "input must not be empty");
		Assert.notNull(locale, "Locale must not be null");
	}

	public static String getDateAsString(long date) {
		DateFormat format = DateFormat.getDateInstance(DateFormat.DEFAULT);
		return format.format(new Date(date));
	}

	public static String getDateAsString(Date date) {
		DateFormat format = DateFormat.getDateInstance(DateFormat.DEFAULT);
		return format.format(date);
	}
}
