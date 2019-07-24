package de.hswt.fi.ui.vaadin.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.CssLayout;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

import javax.annotation.PostConstruct;
import java.util.Locale;

@SideBarItem(sectionId = Sections.TOP, captionCode = UIMessageKeys.HOME_VIEW_CAPTION, order = 0)
@VaadinFontIcon(VaadinIcons.HOME)
@SpringView(name = HomeView.VIEW_NAME)
public class HomeView extends CssLayout implements View {

	private static final String DEFAULT_ENGLISH_FILE_LOCATION = "../../html/partner_en.html";

	private static final String DEFAULT_GERMAN_FILE_LOCATION = "../../html/partner_de.html";

	private static final String SPRING_PROPERTY_HTML_HOME_EN = "html-home-english";

	private static final String SPRING_PROPERTY_HTML_HOME_DE = "html-home-german";

	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "home";

	private final String englishFileLocation;

	private final String germanFileLocation;

	@Autowired
	public HomeView(@Value("${" + SPRING_PROPERTY_HTML_HOME_EN + ":}") String englishFileLocation,
					@Value("${" + SPRING_PROPERTY_HTML_HOME_DE + ":}") String germanFileLocation) {
		this.englishFileLocation = englishFileLocation;
		this.germanFileLocation = germanFileLocation;
	}

	@PostConstruct
	private void postConstruct() {
		setSizeFull();

		addStyleName(CustomValoTheme.RELATIVE);
		addStyleName(CustomValoTheme.BLOCK);

		initHeader();
		initContent();
	}

	private void initHeader() {
		CssLayout headerLayout = new CssLayout();
		headerLayout.setWidth("100%");
		headerLayout.addStyleName(CustomValoTheme.BACKGROUND_COLOR_DEFAULT);
		headerLayout.addStyleName(CustomValoTheme.PADDING);
		headerLayout.addStyleName(CustomValoTheme.CSS_SHADOW_BORDER);
		headerLayout.setHeight(LayoutConstants.HEADER_HEIGHT_VIEW);
		addComponent(headerLayout);
	}

	private void initContent() {

		Locale locale = VaadinSession.getCurrent().getLocale();
		Resource resource = (locale.equals(Locale.GERMANY) || locale.equals(Locale.GERMAN)) ? getGermanResource() : getEnglishResource();

		BrowserFrame browserFrame = new BrowserFrame(null, resource);
		browserFrame.setSizeFull();
		browserFrame.addStyleName(CustomValoTheme.PADDING_LEFT);
		browserFrame.addStyleName(CustomValoTheme.SKIP_HEADER_VIEW);
		browserFrame.addStyleName(CustomValoTheme.BLOCK);
		browserFrame.addStyleName(CustomValoTheme.RELATIVE);
		addComponent(browserFrame);
	}

	private Resource getEnglishResource() {
		return englishFileLocation.isEmpty() ?
				new ThemeResource(DEFAULT_ENGLISH_FILE_LOCATION) : new ExternalResource(englishFileLocation);
	}

	private Resource getGermanResource() {
		return germanFileLocation.isEmpty() ?
				new ThemeResource(DEFAULT_GERMAN_FILE_LOCATION) : new ExternalResource(germanFileLocation);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		//Nothing special to do here
	}
}
