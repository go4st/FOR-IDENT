package de.hswt.fi.ui.vaadin.mschart;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.JavaScriptFunction;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import elemental.json.impl.JreJsonFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
@author August Gilg
 **/

@JavaScript({"js-ms-chart.js", "js-ms-chart-connector.js"})
public class MsChart extends AbstractJavaScriptComponent {

	public interface ValueChangeListener extends Serializable {
		void valueChange();
	}

	private ArrayList<ValueChangeListener> listeners = new ArrayList<>();

	private List<MsSeries> data;

	public MsChart() {
		this(new ArrayList<>(), new MsChartConfig());
	}

	public MsChart(List<MsSeries> data, MsChartConfig config) {

		this.data = data;
		getState().data = getDataJsonValue();
		setConfig(config);

		addFunction("onValueClick", (JavaScriptFunction) arguments -> {
			getState().selectedId = arguments.getString(0);
			for (ValueChangeListener listener : listeners)
				listener.valueChange();
		});
	}

	public void addValueChangeListener(ValueChangeListener listener) {
		listeners.add(listener);
	}

	public void setSeries(MsSeries msSeries) {
		data = Collections.singletonList(msSeries);
		getState().data = getDataJsonValue();
	}

	public void addSeries(MsSeries msSeries) {
		data.add(msSeries);
		getState().data = getDataJsonValue();
	}

	public void setConfig(MsChartConfig config) {
		getState().config = config.getJsonValue();
	}

	private JsonValue getDataJsonValue() {
		JsonObject dataObject = new JreJsonFactory().createObject();
		data.forEach(series -> dataObject.put(series.getId(), series.getJsonValue()));
		return dataObject;
	}

	public MsSeries.Point getValue() {
		return findValue(getState().selectedId);
	}

	public MsSeries.Point findValue(String id) {
		return data.stream().flatMap(series -> series.getPoints().stream())
				.filter(point -> point.getId().equals(id))
				.findFirst()
				.orElse(null);
	}

	public MsSeries.Point findValue(double x, double y) {
		return data.stream().flatMap(series -> series.getPoints().stream())
				.filter(point -> BigDecimal.valueOf(point.getX()).equals(BigDecimal.valueOf(x)) &&
						BigDecimal.valueOf(point.getY()).equals(BigDecimal.valueOf(y)))
				.findFirst()
				.orElse(null);
	}

	public void select(MsSeries.Point point) {
		getState().selectedId = point.getId();
	}

	@Override
	protected MsChartState getState() {
		return (MsChartState) super.getState();
	}

}
