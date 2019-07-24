package de.hswt.fi.beans;

import java.util.Map;

/**
 * This class is used as definition of a bean component, which is declared via
 * {@link de.hswt.fi.beans.annotations.BeanComponent}. A bean component is a
 * class, where at least on field should be a
 * {@link de.hswt.fi.beans.BeanColumnDefinition}.
 *
 * The class is then a collection of bean columns. It contains then all bean
 * columns and nested columns, which are columns which are part of another
 * column.
 *
 * @see de.hswt.fi.beans.BeanColumnDefinition
 * @see de.hswt.fi.beans.annotations.BeanComponent
 *
 * @author Marco Luthardt
 */
public class BeanComponentDefinition {

	/**
	 * The type of the bean component.
	 */
	private Class<?> type;

	/**
	 * Map of all bean columns (incl. nested ones), with their property id as
	 * key.
	 */
	private Map<String, BeanColumnDefinition> beanColumnDefinitions;

	public BeanComponentDefinition(Class<?> type,
			Map<String, BeanColumnDefinition> beanColumnDefinitions) {
		if (type == null || beanColumnDefinitions == null) {
			throw new NullPointerException("Parameters must not be null.");
		}
		this.type = type;
		this.beanColumnDefinitions = beanColumnDefinitions;
	}

	public Class<?> getType() {
		return type;
	}

	public Map<String, BeanColumnDefinition> getBeanColumnDefinitions() {
		return beanColumnDefinitions;
	}

	public BeanColumnDefinition getBeanColumnDefinition(String key) {
		return beanColumnDefinitions.get(key);
	}

	@Override
	public String toString() {
		return "BeanComponentDefinition [" + (type != null ? "type=" + type + ", " : "")
				+ (beanColumnDefinitions != null ? "beanColumnDefinitions=" + beanColumnDefinitions
						: "")
				+ "]";
	}
}
