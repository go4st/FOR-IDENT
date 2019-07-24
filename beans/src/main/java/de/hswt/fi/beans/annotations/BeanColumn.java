package de.hswt.fi.beans.annotations;

import java.lang.annotation.*;

/**
 * This annotation is used to define a field in a class, which is annotated as
 * {@link de.hswt.fi.beans.annotations.BeanComponent}, as a column in a table
 * representation.
 *
 *
 * @author Marco Luthardt
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(value = BeanColumns.class)
public @interface BeanColumn {

	/**
	 * Defines a format as string, which can be read and used by a given
	 * interpreter when a conversion to String is desired.
	 *
	 * @return a format as String e.g. %.2f for a double value
	 */
	String format() default "";

	/**
	 * An i18n key, which can be used to define a localized header for the
	 * column of this value.
	 *
	 * @return an i18n key
	 */
	String i18nId() default "";

	/**
	 * A caption for the column if the desired field which must not be
	 * localized. If a caption is present, the i18n value will be ignored.
	 *
	 * @return a caption for the column in the table representation.
	 */
	String caption() default "";

	/**
	 * The string represents the name of a getter method, which must then be
	 * present in the type of the field, which is annotated. The getter returns
	 * must return an i18n key corresponding to the value of the field.
	 *
	 * Example: getI18nKey() =&gt; name of the getter: i18nKey
	 *
	 * @return name of a getter method which gives a i18n key
	 */
	String i18nValuePropertyId() default "";

	/**
	 * The selector defines, that this property or field should be used to
	 * select a group of columns, which is created out of the corresponding
	 * nested bean columns, but can also be this field as single value, e.g. a
	 * double.
	 *
	 * @return if the field is a selector or not
	 */
	boolean selector() default false;

	/**
	 * Each field can be represented as multiple columns if desired, getting the
	 * same value for different columns. If a name for the ghost column is set,
	 * an extra column will be created, which can have the same behavior as any
	 * non ghost column.
	 *
	 * One and only one column must be a non ghost column at the annoted field.
	 *
	 * @return the name of the ghost column
	 */
	String ghostName() default "";
}
