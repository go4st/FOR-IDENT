package de.hswt.fi.beans.annotations;

import java.lang.annotation.*;

/**
 * Multi wrapper for {@link de.hswt.fi.beans.annotations.BeanColumn}.
 *
 * @see de.hswt.fi.beans.annotations.BeanColumn
 *
 * @author Marco Luthardt
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BeanColumns {
	BeanColumn[] value();
}
