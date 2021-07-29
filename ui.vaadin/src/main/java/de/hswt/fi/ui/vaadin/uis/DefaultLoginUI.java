package de.hswt.fi.ui.vaadin.uis;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import de.hswt.fi.common.spring.SpringProfileUtil;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.handler.UIScopeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.i18n.I18N;

@SpringUI(path = "login")
@Theme("fi-valo")
@Widgetset(value = "de.hswt.fi.ui.vaadin.widgetset")
public class DefaultLoginUI extends AbstractLoginUI {

	private static final long serialVersionUID = 2939745829111476077L;

	@Value("${de.hswt.fi.ui.header.logo}")
	private String logo;

	@Autowired
    public DefaultLoginUI(SpringProfileUtil springProfileUtil, I18N i18n, EventBus.UIEventBus uiEventBus,
                          UIScopeHandler uiscopeHandler, @Value("${de.hswt.fi.ui.header.caption}") String title) {
        super(springProfileUtil, i18n, uiEventBus, uiscopeHandler);
		getPage().setTitle(title + " Login");
	}

	@Override
	protected void buildLogo() {
		CssLayout iconLayout = new CssLayout();
		iconLayout.addStyleName("logo");
		cssLayout.addComponent(iconLayout);

		Embedded logoEmbedded = new Embedded(null, new ThemeResource(logo));
		logoEmbedded.addStyleName(CustomValoTheme.FLOAT_RIGHT);
		logoEmbedded.addStyleName(CustomValoTheme.MARGIN);
		logoEmbedded.addStyleName(CustomValoTheme.BLOCK);
		logoEmbedded.setWidth(LayoutConstants.LOGO_WIDTH_SHORT);
		iconLayout.addComponent(logoEmbedded);
	}
}
