package de.hswt.fi.ui.vaadin.components;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import de.hswt.fi.ui.vaadin.CustomValoTheme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author August Gilg
 */
public class CustomList<COMPONENT extends CssLayout> extends CssLayout {

	private static final long serialVersionUID = 1L;

	private COMPONENT selectedItem;

	private COMPONENT currentItem;

	private CssLayout containerLayout;

	private Consumer<COMPONENT> selectionCallback;

	private Consumer<COMPONENT> singleClickCallback;

	private Consumer<COMPONENT> doubleClickCallback;

	private List<String> selectionStyleNames;

	private boolean selectAble;

	private ShortcutListener selectPrevItemOnUp;

	private ShortcutListener selectNextItemOnDown;

	private Registration registrationUp;

	private Registration registrationDown;

	private List<Registration> clickListenerRegistrations;


	public CustomList(String caption) {

		setWidth("100%");

		clickListenerRegistrations = new ArrayList<>();

		this.selectAble = true;

		Label labelCaption = new Label(caption);
		labelCaption.setSizeUndefined();
		addComponent(labelCaption);

		this.containerLayout = new CssLayout();
		this.containerLayout.setWidth("100%");
		this.containerLayout.setHeight("100%");
		this.containerLayout.addStyleName(CustomValoTheme.PADDING_HALF);
		this.containerLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_SCROLLBAR);
		addComponent(this.containerLayout);

		addStyleName(CustomValoTheme.HIDDEN_OVERFLOW);
		addStyleName(CustomValoTheme.BORDER_COLOR_ALT1);

		this.selectionStyleNames = new ArrayList<>();
		
		selectPrevItemOnUp = new ShortcutListener("SelectPrevItemOnUp", ShortcutAction.KeyCode.ARROW_UP, new int[0]) {
			
			private static final long serialVersionUID = -501404970990352673L;

			@Override
			public void handleAction(Object sender, Object target) {
				
				int index = containerLayout.getComponentIndex(getSelectedItem());
				
				if(index == 0) {
					index = containerLayout.getComponentCount() - 1;
				} else {
					index--;
				}

				setSelected(index);
			}
		};
		
		selectNextItemOnDown = new ShortcutListener("SelectNextItemOnDown", ShortcutAction.KeyCode.ARROW_DOWN, new int[0]) {
			
			private static final long serialVersionUID = 2247769184854230701L;

			@Override
			public void handleAction(Object sender, Object target) {
				
				int index = containerLayout.getComponentIndex(getSelectedItem());
				
				if(index == containerLayout.getComponentCount() - 1) {
					index = 0;
				} else {
					index++;
				}

				setSelected(index);
			}
		};
		
