package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.server.Resource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Component;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.components.ContainerContentComponent;

import javax.annotation.PostConstruct;
import java.util.Locale;

public abstract class AbstractHelpComponent extends ContainerContentComponent {

	private static final long serialVersionUID = 1L;

	protected abstract Resource getDefaultHelpResource();

	protected abstract Resource getGermanHelpResource();

	@PostConstruct
	private void postConstruct() {
		initLayout();
	}

	private void initLayout() {

		setSizeFull();

		Locale locale = VaadinSession.getCurrent().getLocale();
		Resource resource = locale.equals(Locale.GERMAN) ? getGermanHelpResource() : getDefaultHelpResource();

		BrowserFrame browserFrame = new BrowserFrame(null, resource);
		browserFrame.setSizeFull();
		browserFrame.addStyleName(CustomValoTheme.PADDING_HALF_LEFT);
		addComponent(browserFrame);
	}

	@Override
	public Component getHeaderComponent() {
		return null;
	}

}