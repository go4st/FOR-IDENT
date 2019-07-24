package de.hswt.fi.beans.annotations;

import java.lang.annotation.*;

/**
 * This annotation is used to tag classes as bean component. A bean component is
 * a bean class with fields, getters and setters. It is used to declare classes
 * as member of a list of classes, which could easily accessed by it's
 * properties (access fields via name of fields). Therefore a mapper is provided
 * {@link de.hswt.fi.beans.BeanComponentMapper}, which can access easily by
 * providing a string with the field names (properties), concatenated by points.
 *
 * eg: myComponent.nestedBeanComponentAsProperty.nestedProperty
 *
 * Each bean component should have bean columns {@link BeanColumn}. Bean columns
 * are fields within the component. Without any bean columns, a bean component
 * is much useless.
 *
 * BeanComponents and {@link BeanColumn} are used to get an easy way to
 * represent object hierarchies in a table style, e.g. an excel sheet.
 *
 * @see de.hswt.fi.beans.annotations.BeanColumn
 *
 * @author Marco Luthardt
 *
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BeanComponent {

}
