package de.hswt.fi.ui.vaadin.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@UIScope
public class AccessDeniedView extends VerticalLayout implements View {

	private static final long serialVersionUID = -6355152419435091404L;

	private Label message;

	public AccessDeniedView() {
		setMargin(true);
		message = new Label();
		addComponent(message);
		message.setSizeUndefined();
		message.addStyleName(ValoTheme.LABEL_FAILURE);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		message.setValue(
				String.format("You do not have access to this view: %s", event.getViewName()));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		AccessDeniedView that = (AccessDeniedView) o;

		return message != null ? message.equals(that.message) : that.message == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (message != null ? message.hashCode() : 0);
		return result;
	}
}