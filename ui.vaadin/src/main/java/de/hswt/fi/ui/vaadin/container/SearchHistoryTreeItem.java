package de.hswt.fi.ui.vaadin.container;

import java.util.LinkedHashSet;
import java.util.Set;

public class SearchHistoryTreeItem<T> {

	private String caption;

	private T content;

	private Set<SearchHistoryTreeItem<T>> children;

	public SearchHistoryTreeItem(String caption) {
		this(caption, null);
	}

	public SearchHistoryTreeItem(String caption, T content) {
		this.caption = caption;
		this.content = content;
		children = new LinkedHashSet<>();
	}

	public T getContent() {
		return this.content;
	}

	public String getCaption() {
		return this.caption;
	}

	public Set<SearchHistoryTreeItem<T>> getChildren() {
		return children;
	}

	public void addChild(SearchHistoryTreeItem<T> child) {
		children.add(child);
	}

	@Override
	public String toString() {
		return caption;
	}

}
