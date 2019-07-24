package de.hswt.fi.beans;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class is an registry for all
 * {@link de.hswt.fi.beans.BeanComponentDefinition bean components}. The
 * registry should be filled at lifecycle start an shoul be used all over the
 * program only ones.
 *
 * @author Marco Luthardt
 */
@Component
public class BeanComponentRegistry {

	/**
	 * A map with all bean components with their type as key.
	 */
	private Map<Class<?>, BeanComponentDefinition> beanComponentDefinitions;

	public BeanComponentRegistry() {
		beanComponentDefinitions = new HashMap<>();
	}

	/**
	 * Adds an new bean component to the registry.
	 *
	 * @param definition
	 *            the definition of the bean component to add
	 */
	protected void addBeanComponentDefinition(BeanComponentDefinition definition) {
		Objects.requireNonNull(definition, "Parameter definition is null.");
		beanComponentDefinitions.put(definition.getType(), definition);
	}

	/**
	 * Method to get the bean definition of the given object.
	 *
	 * @param bean
	 *            the bean for which the bean component is requested
	 *
	 * @return the bean column definition or null if not present
	 */
	protected BeanComponentDefinition getBeanComponentDefinition(Object bean) {
		return beanComponentDefinitions.get(bean.getClass());
	}

	/**
	 * Method to get the bean definition of the given type.
	 *
	 * @param type
	 *            the type for which the bean component is requested
	 *
	 * @return the bean column definition or null if not present
	 */
	protected BeanComponentDefinition getBeanComponentDefinition(Class<?> type) {
		return beanComponentDefinitions.get(type);
	}

	/**
	 * Method to get a bean column from the given type of object.
	 *
	 * @param bean
	 *            the bean component to get a bean column definition from
	 * @param propertyId
	 *            the property id of the bean column definition
	 *
	 * @return the bean column definition, or null if the bean component or the
	 *         column is not registered
	 */
	protected BeanColumnDefinition getBeanColumnDefinition(Object bean, String propertyId) {
		if (bean == null || propertyId == null) {
			return null;
		}
		BeanComponentDefinition componentDefinition = beanComponentDefinitions.get(bean.getClass());
		if (componentDefinition == null) {
			return null;
		}
		return componentDefinition.getBeanColumnDefinition(propertyId);
	}

	/**
	 * Method to get a bean column from the given type.
	 *
	 * @param clazz
	 *            the type of the bean component to get a bean column definition
	 *            from
	 * @param propertyId
	 *            the property id of the bean column definition
	 *
	 * @return the bean column definition, or null if the bean component or the
	 *         column is not registered
	 */
	protected BeanColumnDefinition getBeanColumnDefinition(Class<?> clazz, String propertyId) {
		if (clazz == null || propertyId == null) {
			return null;
		}
		BeanComponentDefinition componentDefinition = beanComponentDefinitions.get(clazz);
		if (componentDefinition == null) {
			return null;
		}
		return componentDefinition.getBeanColumnDefinition(propertyId);
	}
}
