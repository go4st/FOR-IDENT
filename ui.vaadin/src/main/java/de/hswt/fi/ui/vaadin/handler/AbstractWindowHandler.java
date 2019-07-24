package de.hswt.fi.ui.vaadin.handler;

import com.vaadin.shared.Registration;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;
import org.vaadin.spring.events.EventBus;

public abstract class AbstractWindowHandler<H extends EventBus> extends AbstractHandler<H>
		implements CloseListener {

	private static final long serialVersionUID = -2606038970650048801L;

	private Registration registration;

	protected abstract Window getWindow();

	@Override
	protected void postConstruct() {
		Window window = getWindow();
		if (window != null) {
			registration = window.addCloseListener(this);
		}
	}

	@Override
	protected void preDestroy() {
		Window window = getWindow();
		if (window != null) {
			registration.remove();
		}
	}
}
