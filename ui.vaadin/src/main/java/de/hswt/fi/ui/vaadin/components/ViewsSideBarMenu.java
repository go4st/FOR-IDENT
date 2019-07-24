package de.hswt.fi.ui.vaadin.components;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import de.hswt.fi.ui.vaadin.views.Sections;
import org.vaadin.spring.sidebar.SideBarItemDescriptor;
import org.vaadin.spring.sidebar.SideBarSectionDescriptor;
import org.vaadin.spring.sidebar.SideBarUtils;
import org.vaadin.spring.sidebar.components.AbstractSideBar.ItemFilter;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ViewsSideBarMenu extends CssLayout {

	private static final long serialVersionUID = -7573548309005558127L;

	private CssLayout menuItemsLayout;

	private CssLayout menuItemsCollapsedLayout;

	private CssLayout menu;

	private ItemFilter itemFilter;

	private SideBarUtils sideBarUtils;

	ViewsSideBarMenu(SideBarUtils sideBarUtils, ItemFilter itemFilter) {
		this.sideBarUtils = sideBarUtils;
		this.itemFilter = itemFilter;

		setPrimaryStyleName("valo-menu");

		menuItemsLayout = new CssLayout();
		menuItemsLayout.setPrimaryStyleName("valo-menuitems");

		menuItemsCollapsedLayout = new CssLayout();
		menuItemsCollapsedLayout.setPrimaryStyleName("valo-menuitems");
		menuItemsCollapsedLayout.setVisible(false);

		menu = new CssLayout();
		menu.setWidth("100%");
		menu.addComponent(menuItemsLayout);
		menu.addComponent(menuItemsCollapsedLayout);
		menu.addStyleName("valo-menu-part");
		addComponent(menu);
	}

	public void setItemFilter(ItemFilter itemFilter) {
		if (isAttached()) {
			throw new IllegalStateException(
					"An ItemFilter cannot be set when the SideBar is attached");
		}
		this.itemFilter = itemFilter;
	}

	@Override
	public void attach() {
		super.attach();
		for (SideBarSectionDescriptor section : sideBarUtils
				.getSideBarSections(getUI().getClass())) {

			Set<SideBarItemDescriptor> passedItems = sideBarUtils.getSideBarItems(section).stream()
					.filter(i -> itemFilter.passesFilter(i))
					.sorted(Comparator.comparingInt(SideBarItemDescriptor::getOrder))
					.collect(Collectors.toCollection(TreeSet::new));

			if (!passedItems.isEmpty()) {

				if (!Sections.TOP.equals(section.getId())) {
					menuItemsLayout.addComponent(
							buildSectionLabel(section.getCaption(), passedItems.size()));
					menuItemsCollapsedLayout.addComponent(
							buildSectionLabel(section.getCaption(), passedItems.size(), true));
				}

				for (SideBarItemDescriptor item : passedItems) {
					if (itemFilter.passesFilter(item)) {
						menuItemsLayout.addComponent(buildItemButton(item));
						menuItemsCollapsedLayout.addComponent(buildItemButton(item, true));
					}

				}
			}

		}
	}

	@Override
	public void detach() {
		removeAllComponents();
		super.detach();
	}

	private Label buildSectionLabel(String caption, int itemCount) {
		return buildSectionLabel(caption, itemCount, false);
	}

	private Label buildSectionLabel(String caption, int itemCount, boolean small) {
		Label label = new Label(caption, ContentMode.HTML);
		label.setPrimaryStyleName("valo-menu-subtitle");
		label.addStyleName("h4");
		label.setSizeUndefined();
		if (small) {
			label.setValue("");
		} else {
			label.setValue(
					label.getValue() + " <span class=\"valo-menu-badge\">" + itemCount + "</span>");
		}
		return label;
	}

	private Button buildItemButton(SideBarItemDescriptor descriptor) {
		return buildItemButton(descriptor, false);
	}

	private Button buildItemButton(SideBarItemDescriptor descriptor, boolean smallButton) {
		final Button button = new Button(descriptor.getCaption(), descriptor.getIcon());
		button.setDescription(button.getCaption());
		button.setCaptionAsHtml(true);
		button.setPrimaryStyleName("valo-menu-item");
		button.addClickListener(e -> {
			try {
				descriptor.itemInvoked(getUI());
			} finally {
				setEnabled(true);
			}
		});

		if (smallButton) {
			button.setCaption(null);
			button.addStyleName(ValoTheme.BUTTON_HUGE);
			button.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		}

		return button;
	}

	void setCollapsed(boolean collapsed) {
		if (collapsed && menuItemsLayout.isVisible()) {
			menuItemsLayout.setVisible(false);
			menuItemsCollapsedLayout.setVisible(true);
		} else if (!collapsed && menuItemsCollapsedLayout.isVisible()) {
			menuItemsLayout.setVisible(true);
			menuItemsCollapsedLayout.setVisible(false);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		ViewsSideBarMenu that = (ViewsSideBarMenu) o;
		return Objects.equals(menuItemsLayout, that.menuItemsLayout) &&
				Objects.equals(menuItemsCollapsedLayout, that.menuItemsCollapsedLayout) &&
				Objects.equals(menu, that.menu) &&
				Objects.equals(itemFilter, that.itemFilter) &&
				Objects.equals(sideBarUtils, that.sideBarUtils);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), menuItemsLayout, menuItemsCollapsedLayout, menu, itemFilter, sideBarUtils);
	}
}
