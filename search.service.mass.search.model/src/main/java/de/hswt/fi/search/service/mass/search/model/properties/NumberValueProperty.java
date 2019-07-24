package de.hswt.fi.search.service.mass.search.model.properties;

import de.hswt.fi.search.service.mass.search.model.Property;

public interface NumberValueProperty extends Property<NumberValueProperty> {


	Double getValue();

	void setValue(Double value);

	Double getPh();

	void setPh(Double ph);

	Integer getCharge();

	void setCharge(Integer charge);

	boolean hasChanged(NumberValueProperty property);

}
