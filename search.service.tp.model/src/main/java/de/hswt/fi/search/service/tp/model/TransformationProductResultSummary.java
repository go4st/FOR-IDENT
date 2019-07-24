package de.hswt.fi.search.service.tp.model;

import de.hswt.fi.model.ResultSummary;

import java.util.HashMap;
import java.util.Map;

public class TransformationProductResultSummary implements ResultSummary {

	private Map<String, Object> i18nCaptionValueMap;

	public TransformationProductResultSummary(int knownCount, int knownPathwayCandidatesCount,
											  int unknownCount, int unknownPathwayCandidatesCount) {

		i18nCaptionValueMap = new HashMap<>();
		i18nCaptionValueMap.put(I18nKeys.TP_MODEL_RESULT_CAPTION_TITLE_KNOWN, knownCount);
		i18nCaptionValueMap.put(I18nKeys.TP_MODEL_RESULT_CAPTION_TITLE_KNOWN_PATHWAY_CANDIDATES, knownPathwayCandidatesCount);
		i18nCaptionValueMap.put(I18nKeys.TP_MODEL_RESULT_CAPTION_TITLE_UNKNOWN, unknownCount);
		i18nCaptionValueMap.put(I18nKeys.TP_MODEL_RESULT_CAPTION_TITLE_UNKNOWN_PATHWAY_CANDIDATES, unknownPathwayCandidatesCount);
	}

	@Override
	public Map<String, Object> getI18nCaptionValueMap() {
		return i18nCaptionValueMap;
	}

	@Override
	public String getTitleI18nKey() {
		return I18nKeys.TP_MODEL_RESULT_SUMMARY_TITLE;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TransformationProductResultSummary that = (TransformationProductResultSummary) o;

		return i18nCaptionValueMap != null ? i18nCaptionValueMap.equals(that.i18nCaptionValueMap) : that.i18nCaptionValueMap == null;
	}

	@Override
	public int hashCode() {
		return i18nCaptionValueMap != null ? i18nCaptionValueMap.hashCode() : 0;
	}
}
