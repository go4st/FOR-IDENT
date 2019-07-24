package de.hswt.fi.search.service.mass.search.model.properties;

/**
 * The Class JPANumberProperty is the abstract base class for all number values
 * in the DB.
 *
 * @author Marco Luthardt
 */
public abstract class AbstractNumberProperty implements NumberValueProperty {

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(NumberValueProperty o) {
		return Double.compare(this.getPh(), o.getPh());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hswt.riskident.si.model.SINumberValueProperty#hasChanged(de.hswt.
	 * riskident.si.model.SINumberValueProperty)
	 */
	@Override
	public boolean hasChanged(NumberValueProperty property) {
		if (property == null) {
			throw new IllegalArgumentException("Argument property mus not be null.");
		}
		return hashCode() != property.hashCode();
	}

	@Override
	public String toString() {
		return getValue() != null ? getValue().toString() : "";
	}
}
