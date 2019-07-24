package de.hswt.fi.beans;

import de.hswt.fi.beans.annotations.BeanColumn;
import de.hswt.fi.beans.annotations.BeanColumns;
import de.hswt.fi.beans.annotations.BeanComponent;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * This class is used to find and read all classes which are annotated as
 * {@link de.hswt.fi.beans.annotations.BeanComponent} and with
 * {@link de.hswt.fi.beans.annotations.BeanColumn} annotated fields.
 *
 * The de.hswt.fi.beans.annotations are parsed an the
 * {@link de.hswt.fi.beans.BeanComponentRegistry} is created with all found bean
 * components, handled as {@link de.hswt.fi.beans.BeanComponentDefinition}.
 *
 * @see de.hswt.fi.beans.annotations.BeanColumn
 * @see de.hswt.fi.beans.annotations.BeanComponent
 *
 * @see de.hswt.fi.beans.BeanColumnDefinition
 * @see de.hswt.fi.beans.BeanComponentDefinition
 *
 * @author Marco Luthardt
 */
@Component
public class BeanRegsitryStartUpCreator implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(BeanRegsitryStartUpCreator.class);

	private static final String BASE_PACKAGE = "de.hswt.fi";

	private final BeanComponentRegistry registry;

	@Autowired
	public BeanRegsitryStartUpCreator(BeanComponentRegistry registry) {
		this.registry = registry;
	}

	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		event.getApplicationContext();
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
				false);

		scanner.addIncludeFilter(new AnnotationTypeFilter(BeanComponent.class));

		for (BeanDefinition bd : scanner.findCandidateComponents(BASE_PACKAGE)) {
			Map<String, BeanColumnDefinition> beanColumns = new LinkedHashMap<>();
			Class<?> clazz = createBeanColumnDefinitions(bd.getBeanClassName(), null, beanColumns);
			if (clazz != null) {
				registry.addBeanComponentDefinition(
						new BeanComponentDefinition(clazz, beanColumns));
			}
		}
	}

	/**
	 * Creates the bean column definition for a given class, which is annotated
	 * with {@link de.hswt.fi.beans.annotations.BeanComponent}.
	 *
	 * The call of this method will result in a recursive access of this method
	 * until no field of a class is annotated with
	 * {@link de.hswt.fi.beans.annotations.BeanComponent}.
	 *
	 * @param className
	 *            the fully qualified name of the annotated class
	 * @param parent
	 *            the parent of this bean component, if any
	 * @param beanColumns
	 *            a map with all already created bean component definitions for
	 *            the since the first call of this method
	 * @return the type of the current class
	 */
	private Class<?> createBeanColumnDefinitions(String className, BeanColumnDefinition parent, Map<String, BeanColumnDefinition> beanColumns) {

		Class<?> clazz = null;
		try {
			clazz = ClassUtils.forName(className, getClass().getClassLoader());
		} catch (ClassNotFoundException | LinkageError e1) {
			LOGGER.error("An error occurred", e1);
			return null;
		}

		List<Field> unsortedFields = FieldUtils.getFieldsListWithAnnotation(clazz,
				BeanColumn.class);
		unsortedFields.addAll(FieldUtils.getFieldsListWithAnnotation(clazz, BeanColumns.class));

		if (unsortedFields.isEmpty()) {
			return null;
		}

		List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
		fields.removeIf(f -> !unsortedFields.contains(f));

		Map<String, PropertyDescriptor> descriptors = new LinkedHashMap<>();
		Arrays.stream(PropertyUtils.getPropertyDescriptors(clazz))
				.forEach(d -> descriptors.put(d.getName(), d));

		for (Field field : fields) {
			if (!isValidBeanColumnProperty(field, descriptors.get(field.getName()))) {
				continue;
			}
			parseField(field, parent, beanColumns);
		}
		return clazz;
	}

	/**
	 * The method interpret the {@link de.hswt.fi.beans.annotations.BeanColumn}
	 * annotation. Calls the method to bean component creation, if the annotated
	 * field is a nested component.
	 *
	 * @param field
	 *            the field which is annotated
	 * @param parent
	 *            the parent column definition, if any
	 * @param beanColumns
	 *            a map with all bean column definitions which were created so
	 *            far for the first provided class
	 */
	private void parseField(Field field, BeanColumnDefinition parent, Map<String, BeanColumnDefinition> beanColumns) {
		BeanColumn[] beanColumnAnnotations = field.getAnnotationsByType(BeanColumn.class);

		for (BeanColumn beanColumnAnnotation : beanColumnAnnotations) {
			BeanColumnDefinition beanColumnDefinition = createBeanColumnDefinition(field, parent,
					beanColumnAnnotation, beanColumnAnnotation.ghostName());
			beanColumns.put(beanColumnDefinition.getPropertyId(), beanColumnDefinition);

			Class<?> c = createBeanColumnDefinitions(beanColumnDefinition.getType().getName(),
					beanColumnDefinition, beanColumns);

			beanColumnDefinition.setGroup(c != null);

			if (parent != null && !beanColumnDefinition.isSelector()
					&& !beanColumnDefinition.isGhost()) {
				parent.addChild(beanColumnDefinition);
			}
		}
	}

	/**
	 * Creates a bean column definition from a given field which is annotated
	 * with {@link de.hswt.fi.beans.annotations.BeanColumn}.
	 *
	 * @param field
	 *            the annotated field
	 * @param parent
	 *            the parent bean column definition, if any
	 * @param beanColumnAnnotation
	 *            the annotation
	 * @param ghostName
	 *            name of the column, if the column should be a ghost column
	 * @return the created bean column definition
	 */
	private BeanColumnDefinition createBeanColumnDefinition(Field field, BeanColumnDefinition parent, BeanColumn beanColumnAnnotation, String ghostName) {
		BeanColumnDefinition beanColumnDefinition = new BeanColumnDefinition();

		beanColumnDefinition.setSelector(beanColumnAnnotation.selector());

		if (!beanColumnAnnotation.caption().isEmpty()) {
			beanColumnDefinition.setCaption(beanColumnAnnotation.caption());
		} else if (!beanColumnAnnotation.i18nId().isEmpty()) {
			beanColumnDefinition.addI18nId(beanColumnAnnotation.i18nId());
		}

		if (!beanColumnAnnotation.i18nValuePropertyId().isEmpty()) {
			beanColumnDefinition.setI18nValuePropertyId(beanColumnAnnotation.i18nValuePropertyId());
		}

		if (!beanColumnAnnotation.format().isEmpty()) {
			beanColumnDefinition.setFormatDefinition(beanColumnAnnotation.format());
		}

		if (Collection.class.isAssignableFrom(field.getType())) {
			beanColumnDefinition.setCollection(true);
			beanColumnDefinition.setType(getCollectionParameterType(field.getGenericType()));
		} else {
			Class<?> type = field.getType();
			if (ClassUtils.isPrimitiveOrWrapper(type)) {
				type = ClassUtils.resolvePrimitiveIfNecessary(type);
			}
			beanColumnDefinition.setType(type);
		}

		String parentPropertyId = "";
		if (parent != null) {
			parentPropertyId = parent.getPropertyId() + ".";
			parent.getI18nIds().forEach(beanColumnDefinition::addI18nId);
		}

		if (ghostName.isEmpty()) {
			beanColumnDefinition.setPropertyId(parentPropertyId + field.getName());
		} else {
			beanColumnDefinition.setPropertyId(parentPropertyId + ghostName);
			beanColumnDefinition.setRevenantPropertyId(parentPropertyId + field.getName());
		}

		return beanColumnDefinition;
	}

	/**
	 * Helper method to get the parameter type of a collection
	 *
	 * @param listType
	 *            the type of the list
	 * @return the type of the collection parameter
	 */
	private Class<?> getCollectionParameterType(Type listType) {
		if (listType instanceof ParameterizedType) {
			Type elementType = ((ParameterizedType) listType).getActualTypeArguments()[0];
			try {
				return Class.forName(elementType.getTypeName());
			} catch (ClassNotFoundException e) {
				LOGGER.error("An error occurred", e);
			}
		}
		return null;
	}

	/**
	 * Checks if a given field is a valid one for being a bean column. The field
	 * must have a getter name which is suitable for the given field.
	 *
	 * @param field
	 *            the field to check
	 * @param propertyDescriptor
	 * @return true if the field is valid, otherwise false
	 */
	private boolean isValidBeanColumnProperty(Field field, PropertyDescriptor propertyDescriptor) {
		boolean isValid = true;
		String readerMethodName = "get" + field.getName() + "()";
		if(propertyDescriptor == null) {
			throw new IllegalArgumentException("Make sure Getter and Setter is available for " + field.getName());
		}
		if (propertyDescriptor.getReadMethod() == null) {
			LOGGER.warn("invalid bean column {}, property has no getter method", field.getName());
			isValid = false;
		} else if (readerMethodName.equals(propertyDescriptor.getReadMethod().getName())) {
			LOGGER.warn("invalid bean column {}, has no equal named getter method",
					field.getName());
			isValid = false;
		}
		return isValid;
	}
}
