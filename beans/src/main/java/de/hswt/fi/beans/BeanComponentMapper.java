package de.hswt.fi.beans;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This is a helper class for handling and accessing
 * {@link de.hswt.fi.beans.BeanColumnDefinition bean columns}.
 *
 * @see de.hswt.fi.beans.BeanComponentDefinition
 * @see de.hswt.fi.beans.BeanColumnDefinition
 *
 * @author Marco Luthardt
 */
public class BeanComponentMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(BeanComponentMapper.class);

	private static final String POSTFIX_CAPTION = "Caption";

	private static final String PREFIX_SETTER = "set";

	private final BeanComponentRegistry registry;

	/**
	 * Function to access the i18n mapping.
	 */
	private Function<String, String> i18n;

	/**
	 * Function to get the current local.
	 */
	private Supplier<Locale> locale;

	/**
	 *
	 * @param i18n
	 *            function to access the i18n mapping (providing key, get
	 *            localized value)
	 * @param locale
	 *            function to get the current locale
	 */
	@Autowired
	public BeanComponentMapper(Function<String, String> i18n, Supplier<Locale> locale, BeanComponentRegistry registry) {
		this.i18n = i18n;
		this.locale = locale;
		this.registry = registry;
	}

	/**
	 * Method to get all bean column definitions for the type of the given bean
	 * object and contained on the list of property ids. The nested bean column
	 * definitions are included if they are in the list.
	 *
	 * @param bean
	 *            the bean to get type from
	 * @param propertyIds
	 *            the property id's which are requested
	 *
	 * @return the map of the requested bean column definitions with the
	 *         property id as key, will always return a map, can be empty when
	 *         no bean column definitions can be found
	 */
	public Map<String, BeanColumnDefinition> getBeanColumnDefinitions(Object bean, List<String> propertyIds) {
		Map<String, BeanColumnDefinition> definitions = new LinkedHashMap<>();
		for (String propertyId : propertyIds) {
			BeanColumnDefinition definition = registry.getBeanColumnDefinition(bean, propertyId);
			if (definition != null) {
				definitions.put(propertyId, definition);
			}
		}
		return definitions;
	}

	/**
	 * Method to get all property id's of bean column definitions, which are
	 * tagged as selectors for the type of the given bean.
	 *
	 * @param bean
	 *            the bean to get the property id's for
	 * @return all selector property id's contained by the type of the bean,
	 *         will always return a list, can be empty when no columns can be
	 *         found
	 */
	public List<String> getSelectorColumns(Object bean) {
		LinkedList<String> columnIds = new LinkedList<>();
		BeanComponentDefinition definition = registry.getBeanComponentDefinition(bean);
		if (definition == null) {
			return columnIds;
		}

		for (BeanColumnDefinition columnDefinition : definition.getBeanColumnDefinitions()
				.values()) {
			if (columnDefinition.isSelector()) {
				columnIds.add(columnDefinition.getPropertyId());
			}
		}
		return columnIds;
	}

	/**
	 * Method to get all captions of property id's which belongs to the given
	 * bean column definitions. This will also include all captions of all
	 * nested bean columns.
	 *
	 * Columns and nested columns, which property id's end match the post fix
	 * filter strings, are not taken into account and will not be included in
	 * the captions map.
	 *
	 * @param columns
	 *            the bean column definitions which the captions are requested
	 * @param postfixFilter
	 *            collection of post filter strings applied to the property id's
	 *            end
	 * @return a map the captions of all bean columns including the nested ones
	 *         with the property id as key, which property id's not ending with
	 *         strings specified in as postfix filter, will always return a map,
	 *         can be empty when no captions can be found
	 *
	 */
	public Map<String, String> getNestedPropertyCaptions(Collection<BeanColumnDefinition> columns, Collection<String> postfixFilter) {
		List<BeanColumnDefinition> nestedColumns = new ArrayList<>();
		for (BeanColumnDefinition column : columns) {
			nestedColumns.addAll(getAllNestedPropertyIds(column, postfixFilter));
		}

		Map<String, String> captions = new LinkedHashMap<>();
		nestedColumns.forEach(c -> captions.put(c.getPropertyId(), getNestedCaption(c)));

		
		return captions;
	}

	/**
	 * Get all captions for all property id's of the type of the bean.
	 *
	 * @param bean
	 *            the bean to get captions from it's type
	 * @param properties
	 *            the property id's which are requested captions for
	 * @return map with the captions of the requested property id's and type
	 *         with the property id as key, will always return a map, can be
	 *         empty when no captions can be found
	 */
	public Map<String, String> getPropertyCaptions(Object bean, List<String> properties) {

		List<BeanColumnDefinition> definitions = getColumnDefinitions(bean, properties);
		LinkedHashMap<String, String> captions = new LinkedHashMap<>();

		definitions.forEach(d -> captions.put(d.getPropertyId(), getNestedCaption(d)));

		return captions;
	}

	/**
	 * Get the value of a property as string.
	 *
	 * @param bean
	 *            the bean to get the type from
	 * @param propertyId
	 *            the property id of the requested value
	 *
	 * @return the string representation of the requested value, will always
	 *         return a string, empty if no value can be found
	 */
	public String getPropertyAsString(Object bean, String propertyId) {
		BeanColumnDefinition columnDefinition = getColumnDefinition(bean, propertyId);

		if (columnDefinition == null) {
			return "";
		}

		return getPropertyAsString(bean, propertyId, columnDefinition);
	}

	/**
	 * Get the value of a property as string.
	 *
	 * @param bean
	 *            the bean to get the type from
	 * @param propertyId
	 *            the property id of the requested value
	 * @param columnDefinition
	 *            the bean column definition of the requested value
	 *
	 * @return the string representation of the requested value, will always
	 *         return a string, empty if no value can be found
	 */
	public String getPropertyAsString(Object bean, String propertyId, BeanColumnDefinition columnDefinition) {

		Object value = getValue(bean, propertyId, columnDefinition);

		if (value == null) {
			return "";
		} else 
		if (String.class.isAssignableFrom(columnDefinition.getType())) {
			return (String) value;
		} else if (Double.class.isAssignableFrom(columnDefinition.getType())) {
			return String.format(getLocale(), columnDefinition.getFormatDefinition(), value);
		} else if (Date.class.isAssignableFrom(columnDefinition.getType())) {
			return DateFormat.getDateInstance(DateFormat.DEFAULT, locale.get())
					.format((Date) value);
		} else if (!columnDefinition.getI18nValuePropertyId().isEmpty()) {
			return getI18nValue(value, columnDefinition);
		} else {
			return value.toString();
		}
	}

	/**
	 * Method to get the value as object of a specific column.
	 *
	 * @param bean
	 *            the bean to get the type from
	 * @param propertyId
	 *            the property id of the requested column
	 * @param columnDefinition
	 *            the bean column definition of the requested column
	 *
	 * @return the value of the column or null, if no column can be found
	 */
	public Object getValue(Object bean, String propertyId, BeanColumnDefinition columnDefinition) {
		Object value = null;

		if (columnDefinition == null) {
			return null;
		}

		try {
			String realPropertyId = columnDefinition.isGhost()
					? columnDefinition.getRevenantPropertyId() : propertyId;
			if (PropertyUtils.isReadable(bean, realPropertyId)) {
				value = PropertyUtils.getNestedProperty(bean, realPropertyId);
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			LOGGER.error("An error occurred: ", e);
		} catch (NestedNullException e) {
			// Enabling this log causes massive server output when exporting results, because many results
			// have no values (are null)
		}
		return value;
	}

	/**
	 * Method to provide a mechanism of bean mapping to a given class. The given
	 * class must follow the bean conventions. If a field of type string with
	 * it's getter and setter matches a nested property id of the given property
	 * id, the setter will be used to set the corresponding value.
	 *
	 * If a field width same name an "Caption" as post fix is present at the
	 * requested type, the caption of the nested property id will also be set.
	 *
	 * The created and filled instance will be returned. It contains then all
	 * matching values as string values and captions (when caption field is
	 * present).
	 *
	 * @param clazz
	 *            the type of the requested mapping object
	 * @param bean
	 *            the bean to get the type from for the bean mapping
	 * @param propertyId
	 *            the property id of the bean column to look for nested columns
	 *
	 * @return a list of instances of clazz with filled values in string
	 *         representation and captions
	 */
	public <T> List<T> getValue(Class<T> clazz, Object bean, String propertyId) {
		return getValue(clazz, bean, propertyId, null);
	}

	/**
	 * Method to provide a mechanism of bean mapping to a given class. The given
	 * class must follow the bean conventions. If a field of type string with
	 * it's getter and setter matches a nested property id of the given property
	 * id, the setter will be used to set the corresponding value.
	 *
	 * If a field width same name an "Caption" as post fix is present at the
	 * requested type, the caption of the nested property id will also be set.
	 *
	 * The created and filled instance will be returned. It contains then all
	 * matching values as string values and captions (when caption field is
	 * present).
	 *
	 * A custom mapping can be provided. With this it is possible, to specify a
	 * mapping from a certain nested property id to field, not equally named as
	 * the nested property id.
	 *
	 * @param clazz
	 *            the type of the requested mapping object
	 * @param bean
	 *            the bean to get the type from for the bean mapping
	 * @param propertyId
	 *            the property id of the bean column to look for nested columns
	 * @param customMapping
	 *            a custom mapping, to specify mapping from a nested property id
	 *            to field name, where the property is the key, and the custom
	 *            field name the value
	 *
	 * @return a list of instances of clazz with filled values in string
	 *         representation and captions
	 */
	public <T> List<T> getValue(Class<T> clazz, Object bean, String propertyId, Map<String, String> customMapping) {
		List<T> values = new ArrayList<>();

		BeanColumnDefinition columnDefinition = registry.getBeanColumnDefinition(bean, propertyId);

		if (columnDefinition == null) {
			return values;
		}

		Map<String, String> mapping;
		if (customMapping == null) {
			List<String> setterNames = getSetterNames(clazz);
			mapping = new HashMap<>();
			setterNames.forEach(n -> mapping.put(n, n));
		} else {
			mapping = customMapping;
		}

		if (columnDefinition.isCollection()) {
			BeanWrapper wrapper = new BeanWrapperImpl(bean);
			Collection<?> collection = (Collection<?>) wrapper.getPropertyValue(propertyId);
			for (Object collectionBean : collection) {
				T value = getMappedValue(clazz, collectionBean, null, mapping);
				if (value != null) {
					values.add(value);
				}
			}
		} else {
			T value = getMappedValue(clazz, bean, propertyId, mapping);
			if (value != null) {
				values.add(value);
			}
		}

		return values;
	}

	/**
	 * Get the currently used locale.
	 *
	 * @return the current locale
	 */
	public Locale getLocale() {
		return locale.get();
	}

	/**
	 * Method to get a localized value of a given column as string.
	 *
	 * @param value
	 *            the non localized value
	 * @param columnDefinition
	 *            the bean column definition
	 * @return the localized value as string, or the normal string
	 *         representation of no localization is possible.
	 */
	private String getI18nValue(Object value, BeanColumnDefinition columnDefinition) {
		String i18nKey = null;
		try {
			i18nKey = (String) PropertyUtils.getProperty(value,
					columnDefinition.getI18nValuePropertyId());
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			LOGGER.error("An error occured", e);
		}
		if (i18nKey != null) {
			return getMessage(i18nKey);
		}
		return value.toString();
	}

	/**
	 * Method to get all nested property id's of a given bean column. Property
	 * id's can be excluded with the post fix filter strings, applied to the end
	 * of the property id's.
	 *
	 * @param column
	 *            the bean column definition to get the nested property id's
	 *            from
	 * @param postfixFilter
	 *            the list of post fix filter stings, applied to the end of the
	 *            nested property id's
	 * @return all nested property id's of the given column, always returns a
	 *         list, can be empty when no nested property id's were found or
	 *         were filtered out
	 */
	private List<BeanColumnDefinition> getAllNestedPropertyIds(BeanColumnDefinition column, Collection<String> postfixFilter) {
		List<BeanColumnDefinition> columns = new ArrayList<>();
		if (!column.getChildren().isEmpty()) {
			column.getChildren()
					.forEach(c -> columns.addAll(getAllNestedPropertyIds(c, postfixFilter)));
		} else if (postfixFilter.stream().noneMatch(f -> column.getPropertyId().endsWith(f))) {
			columns.add(column);
		}

		return columns;
	}

	/**
	 * Get a single bean column definition, specified through it's property id.
	 *
	 * @param bean
	 *            the bean to get the type from
	 * @param propertyId
	 *            the property id of the requested bean column definition
	 * @return the requested bean column definition or null, when no definition
	 *         can be found
	 */
	private BeanColumnDefinition getColumnDefinition(Object bean, String propertyId) {
		BeanComponentDefinition componentDefinition = registry.getBeanComponentDefinition(bean);
		if (componentDefinition == null) {
			return null;
		}
		return componentDefinition.getBeanColumnDefinition(propertyId);
	}

	/**
	 * Get a list of bean column definitions, specified through their property
	 * id's.
	 *
	 * @param bean
	 *            the bean to get the type from
	 * @return the requested bean column definitions, return always a list, can
	 *         be empty when no bean column definitions can be found
	 */
	private List<BeanColumnDefinition> getColumnDefinitions(Object bean, List<String> properties) {
		LinkedList<BeanColumnDefinition> definitions = new LinkedList<>();
		BeanComponentDefinition componentDefinition = registry.getBeanComponentDefinition(bean);
		if (componentDefinition == null) {
			return definitions;
		}
		componentDefinition.getBeanColumnDefinitions().keySet().stream()
				.filter(properties::contains)
				.forEach(k -> definitions.add(componentDefinition.getBeanColumnDefinition(k)));

		return definitions;
	}

	/**
	 * Returns a caption of a bean column, constructed of it's one localized
	 * caption and also the parent's localized captions. The captions are in
	 * order of the hierarchy, highest parent as first and self caption at the
	 * end.
	 *
	 * @param column
	 *            the column to get the localized caption from
	 * @return the constructed localized caption, returns never null, but can be
	 *         emtpy when no caption was specified.
	 */
	private String getNestedCaption(BeanColumnDefinition column) {
		StringBuilder captionBuilder = new StringBuilder();
		for (String i18nId : column.getI18nIds()) {
			if (!i18nId.isEmpty()) {
				captionBuilder.append(getMessage(i18nId)).append(" ");
			}
		}
		String caption = captionBuilder.toString();
		if (!column.getCaption().isEmpty()) {
			caption += column.getCaption();
		}
		return caption;
	}

	/**
	 * Get the single caption of a bean column, specified through it's property
	 * id. The parents of the bean column are not taken into account.
	 *
	 * @param bean
	 *            the bean to get the type from
	 * @param propertyId
	 *            the property if of the bean column definition
	 *
	 * @return the caption of the bean column, never null, but can be empty when
	 *         bean column definition is not present or did not contain a
	 *         caption or i18n key
	 */
	private String getCaption(Object bean, String propertyId) {
		BeanColumnDefinition columnDefinition = registry.getBeanColumnDefinition(bean, propertyId);
		if (columnDefinition != null) {
			if (!columnDefinition.getCaption().isEmpty()) {
				return columnDefinition.getCaption();
			} else if (!columnDefinition.getI18nId().isEmpty()) {
				return getMessage(columnDefinition.getI18nId());
			} else {
				return "";
			}
		}
		return "";
	}

	/**
	 * Helper method to get all relevant setter methods of a given class.
	 *
	 * Relevant means, it must be a string setter and must not end with a
	 * "Caption".
	 *
	 * @param clazz
	 *            the class to get the setter from
	 * @return a list with all names of setter methods, "set" is removed, never
	 *         null, but can be emtpy
	 */
	private List<String> getSetterNames(Class<?> clazz) {
		List<String> setterNames = new ArrayList<>();
		for (Method method : clazz.getDeclaredMethods()) {
			if (!isStringSetter(method) || method.getName().endsWith(POSTFIX_CAPTION)) {
				continue;
			}
			setterNames.add(getSetterName(method));
		}
		return setterNames;
	}

	/**
	 * Method to check if a method is a setter for a single string value.
	 *
	 * @param method
	 *            the method to check
	 *
	 * @return true if the method is a setter for a single string value,
	 *         otherwise false
	 */
	private boolean isStringSetter(Method method) {
		return method.getName().startsWith(PREFIX_SETTER) && method.getParameterTypes().length == 1
				&& String.class.isAssignableFrom(method.getParameterTypes()[0]);
	}

	/**
	 * Method to get the "cleaned" name of a setter method. The "set" prefix
	 * will be removed and the first letter is written in lower case.
	 *
	 * @param method
	 *            the method to get the name from
	 * @return the cleaned name of the setter method
	 */
	private String getSetterName(Method method) {
		String name = method.getName().replaceFirst(PREFIX_SETTER, "");
		String firstLetter = name.substring(0, 1);
		name = name.replaceFirst(firstLetter, firstLetter.toLowerCase());
		return name;
	}

	/**
	 *
	 * @param clazz class to get the mapped Value from
	 * @param bean specific bean object to get the value from
	 * @param propertyId the property ID of the value member
	 * @param mapping the mapping of the property
	 * @return mapped property value
	 */
	private <T> T getMappedValue(Class<T> clazz, Object bean, String propertyId, Map<String, String> mapping) {
		T value = null;
		try {
			value = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.error(e.getMessage(), e);
		}

		if (value == null) {
			return null;
		}

		BeanWrapper wrapper = new BeanWrapperImpl(value);

		for (String mappingProperty : mapping.keySet()) {
			String id = propertyId != null ? propertyId + "." + mappingProperty : mappingProperty;
			String propertyValue = getPropertyAsString(bean, id);
			if (propertyValue.isEmpty()) {
				continue;
			}

			String mappingValue = mapping.get(mappingProperty);

			wrapper.setPropertyValue(mappingValue, propertyValue);
			if (wrapper.isWritableProperty(mappingValue + POSTFIX_CAPTION)) {
				wrapper.setPropertyValue(mappingValue + POSTFIX_CAPTION,
						getCaption(bean, id));
			}
		}

		return value;
	}

	/**
	 * Returns a localized string specified through the given key.
	 *
	 * @param key
	 *            the key of the localized message for the i18n mechanism
	 *
	 * @return the localized message
	 */
	private String getMessage(String key) {
		return i18n.apply(key);
	}
}
