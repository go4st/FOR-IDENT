package de.hswt.fi.ui.vaadin.grid;

/**
 * Created by luthardt on 4/27/17.
 */
public class IntegerFilterField extends AbstractNumberFilterField<Integer> {

	@Override
	protected Integer getConvertedValue(String text) {
		Integer result = null;
		try {
			result = Integer.parseInt(text);
		} catch (NumberFormatException e) {}
		return result;
	}
}
