package de.hswt.fi.ui.vaadin.factories;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import de.hswt.fi.common.Ionisation;
import de.hswt.fi.common.StationaryPhase;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.HtmlDescriptionUtil;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.components.CollapsibleLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.I18N;

import java.util.*;

public class ComponentFactory {

	private static final String MASS_TEXT_FIELD_DESCRIPTION = "massTextFieldDescription_";

	private static final String LOGD_TEXT_FIELD_DESCRIPTION = "logDTextFieldDescription_";

	private static final int COMBOBOX_PAGE_LENGT = 10;

	private I18N i18n;

	public Button createButton(String caption, String description) {
		return createButton(caption, null, description, true);
	}

	public Button createButton(String caption) {
		return createButton(caption, null, null, true);
	}

	public Button createButton(String caption, Resource icon) {
		return createButton(caption, icon, null, true);
	}

	public Button createButton(String caption, Resource icon, String description) {
		return createButton(caption, icon, description, true);
	}

	public Button createButton(Resource icon) {
		return createButton(null, icon, null, true);
	}

	public Button createButton(Resource icon, String description) {
		return createButton(null, icon, description, true);
	}

	public Button createButton(Resource icon, boolean withMargin) {
		return createButton(null, icon, null, withMargin);
	}

	public Button createButton(Resource icon, String description, boolean withMargin) {
		return createButton(null, icon, description, withMargin);
	}

	public Button createButton(String caption, boolean withRightMargin) {
		return createButton(caption, null, null, withRightMargin);
	}

	public Button createButton(String caption, Resource icon, String description, boolean withRightMargin) {
		Button button = new Button();
		button.addStyleName(CustomValoTheme.BACKGROUND_COLOR_ALT3);
		button.addStyleName(CustomValoTheme.BORDER_NONE);

		if (caption != null) {
			button.setCaption(caption);
		}
		if (icon != null) {
			button.setIcon(icon);
		}
		if (description != null) {
			button.setDescription(description);
		}
		if (withRightMargin) {
			button.addStyleName(CustomValoTheme.MARGIN_HALF_RIGHT);
		}

		return button;
	}

	public ComboBox<Double> createPhComboBox() {

		Double[] values = {3.0, 5.0, 7.0, 9.0};

		ComboBox<Double> phCombobox = new ComboBox<>("pH");
		phCombobox.setTextInputAllowed(false);
		phCombobox.setItems(values);
		phCombobox.setSelectedItem(values[0]);
		phCombobox.setWidth("100%");

		return phCombobox;
	}

	public ComboBox<Double> createPpmComboBox() {

		Double[] values = {1.0, 2.0, 5.0, 10.0, 20.0, 50.0, 100.0, 500.0, 1000.0, 1500.0, 2000.0, 5000.0};

		ComboBox<Double> ppmComboBox = new ComboBox<>("ppm");
		ppmComboBox.setTextInputAllowed(false);
		ppmComboBox.setItems(values);
		ppmComboBox.setValue(values[0]);
		ppmComboBox.setPageLength(COMBOBOX_PAGE_LENGT);
		ppmComboBox.setWidth("100%");

		return ppmComboBox;
	}

	public ComboBox<Double> createLogXRangeComboBox() {

		Set<Double> values = new HashSet<>();

		for (double i = 0.0; i <= 1.0; i += 0.1) {
			values.add(i);
		}

		ComboBox<Double> rangeComboBox = new ComboBox<>("Delta");
		rangeComboBox.setTextInputAllowed(false);
		rangeComboBox.setItems(values);
		rangeComboBox.setValue(values.iterator().next());
		rangeComboBox.setPageLength(COMBOBOX_PAGE_LENGT);

		return rangeComboBox;
	}

	public ComboBox<Ionisation> createIonisationComboBox() {

		ComboBox<Ionisation> ionisationComboBox = new ComboBox<>(
				i18n.get(UIMessageKeys.IONISATION_COMBO_BOX_CAPTION));
		ionisationComboBox.setItems(EnumSet.allOf(Ionisation.class));
		ionisationComboBox.setTextInputAllowed(false);
		ionisationComboBox.setWidth("100%");

		return ionisationComboBox;
	}

