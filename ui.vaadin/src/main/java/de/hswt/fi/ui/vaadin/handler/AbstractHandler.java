package de.hswt.fi.ui.vaadin.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.Serializable;

public abstract class AbstractHandler<H extends EventBus> implements Serializable {

	private static final long serialVersionUID = -3675435114936876866L;

	protected H eventBus;

	@PostConstruct
	private void postConstructInternal() {
		this.eventBus.subscribe(this);
		postConstruct();
	}

	@PreDestroy
	private void preDestroyInternal() {
		this.eventBus.unsubscribe(this);
		preDestroy();
	}

	protected void postConstruct() {

	}

	protected void preDestroy() {

	}

	@Autowired
	public void setEventBus(H eventBus) {
		this.eventBus = eventBus;
	}
}
