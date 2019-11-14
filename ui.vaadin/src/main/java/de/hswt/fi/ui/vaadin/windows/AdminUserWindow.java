package de.hswt.fi.ui.vaadin.windows;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.server.UserError;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import de.hswt.fi.application.properties.ApplicationProperties;
import de.hswt.fi.common.PasswordGenerator;
import de.hswt.fi.security.service.api.SecurityService;
import de.hswt.fi.security.service.model.Database;
import de.hswt.fi.security.service.model.Group;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringComponent
@PrototypeScope
public class AdminUserWindow extends AbstractWindow {

	private static final long serialVersionUID = 2888491689574275009L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminUserWindow.class);

	private final SecurityService securityService;

	private final ApplicationProperties applicationProperties;

	private final PasswordGenerator passwordGenerator;

	private Binder<RegisteredUser> binder;

	private RegisteredUser registeredUser;

	private TextField userNameTextField;

	private TextField firstNameTextField;

	private TextField lastNameTextField;

	private TextField passwordField;

	private TextField mailTextField;

	private TextField organisationTextField;

	private CheckBox userEnabledCheckbox;

	private CheckBoxGroup<Group> securityGroupOptionGroup;

	private CheckBoxGroup<Database> securityDatabaseOptionGroup;

	private CheckBox sendMailCheckbox;

	private CssLayout contentLayout;

	private CssLayout passwordComponentLayout;

	private String windowCaption;

	private CssLayout checkBoxLayout;

	@Autowired
	protected AdminUserWindow(I18N i18n, ComponentFactory componentFactory, SecurityService securityService,
							  ApplicationProperties applicationProperties, PasswordGenerator passwordGenerator) {
		super(componentFactory, i18n, false);

		this.securityService = securityService;
		this.applicationProperties = applicationProperties;
		this.passwordGenerator = passwordGenerator;

		setWidth(LayoutConstants.WINDOW_WIDTH_MEDIUM);

		binder = new Binder<>();
	}

	@Override
	protected Component getContentComponent() {

		contentLayout = new CssLayout();
		contentLayout.setSizeFull();
		contentLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_COLUMN);

		initTextFields();
		initCheckBoxes();
		initGroupList();
		initDatabasesList();

		binder.bind(userNameTextField, RegisteredUser::getUsername, RegisteredUser::setUsername);
		binder.bind(firstNameTextField, RegisteredUser::getFirstname, RegisteredUser::setFirstname);
		binder.bind(lastNameTextField, RegisteredUser::getLastname, RegisteredUser::setLastname);
		binder.bind(mailTextField, RegisteredUser::getMail, RegisteredUser::setMail);
		binder.bind(organisationTextField, RegisteredUser::getOrganisation, RegisteredUser::setOrganisation);
		binder.bind(userEnabledCheckbox, RegisteredUser::isEnabled, RegisteredUser::setEnabled);

		return contentLayout;
	}

	private void initTextFields() {

		CssLayout textFieldsLayout = new CssLayout();

		mailTextField = componentFactory.createTextField(i18n.get(UIMessageKeys.USER_EMAIL), 60);
		mailTextField.addValueChangeListener(event -> handleMailTextFieldChange(event.getValue()));
		textFieldsLayout.addComponent(componentFactory.createRowLayout(mailTextField));

		userNameTextField = componentFactory.createTextField(i18n.get(UIMessageKeys.USER_USERNAME),
				60);
		userNameTextField.setEnabled(false);
		textFieldsLayout.addComponent(componentFactory.createRowLayout(userNameTextField));

		passwordField = new TextField();
		passwordField.setCaption(i18n.get(UIMessageKeys.USER_PASSWORD));
		passwordField.setWidth("100%");
		passwordField.setReadOnly(true);

		CssLayout buttonWrapperLayout = new CssLayout();
		buttonWrapperLayout.setCaption(""); // layout hack, needs a caption

		Button generatePasswordButton = componentFactory.createButton(
				i18n.get(UIMessageKeys.CHANGE_PASSWORD_WINDOW_GENERATE_PASSWORD), false);

		generatePasswordButton.addClickListener(e -> generatePassword());

		buttonWrapperLayout.addComponent(generatePasswordButton);

		passwordComponentLayout = componentFactory.createRowLayout(passwordField,
				buttonWrapperLayout);
		textFieldsLayout.addComponent(passwordComponentLayout);

		firstNameTextField = componentFactory
				.createTextField(i18n.get(UIMessageKeys.USER_FIRSTNAME), 40);
		textFieldsLayout.addComponent(componentFactory.createRowLayout(firstNameTextField));

		lastNameTextField = componentFactory.createTextField(i18n.get(UIMessageKeys.USER_LASTNAME),
				40);
		textFieldsLayout.addComponent(componentFactory.createRowLayout(lastNameTextField));

		organisationTextField = componentFactory
				.createTextField(i18n.get(UIMessageKeys.USER_ORGANISATION), 80);
		textFieldsLayout.addComponent(componentFactory.createRowLayout(organisationTextField));

		textFieldsLayout.addComponent(componentFactory.createSpacer("1rem"));

		contentLayout.addComponent(textFieldsLayout);

	}

	private void initCheckBoxes() {

		checkBoxLayout = new CssLayout();

		userEnabledCheckbox = new CheckBox(i18n.get(UIMessageKeys.USER_ENABLED));
		checkBoxLayout.addComponent(componentFactory.createRowLayout(userEnabledCheckbox));

		userEnabledCheckbox.addValueChangeListener(e -> {
			if (userEnabledCheckbox.getValue()) {
				sendMailCheckbox.setEnabled(true);
				sendMailCheckbox.setValue(true);
			} else {
				sendMailCheckbox.setEnabled(false);
				sendMailCheckbox.setValue(false);
			}
		});

		sendMailCheckbox = new CheckBox(i18n.get(UIMessageKeys.USER_ACTIVATIONMAIL));
		checkBoxLayout.addComponent(componentFactory.createRowLayout(sendMailCheckbox));

		checkBoxLayout.addComponent(componentFactory.createSpacer("1rem"));
		contentLayout.addComponent(checkBoxLayout);

		sendMailCheckbox.setEnabled(false);
	}

	private void initGroupList() {

		List<Group> groups = securityService.findAllGroups();

		securityGroupOptionGroup = new CheckBoxGroup<>("Groups");
		securityGroupOptionGroup.setItems(groups);
		securityGroupOptionGroup.setHeight("100%");
		securityGroupOptionGroup.addStyleName(CustomValoTheme.PADDING);
		securityGroupOptionGroup.setItemCaptionGenerator(this::getGroupCaption);

		contentLayout.addComponent(componentFactory.createRowLayout(securityGroupOptionGroup));
	}

	private void initDatabasesList() {

		List<Database> databases = securityService.findAllDatabases();

		securityDatabaseOptionGroup = new CheckBoxGroup<>("Databases");
		securityDatabaseOptionGroup.setItems(databases);
		securityDatabaseOptionGroup.setHeight("100%");
		securityDatabaseOptionGroup.addStyleName(CustomValoTheme.PADDING);
		securityDatabaseOptionGroup.setItemCaptionGenerator(Database::getName);

		contentLayout.addComponent(componentFactory.createRowLayout(securityDatabaseOptionGroup));
	}

	private String getGroupCaption(Group group) {

		switch (group.getName()) {
		case "GROUP_ADMIN":
			return "Admin";
		case "GROUP_SCIENTIST":
			return "Scientist";
		case "GROUP_USER":
			return "User";
		default:
			return "";
		}

	}

	private void generatePassword() {
		passwordField.setReadOnly(false);
		passwordField.setValue(passwordGenerator.generatePassword(applicationProperties.getSecurity().getPasswordLength()));
		passwordField.setReadOnly(true);
	}

	@Override
	protected String getWindowCaption() {
		return windowCaption;
	}

	@Override
	protected void handleOk() {

		try {
			binder.writeBean(registeredUser);
		} catch (ValidationException e) {
			LOGGER.error(e.getMessage());
		}

		Set<Group> selectedGroups = securityGroupOptionGroup.getValue();
		Set<Group> allGroups = new HashSet<>(securityService.findAllGroups());
		allGroups.removeAll(selectedGroups);

		selectedGroups.forEach(g -> registeredUser.addGroup(g));
		allGroups.forEach(registeredUser::removeGroup);

		Set<Database> selectDatabases = securityDatabaseOptionGroup.getSelectedItems();
		registeredUser.removeAllAccessibleDatabases();
		selectDatabases.forEach(registeredUser::addAccessbileDatabase);

		if (passwordComponentLayout.isVisible()) {
			registeredUser.setPassword(passwordField.getValue());
		}
	}

	public void setPasswordComponentVisible(boolean visible) {
		passwordComponentLayout.setVisible(visible);
	}

	public void setCheckBoxComponentVisible(boolean visible) {
		checkBoxLayout.setVisible(visible);
	}

	public void setUser(RegisteredUser user) {

		registeredUser = user == null ? new RegisteredUser() : user;

		Set<Group> userGroups = new HashSet<>(registeredUser.getGroups());
		securityGroupOptionGroup.setValue(userGroups);

		Set<Database> userDatabases = new HashSet<>(registeredUser.getAccessibleDatabases());
		securityDatabaseOptionGroup.setValue(userDatabases);

		binder.readBean(registeredUser);

		if (registeredUser.getId() == null) {
			generatePassword();
		}

		if (userEnabledCheckbox.getValue()) {
			sendMailCheckbox.setValue(false);
			sendMailCheckbox.setEnabled(false);
		}
	}

	private void handleMailTextFieldChange(String mail) {

		userNameTextField.setValue(mail);

		if (EmailValidator.getInstance().isValid(mail)) {
			if (securityService.userExists(mail) && !registeredUser.getMail().equals(mail)) {
				mailTextField.setComponentError(new UserError(i18n.get(UIMessageKeys.USER_SETTINGS_WINDOW_EMAIL_ALREADY_REGISTERED)));
				setCanFinish(false);
			} else {
				mailTextField.setComponentError(null);
				setCanFinish(true);
			}
		}
		else {
			mailTextField.setComponentError(new UserError(i18n.get(UIMessageKeys.USER_SETTINGS_WINDOW_EMAIL_INVALID)));
			setCanFinish(false);
		}
	}

	public boolean sendMail() {
		return sendMailCheckbox.getValue();
	}

	public RegisteredUser getUser() {
		return registeredUser;
	}

	public void clear() {
		userNameTextField.setEnabled(false);
		mailTextField.focus();
		mailTextField.setComponentError(null);
	}
}
