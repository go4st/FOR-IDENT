package de.hswt.fi.ui.vaadin.windows;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.ValidationResult;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
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
public class UserSettingsWindow extends AbstractWindow {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSettingsWindow.class);

    private final SecurityService securityService;

    private RegisteredUser registeredUser;

    private TextField userNameTextField;

    private TextField firstNameTextField;

    private TextField lastNameTextField;

    private PasswordField passwordField;

    private PasswordField passwordConfirmationField;

    private TextField mailTextField;

    private TextField organisationTextField;

    private CssLayout contentLayout;

    private Binder<RegisteredUser> binder;

    private String windowCaption;

    private boolean passwordChanged;

    @Autowired
    protected UserSettingsWindow(ComponentFactory componentFactory, I18N i18n, SecurityService securityService) {
        super(componentFactory, i18n, false);
        this.securityService = securityService;

        setWidth(LayoutConstants.WINDOW_WIDTH_MEDIUM);
        binder = new Binder<>();
    }

    @Override
    protected Component getContentComponent() {

        contentLayout = new CssLayout();
        contentLayout.setSizeFull();
        contentLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_COLUMN);

        initTextFields();
        contentLayout.addComponent(componentFactory.createSpacer("1rem"));
        contentLayout.addComponent(componentFactory.createSpacer("1rem"));

        binder.bind(userNameTextField, RegisteredUser::getUsername, RegisteredUser::setUsername);
        binder.bind(firstNameTextField, RegisteredUser::getFirstname, RegisteredUser::setFirstname);
        binder.bind(lastNameTextField, RegisteredUser::getLastname, RegisteredUser::setLastname);
        binder.forField(mailTextField)
                .withValidator((username, component) -> {
                    if (EmailValidator.getInstance().isValid(username)) {
                        if (!username.equals(registeredUser.getUsername()) && securityService.userExists(username)) {
                            setComponentError(mailTextField, i18n.get(UIMessageKeys.USER_EMAIL_IS_NOT_AVAILABLE));
                            return ValidationResult.error(i18n.get(UIMessageKeys.USER_SETTINGS_WINDOW_EMAIL_ALREADY_REGISTERED));
                        } else {
                            mailTextField.setComponentError(null);
                            setCanFinish(isValidPassword());
                            return ValidationResult.ok();
                        }
                    } else {
                        setComponentError(mailTextField, i18n.get(UIMessageKeys.USER_EMAIL_IS_INVALID));
                        return ValidationResult.error(i18n.get(UIMessageKeys.USER_SETTINGS_WINDOW_EMAIL_INVALID));
                    }
                }).bind(RegisteredUser::getMail, RegisteredUser::setMail);
        binder.bind(organisationTextField, RegisteredUser::getOrganisation, RegisteredUser::setOrganisation);

        return contentLayout;
    }

    private void setComponentError(TextField field, String errorMessage) {
        field.setComponentError(new UserError(errorMessage));
        setCanFinish(false);
    }

    private void initTextFields() {

        mailTextField = componentFactory.createTextField(
                i18n.get(UIMessageKeys.USER_EMAIL, VaadinSession.getCurrent().getLocale()), 60);
        contentLayout.addComponent(componentFactory.createRowLayout(mailTextField));

        userNameTextField = componentFactory.createTextField(
                i18n.get(UIMessageKeys.USER_USERNAME, VaadinSession.getCurrent().getLocale()), 60);
        userNameTextField.setEnabled(false);
        contentLayout.addComponent(componentFactory.createRowLayout(userNameTextField));

        passwordField = new PasswordField(
                i18n.get(UIMessageKeys.USER_PASSWORD, VaadinSession.getCurrent().getLocale()) + " (min." + 6
                        + ")");
        passwordField.setWidth("100%");
        passwordField.addBlurListener(event -> setCanFinish(isValidPassword()));
        passwordField.addValueChangeListener(event -> setCanFinish(isValidPassword()));
        contentLayout.addComponent(componentFactory.createRowLayout(passwordField));

        passwordConfirmationField = new PasswordField(
                i18n.get(UIMessageKeys.USER_PASSWORD_CONFIRMATION, VaadinSession.getCurrent().getLocale()));
        passwordConfirmationField.setWidth("100%");
        passwordConfirmationField.addValueChangeListener(event -> setCanFinish(isValidPassword()));
        contentLayout.addComponent(componentFactory.createRowLayout(passwordConfirmationField));

        firstNameTextField = componentFactory.createTextField(
                i18n.get(UIMessageKeys.USER_FIRSTNAME, VaadinSession.getCurrent().getLocale()), 40);
        contentLayout.addComponent(componentFactory.createRowLayout(firstNameTextField));

        lastNameTextField = componentFactory.createTextField(
                i18n.get(UIMessageKeys.USER_LASTNAME, VaadinSession.getCurrent().getLocale()), 40);
        contentLayout.addComponent(componentFactory.createRowLayout(lastNameTextField));

        organisationTextField = componentFactory.createTextField(
                i18n.get(UIMessageKeys.USER_ORGANISATION, VaadinSession.getCurrent().getLocale()), 80);
        contentLayout.addComponent(componentFactory.createRowLayout(organisationTextField));

    }

    @Override
    public void setWindowCaption(String windowCaption) {
        this.windowCaption = windowCaption;
        setCaption(windowCaption);
    }

    @Override
    protected String getWindowCaption() {
        return windowCaption;
    }

    @Override
    protected void handleOk() {

        try {
            binder.writeBean(registeredUser);

            if (!passwordField.getValue().isEmpty()) {
                registeredUser.setPassword(passwordField.getValue());
                passwordChanged = true;
            }
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    private boolean isValidPassword() {

        String password = passwordField.getValue();
        String confirmedPassword = passwordConfirmationField.getValue();

        if (!password.equals(confirmedPassword)) {
            return false;
        }

        return password.length() >= 6 ||
                registeredUser.getPassword() != null && !registeredUser.getPassword().isEmpty() && password.isEmpty() ||
                registeredUser.getPassword() != null && !registeredUser.getPassword().isEmpty();

    }

    public void clear() {
        setCanFinish(false);
        registeredUser = new RegisteredUser();
        binder.readBean(registeredUser);
        userNameTextField.setEnabled(false);
        mailTextField.focus();
        passwordField.clear();
        passwordConfirmationField.clear();
    }

    public void setUser(RegisteredUser registeredUser) {
        this.registeredUser = registeredUser;
        binder.readBean(registeredUser);
        mailTextField.setComponentError(null);
        userNameTextField.setEnabled(false);
        setCanFinish(true);
    }

    public RegisteredUser getUser() {
        return registeredUser;
    }

    public boolean isPasswordChanged() {
        return passwordChanged;
    }
}
