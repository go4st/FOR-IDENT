package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.vaadin.spring.i18n.I18N;

@SpringComponent
@ViewScope
public class ProcessingHelpComponent extends AbstractHelpComponent {

	private final I18N i18n;

	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_ENGLISH_HELP_LOCATION = "../../html/ProcessingHelp_en.html";

	private static final String DEFAULT_GERMAN_HELP_LOCATION = "../../html/ProcessingHelp_de.html";

	private static final String SPRING_PROPERTY_HTML_HELP_PROCESSING_EN = "html-help-processing-english";

	private static final String SPRING_PROPERTY_HTML_HELP_PROCESSING_DE = "html-help-processing-german";

	private final String englishHelpLocation;

	private final String germanHelpLocation;

	@Autowired
	public ProcessingHelpComponent(I18N i18n,
								   @Value("${" + SPRING_PROPERTY_HTML_HELP_PROCESSING_EN + ":}") String englishHelpLocation,
								   @Value("${" + SPRING_PROPERTY_HTML_HELP_PROCESSING_DE + ":}") String germanHelpLocation) {
		this.i18n = i18n;
		this.englishHelpLocation = englishHelpLocation;
		this.germanHelpLocation = germanHelpLocation;
	}

	@Override
	public String getTitle() {
		return i18n.get(UIMessageKeys.PROCESSING_RESULTS_HELP_COMPONENT_CAPTION);
	}

	@Override
	protected Resource getDefaultHelpResource() {
		return englishHelpLocation.isEmpty() ?
				new ThemeResource(DEFAULT_ENGLISH_HELP_LOCATION) : new ExternalResource(englishHelpLocation);
	}

	@Override
	protected Resource getGermanHelpResource() {

		return germanHelpLocation.isEmpty() ?
				new ThemeResource(DEFAULT_GERMAN_HELP_LOCATION) : new ExternalResource(germanHelpLocation);
	}
}