	public ComboBox<Double> createDoubleValueComboBox(double min, double max, double steps) {

		Set<Double> values = new HashSet<>();

		for (double value = min; value <= max; value += steps) {
			values.add(value);
		}

		ComboBox<Double> comboBox = new ComboBox<>();
		comboBox.setTextInputAllowed(false);
		comboBox.setItems(values);
		comboBox.setValue(values.iterator().next());

		return comboBox;
	}

	public ComboBox<StationaryPhase> createStationaryPhaseComboBox() {

        Set<StationaryPhase> values = new HashSet<>(Arrays.asList(StationaryPhase.values()));

		ComboBox<StationaryPhase> stationaryPhaseComboBox = new ComboBox<>(
				i18n.get(UIMessageKeys.STATIONARY_PHASE_COMBO_BOX_CAPTION));
		stationaryPhaseComboBox.setItems(values);
		stationaryPhaseComboBox.setTextInputAllowed(false);
		stationaryPhaseComboBox.setWidth("100%");

		return stationaryPhaseComboBox;
	}

	public CheckBoxGroup<String> createHalogenOptionGroup() {

		String[] values = {"Cl", "Br", "F", "I"};

		CheckBoxGroup<String> checkBoxGroup = new CheckBoxGroup<>();
		checkBoxGroup.setItems(values);

		return checkBoxGroup;
	}

    public CssLayout createHalogenRow(CheckBoxGroup<String> halogenOptionGroup) {
        CssLayout borderLayout = new CssLayout();
        borderLayout.setCaption(i18n.get(UIMessageKeys.SEARCH_FORM_HALOGEN_LAYOUT_CAPTION));
        borderLayout.addStyleName(CustomValoTheme.LAYOUT_BORDER);
        borderLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_CONTENT_BOX);
        borderLayout.addComponent(halogenOptionGroup);

