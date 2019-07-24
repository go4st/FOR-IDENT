package de.hswt.fi.ui.vaadin.windows;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import de.hswt.fi.security.service.model.RegisteredUser;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.i18n.I18N;

@SpringComponent
@PrototypeScope
public class ReportWindow extends AbstractWindow {

	private static final long serialVersionUID = 7475512427202338814L;

	private RegisteredUser user;

	private Binder<RegisteredUser> binder;

	private TextField mailTextField;

	private CssLayout mailTextFieldLayout;

	private TextField userNameTextField;

	private CssLayout userNameTextFieldLayout;

	private TextField organisationTextField;

	private CssLayout organisationTextFieldLayout;

	private TextArea bugDescriptionTextArea;

	private TextArea userDescriptionTextArea;

	private CheckBox anonymousCheckbox;

	private CssLayout anonymousDescriptionLayout;

	@Autowired
	protected ReportWindow(ComponentFactory componentFactory, I18N i18n) {
		super(componentFactory, i18n, false);
		setWidth(LayoutConstants.WINDOW_WIDTH_MEDIUM);
		binder = new Binder<>();
	}

	public void init(String bugDescription, RegisteredUser user) {
		bugDescriptionTextArea.setReadOnly(false);
		bugDescriptionTextArea.setValue(bugDescription);
		bugDescriptionTextArea.setReadOnly(true);

		this.user = user;

		if (user == null) {
			changeAnonymousState(true);
			return;
		}

		binder.bind(mailTextField, RegisteredUser::getMail, RegisteredUser::setMail);
		binder.bind(userNameTextField, RegisteredUser::getUsername, RegisteredUser::setUsername);
		binder.bind(organisationTextField, RegisteredUser::getOrganisation, RegisteredUser::setOrganisation);
		binder.readBean(user);
		setReadOnlyTextfields(true);

		userDescriptionTextArea.focus();
	}

	@Override
	protected String getWindowCaption() {
		return i18n.get(UIMessageKeys.WINDOW_BUG_REPORT_CAPTION);
	}

	@Override
	protected Component getContentComponent() {

		CssLayout contentLayout = new CssLayout();
		contentLayout.setSizeFull();
		contentLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_COLUMN);

		Label bugDescriptionTextAreaCaption = new Label(i18n.get(UIMessageKeys.WINDOW_BUG_REPORT_DESCRIPION));
		bugDescriptionTextAreaCaption.setWidth(LayoutConstants.HUGE);
		contentLayout.addComponent(componentFactory.createRowLayout(bugDescriptionTextAreaCaption));

		bugDescriptionTextArea = new TextArea();
		bugDescriptionTextArea.setWidth("100%");
		bugDescriptionTextArea.setReadOnly(true);
		contentLayout.addComponent(componentFactory.createRowLayout(bugDescriptionTextArea));

		contentLayout.addComponent(componentFactory.createSpacer());

		mailTextField = componentFactory.createTextField(i18n.get(UIMessageKeys.FROM));
		mailTextFieldLayout = componentFactory.createRowLayout(mailTextField);
		contentLayout.addComponent(mailTextFieldLayout);

		userNameTextField = componentFactory.createTextField(i18n.get(UIMessageKeys.USER_USERNAME));
		userNameTextFieldLayout = componentFactory.createRowLayout(userNameTextField);
		contentLayout.addComponent(userNameTextFieldLayout);

		organisationTextField = componentFactory.createTextField(i18n.get(UIMessageKeys.USER_ORGANISATION));
		organisationTextFieldLayout = componentFactory.createRowLayout(organisationTextField);
		contentLayout.addComponent(organisationTextFieldLayout);

		anonymousCheckbox = new CheckBox(i18n.get(UIMessageKeys.WINDOW_BUG_REPORT_ANONYMOUS_CHECKBOX_CAPTION));
		anonymousCheckbox.addValueChangeListener(c -> changeAnonymousState(anonymousCheckbox.getValue()));

		contentLayout.addComponent(componentFactory.createRowLayout(anonymousCheckbox));

		Label anonymousDescription = new Label(i18n.get(UIMessageKeys.WINDOW_BUG_REPORT_ANONYMOUS_DESCRIPTION));
		anonymousDescription.setWidth(LayoutConstants.HUGE);
		anonymousDescriptionLayout = componentFactory.createRowLayout(anonymousDescription);
		contentLayout.addComponent(anonymousDescriptionLayout);

		contentLayout.addComponent(componentFactory.createSpacer());

		Label userDescriptionTextAreaCaption = new Label(i18n.get(UIMessageKeys.WINDOW_BUG_REPORT_USERCOMMENT));
		userDescriptionTextAreaCaption.setWidth(LayoutConstants.HUGE);
		contentLayout.addComponent(componentFactory.createRowLayout(userDescriptionTextAreaCaption));

		userDescriptionTextArea = new TextArea();
		userDescriptionTextArea.setWidth("100%");
		contentLayout.addComponent(userDescriptionTextArea);

		contentLayout.addComponent(componentFactory.createRowLayout(userDescriptionTextArea));

		return contentLayout;
	}

	private void changeAnonymousState(boolean anonymous) {

		anonymousCheckbox.setValue(anonymous);
		anonymousDescriptionLayout.setVisible(anonymous);
		showTextFields(!anonymous);

		if (anonymous) {
			mailTextField.clear();
			userNameTextField.setValue("Anonymous");
			organisationTextField.clear();
			setReadOnlyTextfields(true);
		} else {
			if (user != null) {
				binder.readBean(user);
			} else {
				userNameTextField.clear();
				setReadOnlyTextfields(false);
			}
		}
	}

	public void showTextFields(boolean show) {
		mailTextFieldLayout.setVisible(show);
		userNameTextFieldLayout.setVisible(show);
		organisationTextFieldLayout.setVisible(show);
	}

	public void setReadOnlyTextfields(boolean readOnly) {
		mailTextField.setReadOnly(readOnly);
		userNameTextField.setReadOnly(readOnly);
		organisationTextField.setReadOnly(readOnly);
	}

	public void clear() {

		binder.setBean(null);

		setReadOnlyTextfields(false);
		setReadOnlyTextfields(true);

		bugDescriptionTextArea.clear();
		userDescriptionTextArea.clear();

		anonymousCheckbox.setValue(false);
		changeAnonymousState(false);
	}

	@Override
	protected void handleOk() {
		// Nothing to do here
	}

	public String getUserMail() {
		return mailTextField.getValue();
	}

	public String getUserName() {
		return userNameTextField.getValue();
	}

	public String getUserOrganization() {
		return organisationTextField.getValue();
	}

	public String getUserComment() {
		return userDescriptionTextArea.getValue();
	}

	public String getBugDescription() {
		return bugDescriptionTextArea.getValue();
	}
}