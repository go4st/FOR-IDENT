package de.hswt.fi.ui.vaadin;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Optional;

public final class HtmlDescriptionUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(HtmlDescriptionUtil.class);

	private static final String LOCATION_BASENAME = "/public/html/tooltips/";

	private static final String FILE_EXTENSION = ".html";

	private static final String DEFAULT_LOCALE = "de";

	private HtmlDescriptionUtil() {
		// prevent instantiation
	}

	public static String getDescription(String fileBaseName, Locale locale) {

		Optional<InputStream> localeTemplate = getLocaleTemplate(fileBaseName, locale);
		if (localeTemplate.isPresent()) {

			try {
				return IOUtils.toString(localeTemplate.get());
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}

		} else {
			try {
				return IOUtils.toString(getDefaultTemplate(fileBaseName));
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		return "";
	}

	private static InputStream getDefaultTemplate(String fileBaseName) {
		return HtmlDescriptionUtil.class.getResourceAsStream(
				LOCATION_BASENAME + fileBaseName + DEFAULT_LOCALE + FILE_EXTENSION);
	}

	private static Optional<InputStream> getLocaleTemplate(String fileBaseName, Locale locale) {
		InputStream inputStream = HtmlDescriptionUtil.class.getResourceAsStream(
				LOCATION_BASENAME + fileBaseName + locale.toLanguageTag() + FILE_EXTENSION);

		return Optional.ofNullable(inputStream);
	}

}
