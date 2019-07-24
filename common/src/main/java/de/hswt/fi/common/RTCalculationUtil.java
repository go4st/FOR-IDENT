package de.hswt.fi.common;
import java.util.List;

public class RTCalculationUtil {

	private RTCalculationUtil() {
		throw new IllegalStateException("Static utility class should not be initialized");
	}

	public static Double getMeanRt(List<Double> rts) {

		double sum = 0.0d;

		if (rts.isEmpty()) {
			return null;
		}

		int count = 0;

		for (Double rt : rts) {
			if (rt >= 0.0d) {
				sum += rt;
				count++;
			}
		}

		if (sum >= 0.0 && count > 0) {
			return ValueFormatUtil.roundRT(sum / count);
		}

		return null;
	}
}
