package de.hswt.fi.ui.vaadin.container;

import de.hswt.fi.search.service.mass.search.model.Entry;

public class SearchHistoryTreeRootItem<RESULTCONTAINER extends ResultContainer> extends SearchHistoryTreeItem<RESULTCONTAINER> {

	private SearchHistoryTreeItem parameterParentItem;

	private SearchHistoryTreeItem selectedResultParentItem;

	public SearchHistoryTreeRootItem(String caption, RESULTCONTAINER resultcontainer, String parameterParentCaption, String selectedResultParentCaption) {
		super(caption, resultcontainer);

		parameterParentItem = new SearchHistoryTreeItem(parameterParentCaption);
		selectedResultParentItem = new SearchHistoryTreeItem(selectedResultParentCaption);

		addChild(parameterParentItem);
		addChild(selectedResultParentItem);
	}

	public void addParameter(String parameterStringRepresentation) {
		parameterParentItem.addChild(new SearchHistoryTreeItem(parameterStringRepresentation));
	}

	public void addSelectedResult(String entryStringRepresenation, Entry entry) {
		selectedResultParentItem.addChild(new SearchHistoryTreeItem(entryStringRepresenation, entry));
	}

	public SearchHistoryTreeItem getParameterParentItem() {
		return parameterParentItem;
	}

	public SearchHistoryTreeItem getSelectedResultParentItem() {
		return selectedResultParentItem;
	}
}
