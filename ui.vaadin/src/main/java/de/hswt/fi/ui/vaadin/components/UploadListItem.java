package de.hswt.fi.ui.vaadin.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import de.hswt.fi.ui.vaadin.CustomValoTheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author August Gilg
 */
public class UploadListItem<T> extends CssLayout {

	private static final long serialVersionUID = 1L;

	private T itemData;

	private Label caption;

	private Label counter;

	private List<Consumer<UploadListItem<T>>> deletionsListeners;
	
	public UploadListItem(T itemData, String itemCaption) {
		this(itemData, itemCaption, -1, "", false);
	}
	
	public UploadListItem(T itemData, String itemCaption, boolean deleteAble) {
		this(itemData, itemCaption, -1, "", deleteAble);
	}
	
	public UploadListItem(T itemData, String itemCaption, int componentCount,
			String entryCounterDescription, boolean deleteable) {
		
		setWidth("100%");
		addStyleName(CustomValoTheme.PADDING_HALF);
		
		this.itemData = itemData;
		deletionsListeners = new ArrayList<>();

		caption = new Label(itemCaption);
		caption.setHeightUndefined();
		caption.setWidth("80%");
		caption.addStyleName(CustomValoTheme.LABEL_OVERFLOW_DOTTED);
		caption.addStyleName(CustomValoTheme.NO_TEXT_SELECTION);
		caption.setDescription(itemCaption);
		addComponent(caption);

		if(componentCount != -1) {
			counter = new Label("(" + componentCount + ")");
			counter.setHeightUndefined();
			counter.setDescription(entryCounterDescription);
			counter.addStyleName(CustomValoTheme.FLOAT_RIGHT);
			counter.setWidth("15%");
			counter.addStyleName(CustomValoTheme.NO_TEXT_SELECTION);
			addComponent(counter);
		}

		if(deleteable) {
			Button deleteButton = new Button();
			deleteButton.setSizeUndefined();
			deleteButton.setIcon(VaadinIcons.BAN);
			deleteButton.addStyleName(CustomValoTheme.FLOAT_RIGHT);
			deleteButton.addStyleName(CustomValoTheme.BACKGROUND_COLOR_GRADIENT_ALT3);
			deleteButton.addStyleName(CustomValoTheme.BORDER_NONE);
			deleteButton.addClickListener(l -> {
				if(deletionsListeners != null) {
					deletionsListeners.forEach(d -> d.accept(this));
				}
			});
			addComponent(deleteButton);
		}
		
	}

	public T getItemData() {
		return itemData;
	}

	public void setItemData(T data) {
		this.itemData = data;
	}
	
	public void addDeletionsListener(Consumer<UploadListItem<T>> consumer) {
		if(deletionsListeners != null) {
			deletionsListeners.add(consumer);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		UploadListItem<?> that = (UploadListItem<?>) o;
		return Objects.equals(itemData, that.itemData) &&
				Objects.equals(caption, that.caption) &&
				Objects.equals(counter, that.counter);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), itemData, caption, counter);
	}
}
