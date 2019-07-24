package de.hswt.fi.ui.vaadin.mschart;

import com.vaadin.shared.ui.JavaScriptComponentState;
import elemental.json.JsonValue;

/**
 @author August Gilg
 **/

public class MsChartState extends JavaScriptComponentState {

	public JsonValue data;

	public JsonValue config;

	public String selectedId;

}
