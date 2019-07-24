package de.hswt.fi.ui.vaadin.eventbus.payloads;

import com.vaadin.ui.Grid.Column;

import java.util.List;

public class DownloadRequestPayload<ENTRY> {

	private List<ENTRY> itemIds;

	private List<Column<ENTRY, ?>> columns;

	public DownloadRequestPayload(List<ENTRY> itemIds, List<Column<ENTRY, ?>> columns) {
		this.itemIds = itemIds;
		this.columns = columns;
	}

	public List<ENTRY> getItems() {
		return itemIds;
	}

	public void setItemIds(List<ENTRY> itemIds) {
		this.itemIds = itemIds;
	}

	public List<Column<ENTRY, ?>> getColumns() {
		return columns;
	}

	public void setColumns(List<Column<ENTRY, ?>> columns) {
		this.columns = columns;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ExcelContentPayload [itemIds=");
		builder.append(itemIds);
		builder.append(", columns=");
		builder.append(columns);
		builder.append("]");
		return builder.toString();
	}

}
