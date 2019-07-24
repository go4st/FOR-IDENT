package de.hswt.fi.model;

import java.util.Map;

/**
 * Interface for result summary classes which contains result summaries.
 *
 * The summary classes must use the fi bean framework for their properties.
 *
 * @author Marco Luthardt
 */
public interface ResultSummary {

	String getTitleI18nKey();

	Map<String, Object> getI18nCaptionValueMap();
}
