package de.hswt.fi.ui.vaadin.handler;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.VaadinSessionScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBus.SessionEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.util.Locale;

@SpringComponent
@VaadinSessionScope
public class ChangeLocaleHandler extends AbstractHandler<SessionEventBus> {

	private static final long serialVersionUID = -1788815156537858822L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ChangeLocaleHandler.class);

	@EventBusListenerMethod
	protected void handleEvent(Event<Locale> event) {
		LOGGER.debug("entering event bus listener handleEvent with Event<Locale> {}",
				event.getPayload());

		Locale locale = event.getPayload();

		if (!isValidLocale(event.getPayload())) {
			return;
		}

		VaadinSession.getCurrent().setLocale(locale);
		LocaleContextHolder.setLocale(locale);
		Page.getCurrent().reload();
	}

	private boolean isValidLocale(Locale locale) {
		return locale.equals(Locale.GERMANY) || locale.equals(Locale.GERMAN) || locale.equals(Locale.US) ||locale.equals(Locale.ENGLISH);
	}

}
