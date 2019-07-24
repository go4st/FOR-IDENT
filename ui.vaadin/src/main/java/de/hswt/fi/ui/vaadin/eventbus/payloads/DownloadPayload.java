package de.hswt.fi.ui.vaadin.eventbus.payloads;

import java.util.List;

public class DownloadPayload<ENTRY> {

	private List<ENTRY> entries;

	private List<String> columnIds;

	public DownloadPayload(List<ENTRY> entries, List<String> columnIds) {
		super();
		this.entries = entries;
		this.columnIds = columnIds;
	}

	public List<ENTRY> getEntries() {
		return this.entries;
	}

	public void setEntries(List<ENTRY> entries) {
		this.entries = entries;
	}

	public List<String> getColumnIds() {
		return this.columnIds;
	}

	public void setColumnIds(List<String> columnIds) {
		this.columnIds = columnIds;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResultPayload [entries=");
		builder.append(this.entries);
		builder.append(", columnIds=");
		builder.append(this.columnIds);
		builder.append("]");
		return builder.toString();
	}

}
