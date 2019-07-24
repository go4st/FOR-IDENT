package de.hswt.fi.ui.vaadin.windows;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.i18n.I18N;

@SpringComponent
@PrototypeScope
public class RefreshTPDatabaseWindow extends AbstractWindow {

	private static final long serialVersionUID = -2153612655404127892L;

	@Autowired
	protected RefreshTPDatabaseWindow(ComponentFactory componentFactory, I18N i18n) {
		super(componentFactory,i18n, false);
		setWidth(LayoutConstants.HUGE);
	}

	@Override
	protected String getWindowCaption() {
		return i18n.get(UIMessageKeys.ADMIN_CONFIRM_REFRESH_TP_DATABASE_WINDOW_CAPTION);
	}

	@Override
	protected Component getContentComponent() {

		CssLayout contentLayout = new CssLayout();
		contentLayout.setSizeFull();

		Label descriptionLabel = new Label(i18n.get(UIMessageKeys.ADMIN_CONFIRM_REFRESH_TP_DATABASE_WINDOW_DESCRIPTION));
		descriptionLabel.setSizeFull();
		descriptionLabel.addStyleName(CustomValoTheme.PADDING_BOTTOM);
		contentLayout.addComponent(componentFactory.createRowLayout(descriptionLabel));

		return contentLayout;
	}

	@Override
	protected void handleOk() {
	}
}
