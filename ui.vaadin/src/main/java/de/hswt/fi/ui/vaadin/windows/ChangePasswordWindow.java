package de.hswt.fi.ui.vaadin.windows;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import de.hswt.fi.application.properties.ApplicationProperties;
import de.hswt.fi.common.PasswordGenerator;
import de.hswt.fi.security.service.model.RegisteredUser;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.i18n.I18N;

@SpringComponent
@PrototypeScope
public class ChangePasswordWindow extends AbstractWindow {

	private static final long serialVersionUID = 6784811048808864563L;

	private final ApplicationProperties applicationProperties;

	private final PasswordGenerator passwordGenerator;

	private RegisteredUser registeredUser;

	private TextField passwordTextField;

	@Autowired
	protected ChangePasswordWindow(ComponentFactory componentFactory, I18N i18n, ApplicationProperties applicationProperties, PasswordGenerator passwordGenerator) {
		super(componentFactory, i18n, false);
		this.applicationProperties = applicationProperties;
		this.passwordGenerator = passwordGenerator;
	}

	public void setUser(RegisteredUser user) {
		if (user != null) {
			registeredUser = user;
			generatePassword();
		}
	}

	public RegisteredUser getUser() {
		return registeredUser;
	}

	@Override
	protected String getWindowCaption() {
		return i18n.get(UIMessageKeys.WINDOW_CHANGE_PASSWORD_CAPTION);
	}

	@Override
	protected Component getContentComponent() {
		CssLayout contentLayout = new CssLayout();
		contentLayout.setSizeFull();
		contentLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_COLUMN);

		passwordTextField = new TextField();
		passwordTextField.setCaption(i18n.get(UIMessageKeys.CHANGE_PASSWORD_WINDOW_PASSWORD));
		passwordTextField.setWidth("100%");
		passwordTextField.setReadOnly(true);

		CssLayout buttonWrapperLayout = new CssLayout();
		// layout hack, needs a caption
		buttonWrapperLayout.setCaption("");
		Button generatePasswordButton = componentFactory.createButton(
				i18n.get(UIMessageKeys.CHANGE_PASSWORD_WINDOW_GENERATE_PASSWORD), false);
		generatePasswordButton.addClickListener(e -> generatePassword());

		buttonWrapperLayout.addComponent(generatePasswordButton);

		contentLayout.addComponent(
				componentFactory.createRowLayout(passwordTextField, buttonWrapperLayout));

		return contentLayout;
	}

	private void generatePassword() {
		passwordTextField.setReadOnly(false);
		passwordTextField.setValue(passwordGenerator.generatePassword(applicationProperties.getSecurity().getPasswordLength()));
		passwordTextField.setReadOnly(true);
	}

	@Override
	protected void handleOk() {
		if (passwordTextField.getValue() != null && !passwordTextField.getValue().isEmpty()) {
			registeredUser.setPassword(passwordTextField.getValue());
		}
	}
}
