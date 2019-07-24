package de.hswt.fi.ui.vaadin.mschart;

import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import elemental.json.impl.JreJsonFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 @author August Gilg
 **/

public class MsSeries {

	public static class Point {

		private final String id;

		private Double x;

		private Double y;

		public Point(Double x, Double y) {
			id = UUID.randomUUID().toString();
			this.x = x;
			this.y = y;
		}

		public String getId() {
			return id;
		}

		public Double getX() {
			return x;
		}

		public Double getY() {
			return y;
		}

		public JsonValue getJsonValue() {
			JsonObject jsonObject = new JreJsonFactory().createObject();
			jsonObject.put("id", id);
			jsonObject.put("x", x);
			jsonObject.put("y", y);
			return jsonObject;
		}
	}

	private final String name;

	private final String id;

	private String color;

	private List<Point> points;

	public MsSeries(String name) {
		this.name = name;
		id = UUID.randomUUID().toString();
		points = new ArrayList<>();
		color = "";
	}

	public JsonValue getJsonValue() {

		JsonObject jsonObject = new JreJsonFactory().createObject();

		jsonObject.put("name", name);
		jsonObject.put("id", id);
		jsonObject.put("color", color);

		JsonArray pointArray = new JreJsonFactory().createArray();
		jsonObject.put("points", pointArray);
		int index = 0;
		for (MsSeries.Point point : points) {
			pointArray.set(index, point.getJsonValue());
			index++;
		}

		return jsonObject;
	}

	public String getId() {
		return id;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}

	public void addPoint(Point point) {
		points.add(point);
	}

	public void clearPoints() {
		points.clear();
	}


}
