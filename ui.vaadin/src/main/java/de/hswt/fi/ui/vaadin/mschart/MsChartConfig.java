package de.hswt.fi.ui.vaadin.mschart;

import elemental.json.JsonObject;
import elemental.json.JsonValue;
import elemental.json.impl.JreJsonFactory;

/**
 @author August Gilg
 **/

public class MsChartConfig {

	private String axisCaptionX = "";

	private String axisCaptionY = "";

	private int floatingDigits = 3;

	private int barStrokeWidth = 5;

	private int axisOffsetX = 55;

	private int axisOffsetY = 50;

	private String axisColor = "darkgray";

	private String selectionColor = "red";

	private Double hoverOpacity = .9d;

	private String hoverColor = "#f4bc42";

	private String textHoverColor = "black";

	public JsonValue getJsonValue() {

		JsonObject jsonObject = new JreJsonFactory().createObject();

		jsonObject.put("axisCaptionX", axisCaptionX);
		jsonObject.put("axisCaptionY", axisCaptionY);
		jsonObject.put("floatingDigits", floatingDigits);
		jsonObject.put("barStrokeWidth", barStrokeWidth);
		jsonObject.put("axisOffsetX", axisOffsetX);
		jsonObject.put("axisOffsetY", axisOffsetY);
		jsonObject.put("axisColor", axisColor);
		jsonObject.put("selectionColor", selectionColor);
		jsonObject.put("hoverOpacity", hoverOpacity);
		jsonObject.put("hoverColor", hoverColor);
		jsonObject.put("textHoverColor", textHoverColor);

		return jsonObject;
	}

	public MsChartConfig withAxisCaptionX(String axisCaptionX) {
		this.axisCaptionX = axisCaptionX;
		return this;
	}

	public MsChartConfig withAxisCaptionY(String axisCaptionY) {
		this.axisCaptionY = axisCaptionY;
		return this;
	}

	public MsChartConfig withFloatingDigits(int floatingDigits) {
		this.floatingDigits = floatingDigits;
		return this;
	}

	public MsChartConfig withBarStrokeWidth(int barStrokeWidth) {
		this.barStrokeWidth = barStrokeWidth;
		return this;
	}

	public MsChartConfig withAxisOffset(int axisOffset) {
		this.axisOffsetX = axisOffset;
		this.axisOffsetY = axisOffset;
		return this;
	}

	public MsChartConfig withAxisOffsetX(int axisOffsetX) {
		this.axisOffsetX = axisOffsetX;
		return this;
	}

	public MsChartConfig withAxisOffsetY(int axisOffsetY) {
		this.axisOffsetY = axisOffsetY;
		return this;
	}

	public MsChartConfig withSelectionColor(String selectionColor) {
		this.selectionColor = selectionColor;
		return this;
	}

	public MsChartConfig withAxisColor(String axisColor) {
		this.axisColor = axisColor;
		return this;
	}

	public MsChartConfig withHoverOpacity(Double hoverOpacity) {
		this.hoverOpacity = hoverOpacity;
		return this;
	}

	public MsChartConfig withHoverColor(String hoverColor) {
		this.hoverColor = hoverColor;
		return this;
	}

	public MsChartConfig withTextHoverColor(String textHoverColor) {
		this.textHoverColor = textHoverColor;
		return this;
	}
}
