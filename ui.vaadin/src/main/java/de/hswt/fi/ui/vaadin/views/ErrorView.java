package de.hswt.fi.ui.vaadin.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ErrorView extends VerticalLayout implements View {

	private static final long serialVersionUID = -1349484555495574658L;

	private Label message;

	public ErrorView() {
		setMargin(true);
		addComponent(message = new Label());
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		message.setValue(String.format("No such view: %s", event.getViewName()));
	}
}