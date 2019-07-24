package de.hswt.fi.search.service.mass.search.model;

import java.util.Date;

public interface Property<T> extends Comparable<T> {

	String getSource();

	void setSource(String source);

	String getEditor();

	void setEditor(String editor);

	String getAdditional();

	void setAdditional(String additional);

	Date getLastModified();

	void setLastModified(Date lastModified);
}
