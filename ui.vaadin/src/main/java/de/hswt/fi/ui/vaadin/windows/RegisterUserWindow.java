package de.hswt.fi.ui.vaadin.windows;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.ValidationResult;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import de.hswt.fi.security.service.api.SecurityService;
import de.hswt.fi.security.service.model.RegisteredUser;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.i18n.I18N;

@SpringComponent
@PrototypeScope
public class RegisterUserWindow extends AbstractWindow {

	private static final long serialVersionUID = 7299495298671320424L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterUserWindow.class);

	private final SecurityService securityService;

	private RegisteredUser registeredUser;

	private Binder<RegisteredUser> binder;

	private Binder.Binding<RegisteredUser, String> secondPasswordBinding;

	private TextField userNameTextField;

	private TextField mailTextField;

	private PasswordField passwordTextField;

	private TextField firstNameTextField;

	private TextField lastNameTextField;

	private TextField organisationTextField;

	@Autowired
	protected RegisterUserWindow(ComponentFactory componentFactory, I18N i18n, SecurityService securityService) {

		super(componentFactory, i18n, false);
		this.securityService = securityService;

		setWidth(LayoutConstants.WINDOW_WIDTH_MEDIUM);

		binder = new Binder<>();
		binder.addValueChangeListener(event -> setCanFinish(binder.isValid()));
		registeredUser = new RegisteredUser();
	}

	@Override
	protected String getWindowCaption() {
		return i18n.get(UIMessageKeys.REQUEST_USER_ACCOUNT_WINDOW_CAPTION);
	}

	@Override
	protected Component getContentComponent() {

		CssLayout contentLayout = new CssLayout();
		contentLayout.setSizeFull();
		contentLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_COLUMN);

		Label descriptionLabel = new Label(
				i18n.get(UIMessageKeys.REQUEST_USER_ACCOUNT_WINDOW_DESCRIPTION));
		contentLayout.addComponent(componentFactory.createRowLayout(descriptionLabel));

		mailTextField = componentFactory.createTextField(i18n.get(UIMessageKeys.USER_EMAIL), 60);
		binder.forField(mailTextField)
				.asRequired("")
				.withValidator((value, valueContext) -> handleMailTextFieldChange(value))
				.bind(RegisteredUser::getMail, RegisteredUser::setMail);
		contentLayout.addComponent(componentFactory.createRowLayout(mailTextField));

		userNameTextField = componentFactory.createTextField(i18n.get(UIMessageKeys.USER_USERNAME),
				60);
		binder.forField(userNameTextField)
				.bind(RegisteredUser::getMail, RegisteredUser::setMail);
		contentLayout.addComponent(componentFactory.createRowLayout(userNameTextField));

		passwordTextField = new PasswordField(
				i18n.get(UIMessageKeys.USER_PASSWORD) + " (min." + 6 + ")");
		passwordTextField.setWidth("100%");
		binder.forField(passwordTextField)
				.asRequired("")
				.withValidator(value -> {
					validateSecondPasswordField();
					return value.length() >= 6;
				}, "The password you have entered is too short")
				.bind(RegisteredUser::getPassword, RegisteredUser::setPassword);
		contentLayout.addComponent(componentFactory.createRowLayout(passwordTextField));

		PasswordField passwordConfirmTextField = new PasswordField(
				i18n.get(UIMessageKeys.USER_PASSWORD_CONFIRMATION));
		passwordConfirmTextField.setWidth("100%");
		secondPasswordBinding = binder.forField(passwordConfirmTextField)
				.asRequired("")
				.withValidator(value -> value.equals(passwordTextField.getValue()), "Passwords do not match")
				.bind(RegisteredUser::getPassword, RegisteredUser::setPassword);
		contentLayout.addComponent(componentFactory.createRowLayout(passwordConfirmTextField));

		firstNameTextField = componentFactory
				.createTextField(i18n.get(UIMessageKeys.USER_FIRSTNAME), 40);
		binder.forField(firstNameTextField)
				.bind(RegisteredUser::getFirstname, RegisteredUser::setFirstname);
		contentLayout.addComponent(componentFactory.createRowLayout(firstNameTextField));

		lastNameTextField = componentFactory.createTextField(i18n.get(UIMessageKeys.USER_LASTNAME),
				40);
		binder.forField(lastNameTextField)
				.bind(RegisteredUser::getLastname, RegisteredUser::setLastname);
		contentLayout.addComponent(componentFactory.createRowLayout(lastNameTextField));

		organisationTextField = componentFactory
				.createTextField(i18n.get(UIMessageKeys.USER_ORGANISATION), 80);
		binder.forField(organisationTextField)
				.bind(RegisteredUser::getOrganisation, RegisteredUser::setOrganisation);
		contentLayout.addComponent(componentFactory.createRowLayout(organisationTextField));

		return contentLayout;
	}

	private void validateSecondPasswordField() {
		secondPasswordBinding.validate();
	}

	@Override
	protected void handleOk() {
		getUserFromFields(registeredUser);
	}

	private void getUserFromFields(RegisteredUser registeredUser) {
		registeredUser.setUsername(userNameTextField.getValue());
		registeredUser.setMail(mailTextField.getValue());
		registeredUser.setPassword(passwordTextField.getValue());
		registeredUser.setFirstname(firstNameTextField.getValue());
		registeredUser.setLastname(lastNameTextField.getValue());
		registeredUser.setOrganisation(organisationTextField.getValue());
	}

	private ValidationResult handleMailTextFieldChange(String value) {

		userNameTextField.setValue(value);

		if (EmailValidator.getInstance().isValid(value)) {
			if (securityService.userExists(value)) {
				return ValidationResult.error(i18n.get(UIMessageKeys.REQUEST_USER_ACCOUNT_ERROR_EMAIL_IN_USE));
			} else {
				return ValidationResult.ok();
			}
		}
		else {
			return ValidationResult.error(i18n.get(UIMessageKeys.REQUEST_USER_ACCOUNT_ERROR_INVALID_EMAIL));
		}
	}

	public void clear() {
		registeredUser = new RegisteredUser();
		userNameTextField.setReadOnly(true);
		setCanFinish(false);
		mailTextField.focus();
	}

	public RegisteredUser getUser() {
		try {
			binder.writeBean(registeredUser);
		} catch (ValidationException e) {
			LOGGER.error(e.getMessage());
		}
		return registeredUser;
	}

}