        return createRowLayout(borderLayout);
    }

	public CssLayout createRowLayout() {
		CssLayout rowLayout = new CssLayout();
		rowLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_ROW);
		return rowLayout;
	}

	public CssLayout createRowLayout(Component... components) {
		CssLayout rowLayout = createRowLayout();
		rowLayout.addStyleName(CustomValoTheme.MARGIN_HALF_VERTICAL);
		rowLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_ROW);
		for (int i = 0; i < components.length; i++) {
			Component component = components[i];
			if (component.getCaption() != null) {
				CssLayout tempLayout = new CssLayout();
				tempLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_COLUMN);
				tempLayout.addComponent(component);
				component = tempLayout;
			}
			component.addStyleName(CustomValoTheme.MARGIN_HALF_HORIZONTAL);
			if (i == 0) {
				component.addStyleName(CustomValoTheme.FLEX_ITEM_EXPAND);
			} else {
				component.addStyleName(CustomValoTheme.FLEX_ITEM);
			}
			rowLayout.addComponent(component);
		}
		return rowLayout;
	}

	public TextField createTextField(String caption) {
		TextField textField = new TextField();
		textField.setCaption(caption);
		textField.setWidth("100%");

		return textField;
	}

	public TextField createTextField(String caption, int maxLength) {
		TextField textField = new TextField();
		textField.setCaption(caption + " (max." + maxLength + ")");
		textField.setWidth("100%");
		textField.setMaxLength(maxLength);
		return textField;
	}

	public TextArea createTextArea(String caption, int maxLength) {
		TextArea textArea = new TextArea();
		textArea.setCaption(caption + " (max." + maxLength + ")");
		textArea.setWidth("100%");
		textArea.setMaxLength(maxLength);
		return textArea;
	}

	public Label createSpacer() {
		Label spacerLabel = new Label();
		spacerLabel.setHeight("1rem");
		return spacerLabel;
	}

	public Label createSpacer(String height) {
		Label spacerLabel = new Label();
		spacerLabel.setHeight(height);
		return spacerLabel;
	}

	public Label createDividingLine() {
		Label line = new Label();
		line.setHeight("1px");
		line.setPrimaryStyleName(CustomValoTheme.HORIZONTAL_LINE);
		line.addStyleName(CustomValoTheme.MARGIN_TOP);
		line.addStyleName(CustomValoTheme.MARGIN_BOTTOM);
		return line;
	}

	public BrowserFrame getThemeResourceBrowserFrame(String germanHelpLocation, String defaultHelpLocation) {
		Locale locale = VaadinSession.getCurrent().getLocale();
		BrowserFrame browser;
		if (locale.equals(Locale.GERMANY) || locale.equals(Locale.GERMAN)) {
			browser = new BrowserFrame(null, new ThemeResource(germanHelpLocation));
		} else {
			browser = new BrowserFrame(null, new ThemeResource(defaultHelpLocation));
		}
		browser.setSizeFull();
		return browser;
	}

	public TextField createMassTextField() {
        return getHtmlDescriptionTextField(UIMessageKeys.SEARCH_FORM_MASS_TEXT_FIELD_CAPTION, MASS_TEXT_FIELD_DESCRIPTION);
    }

	public TextField createLogPTextField() {
        return getHtmlDescriptionTextField(UIMessageKeys.SEARCH_FORM_LOG_TEXT_FIELD_CAPTION, LOGD_TEXT_FIELD_DESCRIPTION);
    }

    private TextField getHtmlDescriptionTextField(String textFieldCaption, String textFieldDescription) {
        TextField logPTextField = new TextField();
        logPTextField.setCaption(i18n.get(textFieldCaption));

        String logPTextFieldDescription = HtmlDescriptionUtil
                .getDescription(textFieldDescription, UI.getCurrent().getLocale());

        logPTextField.setDescription(logPTextFieldDescription, ContentMode.HTML);
        logPTextField.setWidth("100%");
        return logPTextField;
    }

    public CollapsibleLayout createCollapseableLayout(String caption, Component content, boolean collapseAble, boolean collapsed) {
		return new CollapsibleLayout(caption, content, collapseAble, collapsed);
	}

	public ComboBox<Double> createScoreComboBox() {

		Set<Double> values = new HashSet<>();

		for (double i = 0.0; i <= 5.1; i += 0.2) {
			values.add(i);
		}

		ComboBox<Double> rangeComboBox = new ComboBox<>();
		rangeComboBox.setTextInputAllowed(false);
		rangeComboBox.setItems(values);
		rangeComboBox.setValue(values.iterator().next());
		rangeComboBox.setPageLength(5);

		return rangeComboBox;
	}

	public Label createHorizontalLine() {
		Label lineLabel = new Label();
		lineLabel.setWidth("100%");
		lineLabel.setHeight("0.15rem");
		lineLabel.addStyleName(CustomValoTheme.BLOCK);
		lineLabel.addStyleName(CustomValoTheme.BACKGROUND_COLOR_GRADIENT_ALT3);
		return lineLabel;
	}

	public Label createHorizontalLine(String height) {
		Label lineLabel = new Label();
		lineLabel.setWidth("100%");
		lineLabel.setHeight(height);
		lineLabel.addStyleName(CustomValoTheme.BLOCK);
		lineLabel.addStyleName(CustomValoTheme.BACKGROUND_COLOR_GRADIENT_ALT3);
		return lineLabel;
	}

	public TextField createColumnTextFilterField() {
		TextField filter = new TextField();
		filter.setWidth("100%");
		filter.setPlaceholder("Filter");
		return filter;
	}

	public String getCheckedIconHtml(boolean checked) {
		return checked ? VaadinIcons.CHECK_SQUARE.getHtml()
				: VaadinIcons.CLOSE.getHtml();
	}

	public String getBooleanIconHtml(boolean bool) {
		return bool ? VaadinIcons.CIRCLE.getHtml() : "";
	}

	public ComboBox<Boolean> createColumnBooleanFilterField() {
		ComboBox<Boolean> comboBox = new ComboBox<>();
		comboBox.setWidth("100%");
		comboBox.setItems(true, false);
		comboBox.setItemCaptionGenerator(item -> item ? i18n.get(UIMessageKeys.USER_ENABLED_CAPTION) : i18n.get(UIMessageKeys.USER_DISABLED_CAPTION));
		comboBox.setItemIconGenerator(item -> item ? VaadinIcons.CHECK_SQUARE : VaadinIcons.CLOSE);
		return comboBox;
	}

	@Autowired
	public void setI18n(I18N i18n) {
		this.i18n = i18n;
	}
}