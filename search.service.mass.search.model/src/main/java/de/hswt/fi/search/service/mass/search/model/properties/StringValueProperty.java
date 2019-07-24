package de.hswt.fi.search.service.mass.search.model.properties;

import de.hswt.fi.search.service.mass.search.model.Property;

public interface StringValueProperty extends Property<StringValueProperty> {

	String getValue();

	void setValue(String value);

	boolean hasChanged(StringValueProperty property);

}
