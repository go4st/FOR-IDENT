package de.hswt.fi.ui.vaadin.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CssLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ViewEventBus;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public abstract class AbstractView extends CssLayout implements View {

	private static final long serialVersionUID = -5880949954005605920L;

	@Autowired
	protected ViewEventBus eventBus;

	@PostConstruct
	private void postConstructInternal() {
		eventBus.subscribe(this);
		postConstruct();
	}

	@PreDestroy
	private void preDestroyInternal() {
		eventBus.unsubscribe(this);
		preDestroy();
	}

	protected abstract void postConstruct();

	protected void preDestroy() {
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

	// Do not override hash() and equals() in abstract component classes because of identity issues when attach / remove
}
