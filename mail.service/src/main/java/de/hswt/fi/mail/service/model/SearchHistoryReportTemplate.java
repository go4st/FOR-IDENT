package de.hswt.fi.mail.service.model;

import java.io.Serializable;

public class SearchHistoryReportTemplate<T> extends BasicMailTemplate implements Serializable {

	private static final long serialVersionUID = 1L;

	private T searchParameter;

	public T getSearchParameter() {
		return searchParameter;
	}

	public void setSearchParameter(T searchParameter) {
		this.searchParameter = searchParameter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((searchParameter == null) ? 0 : searchParameter.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		SearchHistoryReportTemplate other = (SearchHistoryReportTemplate) obj;
		if (searchParameter == null) {
			if (other.searchParameter != null) {
				return false;
			}
		} else if (!searchParameter.equals(other.searchParameter)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "SearchHistoryReportTemplate [searchParameter=" + searchParameter + "]";
	}
	
	

}