		addShortcutListener(selectPrevItemOnUp);
		addShortcutListener(selectNextItemOnDown);
	}

	private void toggleArrowNavigation(boolean enabled) {
		
		if(enabled) {
			registrationUp = addShortcutListener(selectPrevItemOnUp);
			registrationDown = addShortcutListener(selectNextItemOnDown);
		} else {
			if (registrationUp != null) {
				registrationUp.remove();
			}
			if (registrationDown != null) {
				registrationDown.remove();
			}
		}
	}

		
	public void destroy() {
		clear();
		this.singleClickCallback = null;
		this.doubleClickCallback = null;
	}

	public void addSelectionChangeListener(Consumer<COMPONENT> selectionCallback) {
		this.selectionCallback = selectionCallback;
	}
	public void addClickListener(Consumer<COMPONENT> singleClickCallback) {
		this.singleClickCallback = singleClickCallback;
	}

	public void addDoubleClickListener(Consumer<COMPONENT> doubleClickCallback) {
		this.doubleClickCallback = doubleClickCallback;
	}

	@SuppressWarnings("unchecked")
	public List<COMPONENT> getAllItems() {

		List<COMPONENT> components = new ArrayList<>();

		for (Component c : this.containerLayout) {
			components.add((COMPONENT) c);
		}

		return components;
	}

	public void addItem(COMPONENT item) {
		containerLayout.addComponent(item);
		currentItem = item;
		clickListenerRegistrations.add(item.addLayoutClickListener(itemClickListener));
	}

	public void removeItem(COMPONENT item) {
		containerLayout.removeComponent(item);
	}

	public COMPONENT getSelectedItem() {
		return selectedItem;
	}

	public void addSelectionStyleName(String style) {
		selectionStyleNames.add(style);
	}

	public void addSelectionStyleName(String... style) {
		selectionStyleNames.addAll(Arrays.asList(style));
	}

	public void removeAllSelectionStyles() {
		selectionStyleNames.clear();
	}

	public void setSelected(COMPONENT item) {

		if (!hasItem(item)) {
			return;
		}

		if (!selectionStyleNames.isEmpty()) {
			if (selectedItem != null) {
				selectionStyleNames.forEach(s -> selectedItem.removeStyleName(s));
			}
			selectionStyleNames.forEach(item::addStyleName);
		}

		selectedItem = item;
	}

	@SuppressWarnings("unchecked")
	public void setSelected(int index) {

		if (containerLayout.getComponentCount() == 0) {
			return;
		}

		Component item = containerLayout.getComponent(index);
		
		if(!item.getClass().isAssignableFrom(UploadListItem.class)) {
			return;
		}
		
		if (!selectionStyleNames.isEmpty()) {
			if (selectedItem != null) {
				selectionStyleNames.forEach(s -> selectedItem.removeStyleName(s));
			}
			selectionStyleNames.forEach(item::addStyleName);
		}

		selectedItem = (COMPONENT) item;
		
		UI.getCurrent().scrollIntoView(selectedItem);
		
		if(selectionCallback != null) {
			selectionCallback.accept(getSelectedItem());
		}
	}
	
	public void setSelectItemsOnClick(boolean selectAble) {
		this.selectAble = selectAble;
		toggleArrowNavigation(selectAble);
	}

	private boolean hasItem(COMPONENT item) {
		for (COMPONENT c : getAllItems()) {
			if (c.equals(item)) {
				return true;
			}
		}
		return false;
	}

	public COMPONENT getCurrentItem() {
		return currentItem;
	}

	public void clear() {
		selectedItem = null;
		clickListenerRegistrations.forEach(Registration::remove);
		containerLayout.removeAllComponents();
	}

	private LayoutClickListener itemClickListener = new LayoutClickListener() {

		private static final long serialVersionUID = 1L;

		@Override
		public void layoutClick(LayoutClickEvent event) {
			itemClick(event);
		}

		@SuppressWarnings("unchecked")
		private void itemClick(LayoutClickEvent event) {

			Class<COMPONENT> type = (Class<COMPONENT>) event.getSource().getClass();

			if (type.isInstance(event.getSource())) {

				COMPONENT item = type.cast(event.getSource());

				if (event.isDoubleClick() && doubleClickCallback != null) {
					doubleClickCallback.accept(item);
				}

				if (selectedItem != null && selectedItem.equals(item)) {
					return;
				}

				if (selectAble) {
					setSelected(item);
				}

				if (singleClickCallback != null) {
					singleClickCallback.accept(item);
				}
			}
		}
	};

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		CustomList<?> that = (CustomList<?>) o;
		return selectAble == that.selectAble &&
				Objects.equals(selectedItem, that.selectedItem) &&
				Objects.equals(containerLayout, that.containerLayout) &&
				Objects.equals(selectionCallback, that.selectionCallback) &&
				Objects.equals(singleClickCallback, that.singleClickCallback) &&
				Objects.equals(doubleClickCallback, that.doubleClickCallback) &&
				Objects.equals(selectionStyleNames, that.selectionStyleNames) &&
				Objects.equals(selectPrevItemOnUp, that.selectPrevItemOnUp) &&
				Objects.equals(selectNextItemOnDown, that.selectNextItemOnDown) &&
				Objects.equals(registrationUp, that.registrationUp) &&
				Objects.equals(registrationDown, that.registrationDown) &&
				Objects.equals(clickListenerRegistrations, that.clickListenerRegistrations) &&
				Objects.equals(itemClickListener, that.itemClickListener);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), selectedItem, containerLayout, selectionCallback, singleClickCallback, doubleClickCallback, selectionStyleNames, selectAble, selectPrevItemOnUp, selectNextItemOnDown, registrationUp, registrationDown, clickListenerRegistrations, itemClickListener);
	}
}