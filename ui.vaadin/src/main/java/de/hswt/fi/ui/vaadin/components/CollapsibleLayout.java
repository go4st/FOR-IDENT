package de.hswt.fi.ui.vaadin.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import de.hswt.fi.ui.vaadin.CustomValoTheme;

import java.util.Objects;

@SpringComponent
@ViewScope
public class CollapsibleLayout extends CssLayout {

	private static final long serialVersionUID = -7368080662378921759L;

	private boolean collapsed;

	private String caption;

	private CssLayout headerLayout;

	private Component content;

	private boolean collapseAble;

	private Button expandButton;

	public CollapsibleLayout(String caption, Component content, boolean collapseAble, boolean collapsed) {

		this.caption = caption;
		this.content = content;
		this.collapseAble = collapseAble;
		this.collapsed = collapsed;

		init();
	}

	private void init() {

		headerLayout = new CssLayout();
		headerLayout.addStyleName(CustomValoTheme.PADDING_HALF_BOTTOM);
		headerLayout.addStyleName(CustomValoTheme.BORDER_BOTTOM_COLOR_BLACK);
		headerLayout.addStyleName(CustomValoTheme.TABLE);
		headerLayout.setWidth("100%");

		Label captionLabel = new Label(caption);
		captionLabel.setWidthUndefined();
		captionLabel.addStyleName(CustomValoTheme.LABEL_VERY_BOLD);
		captionLabel.addStyleName(CustomValoTheme.CSS_LAYOUT_TABLE_CELL_MIDDLE_TEXT_LEFT);
		captionLabel.addStyleName(CustomValoTheme.TEXT_ALIGN_LEFT);
		headerLayout.addComponent(captionLabel);

		addComponent(headerLayout);
		addComponent(content);

		if(collapseAble) {
			addCollapseButton();
		}
	}

	public void addHeaderButton(Button button) {
		button.addStyleName(CustomValoTheme.CSS_LAYOUT_TABLE_CELL_MIDDLE_TEXT_LEFT);
		button.addStyleName(CustomValoTheme.FLOAT_RIGHT);
		headerLayout.addComponent(button);
	}

	private void addCollapseButton() {
		expandButton = new Button(VaadinIcons.CHEVRON_UP);
		expandButton.setPrimaryStyleName("v-filterselect-button");
		expandButton.addStyleName(CustomValoTheme.FLOAT_RIGHT);
		headerLayout.addComponent(expandButton);

		if (collapseAble && collapsed) {
			hide();
		}
		
		// Button functionality
		expandButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7137933055318510522L;
			
			@Override
			public void buttonClick(ClickEvent event) {
				toggleContent();
			}
		});
	}

	public void show() {
		content.setVisible(true);
		expandButton.setIcon(VaadinIcons.CHEVRON_DOWN);
		collapsed = false;
	}

	private void hide() {
		content.setVisible(false);
		expandButton.setIcon(VaadinIcons.CHEVRON_UP);
		collapsed = true;
	}

	private void toggleContent() {
		if (collapsed) {
			show();
		} else {
			hide();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		CollapsibleLayout that = (CollapsibleLayout) o;
		return collapsed == that.collapsed &&
				collapseAble == that.collapseAble &&
				Objects.equals(caption, that.caption) &&
				Objects.equals(headerLayout, that.headerLayout) &&
				Objects.equals(content, that.content) &&
				Objects.equals(expandButton, that.expandButton);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), collapsed, caption, headerLayout, content, collapseAble, expandButton);
	}
}