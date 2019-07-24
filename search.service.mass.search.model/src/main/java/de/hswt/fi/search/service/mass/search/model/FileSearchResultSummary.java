package de.hswt.fi.search.service.mass.search.model;

import de.hswt.fi.model.ResultSummary;

import java.util.HashMap;
import java.util.Map;

public class FileSearchResultSummary implements ResultSummary {

	private Map<String, Object> i18nCaptionValueMap;

	public FileSearchResultSummary() {
		i18nCaptionValueMap = new HashMap<>();
	}

	public void setFeaturesCount(int featuresCount) {
		i18nCaptionValueMap.put(I18nKeys.FILE_SEARCH_RESULT_SUMMARY_CAPTION_FEATURES, featuresCount);
	}

	public void setLocatedFeaturesCount(int locatedFeaturesCount) {
		i18nCaptionValueMap.put(I18nKeys.FILE_SEARCH_RESULT_SUMMARY_CAPTION_LOCATED_FEATURES, locatedFeaturesCount);
	}

	public void setCandidatesCount(int candidatesCount) {
		i18nCaptionValueMap.put(I18nKeys.FILE_SEARCH_RESULT_SUMMARY_CAPTION_CANDIDATES_FOUND, candidatesCount);
	}

	public void setUnlocatedFeaturesCount(int unlocatedFeaturesCount) {
		i18nCaptionValueMap.put(I18nKeys.FILE_SEARCH_RESULT_SUMMARY_CAPTION_UNLOCATED_FEATURES, unlocatedFeaturesCount);
	}

	@Override
	public Map<String, Object> getI18nCaptionValueMap() {
		return i18nCaptionValueMap;
	}

	@Override
	public String getTitleI18nKey() {
		return I18nKeys.SI_MODEL_RESULT_SUMMARY_TITLE;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FileSearchResultSummary that = (FileSearchResultSummary) o;

		return i18nCaptionValueMap != null ? i18nCaptionValueMap.equals(that.i18nCaptionValueMap) : that.i18nCaptionValueMap == null;
	}

	@Override
	public int hashCode() {
		return i18nCaptionValueMap != null ? i18nCaptionValueMap.hashCode() : 0;
	}

}
