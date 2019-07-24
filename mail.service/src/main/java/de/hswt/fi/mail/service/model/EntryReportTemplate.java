package de.hswt.fi.mail.service.model;

import de.hswt.fi.search.service.mass.search.model.Entry;

import java.io.Serializable;
import java.util.List;


public class EntryReportTemplate extends BasicMailTemplate implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Entry> entries;

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((entries == null) ? 0 : entries.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		EntryReportTemplate other = (EntryReportTemplate) obj;
		if (entries == null) {
			if (other.entries != null) {
				return false;
			}
		} else if (!entries.equals(other.entries)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "EntryReportTemplate [entries=" + entries + "]";
	}

}
