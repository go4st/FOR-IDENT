package de.hswt.fi.search.service.mass.search.model.properties;

/**
 * The Class JPAStringProperty is the abstract base class for all string values
 * in the DB.
 *
 * @author Marco Luthardt
 */
public abstract class AbstractStringProperty implements StringValueProperty {

	public AbstractStringProperty() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(StringValueProperty o) {
		return this.getValue().compareTo(o.getValue());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hswt.riskident.si.model.SIStringValueProperty#hasChanged(de.hswt.
	 * riskident.si.model.SIStringValueProperty)
	 */
	@Override
	public boolean hasChanged(StringValueProperty property) {
		if (property == null) {
			throw new IllegalArgumentException("Argument property mus not be null.");
		}
		return hashCode() != property.hashCode();
	}

	@Override
	public String toString() {
		return getValue() != null ? getValue() : "";
	}
}
