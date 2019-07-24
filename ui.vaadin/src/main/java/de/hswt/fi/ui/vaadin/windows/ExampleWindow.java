package de.hswt.fi.ui.vaadin.windows;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import de.hswt.fi.security.service.api.SecurityService;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.i18n.I18N;

@SpringComponent
@PrototypeScope
public class ExampleWindow extends AbstractWindow {

	private static final long serialVersionUID = 366183931194952138L;

	private static final Logger LOG = LoggerFactory.getLogger(ExampleWindow.class);

	private final SecurityService securityService;

	private TextField okButtonCaptionTextField;

	@Autowired
	public ExampleWindow(ComponentFactory componentFactory, I18N i18N, SecurityService securityService) {
		super(componentFactory, i18N, false);
		this.securityService = securityService;
	}

	@Override
	protected String getWindowCaption() {
		return "Example Window";
	}

	@Override
	protected Component getContentComponent() {
		CssLayout contentLayout = new CssLayout();
		contentLayout.setSizeFull();
		contentLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_COLUMN);

		contentLayout.addComponent(new Label("Window configuration"));

		CheckBox okButtonCheckbox = new CheckBox("Ok button enabled (can finish)");
		okButtonCheckbox.addStyleName(CustomValoTheme.MARGIN_VERTICAL);
		okButtonCheckbox.setValue(true);
		okButtonCheckbox.addValueChangeListener(e -> setCanFinish(okButtonCheckbox.getValue()));
		contentLayout.addComponent(okButtonCheckbox);

		CheckBox cancelButtonCheckbox = new CheckBox("Visible cancel button");
		cancelButtonCheckbox.addStyleName(CustomValoTheme.MARGIN_BOTTOM);
		cancelButtonCheckbox.setValue(true);
		cancelButtonCheckbox.addValueChangeListener(
				e -> setCancelButtonVisible(cancelButtonCheckbox.getValue()));
		contentLayout.addComponent(cancelButtonCheckbox);

		okButtonCaptionTextField = new TextField("Ok button caption", "Ok");
		okButtonCaptionTextField.setWidth("100%");
		okButtonCaptionTextField.addStyleName(CustomValoTheme.MARGIN_BOTTOM);
		okButtonCaptionTextField.addValueChangeListener(event -> setOkButtonCaption(event.getValue()));
		contentLayout.addComponent(okButtonCaptionTextField);

		TextField cancelButtonCaptionTextField = new TextField("Cancel button caption", "Cancel");
		cancelButtonCaptionTextField.setWidth("100%");
		cancelButtonCaptionTextField.addStyleName(CustomValoTheme.MARGIN_BOTTOM);
		cancelButtonCaptionTextField.addValueChangeListener(event -> setCancelButtonCaption(event.getValue()));
		contentLayout.addComponent(cancelButtonCaptionTextField);

		Label serviceLabel = new Label();
		serviceLabel
				.setValue("Your user name is: " + securityService.getCurrentUser().getUsername());
		contentLayout.addComponent(serviceLabel);

		setCancelButtonVisible(false);

		return contentLayout;
	}

	@Override
	protected void handleOk() {
		LOG.debug("inside ok clicked");
	}
}
