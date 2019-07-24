package de.hswt.fi.ui.vaadin.components;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import de.hswt.fi.ui.vaadin.layouts.ViewLayout;

public abstract class ContainerContentComponent extends CssLayout {

	private static final long serialVersionUID = -5854162615443926243L;

	public abstract String getTitle();

	public abstract Component getHeaderComponent();

	public Label getTitleLabel() {
		return new Label(getTitle());
	}

	public void layoutChanged(ViewLayout currentLayout) { }

}
