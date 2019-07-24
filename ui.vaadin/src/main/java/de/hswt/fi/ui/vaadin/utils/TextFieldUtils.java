package de.hswt.fi.ui.vaadin.utils;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import de.hswt.fi.common.RangeParser;
import de.hswt.fi.search.service.mass.search.model.SearchParameter;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.Assert;

public final class TextFieldUtils {

	private static final RangeParser rangeParser = new RangeParser();

	private TextFieldUtils() {
		// prevent Instantiation
	}

	public static boolean parseMassSearchValue(final SearchParameter searchParameter, final TextField massTextField, ComboBox ppmComboBox) {
		validateParameter(searchParameter, massTextField, ppmComboBox);

		String mass = massTextField.getValue();
		if (rangeParser.parse(mass)) {
			searchParameter.setAccurateMassRangeMin(rangeParser.getMin());
			searchParameter.setAccurateMassRangeMax(rangeParser.getMax());
			return true;
		} else if (NumberUtils.isCreatable(mass)) {
			searchParameter.setAccurateMass(Double.parseDouble(mass));
			searchParameter.setPpm((Double) ppmComboBox.getValue());
			return true;
		} else {
			return false;
		}
	}

	public static boolean parseLogSearchValue(final SearchParameter searchParameter, final TextField textField, ComboBox logPDeltaCombobox) {
		validateParameter(searchParameter, textField, logPDeltaCombobox);

		String logP = textField.getValue();
		if (rangeParser.parse(logP)) {
			searchParameter.setLogPRangeMin(rangeParser.getMin());
			searchParameter.setLogPRangeMax(rangeParser.getMax());
			return true;
		} else if (NumberUtils.isCreatable(logP)) {
			searchParameter.setLogP(Double.parseDouble(logP));
			searchParameter.setLogPDelta((Double) logPDeltaCombobox.getValue());
			return true;
		} else {
			return false;
		}
	}

	private static void validateParameter(final SearchParameter searchParameter, final TextField massTextField, ComboBox ppmComboBox) {
		Assert.notNull(searchParameter, "SearchParameter must not be null");
		Assert.notNull(massTextField, "TextField must not be null");
		Assert.notNull(ppmComboBox, "Combobox must not be null");
	}
}
