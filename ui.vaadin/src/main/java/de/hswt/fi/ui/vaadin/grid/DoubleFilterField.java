package de.hswt.fi.ui.vaadin.grid;

/**
 * Created by luthardt on 4/27/17.
 */
public class DoubleFilterField extends AbstractNumberFilterField<Double> {

	@Override
	protected Double getConvertedValue(String text) {
		Double result = null;
		try {
			result = Double.parseDouble(text);
		} catch (NumberFormatException e) {}
		return result;
	}
}
