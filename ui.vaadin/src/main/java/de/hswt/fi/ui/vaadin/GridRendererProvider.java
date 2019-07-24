package de.hswt.fi.ui.vaadin;

import com.vaadin.ui.renderers.NumberRenderer;
import org.springframework.context.i18n.LocaleContextHolder;

import java.security.InvalidParameterException;

public final class GridRendererProvider {

	private GridRendererProvider() {
		// Prevent instantiation
	}

	public static  NumberRenderer getLocalizedRenderer(int digits) {

		if (digits == 0 || digits > 5) {
			throw new InvalidParameterException("Digits must between 1 and 4");
		}
		return new NumberRenderer("%." + digits + "f", LocaleContextHolder.getLocale());
	}
}
