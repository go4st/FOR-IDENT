package de.hswt.fi.ui.vaadin.windows;

import com.vaadin.data.HasValue;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
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
public class ResetPasswordWindow extends AbstractWindow {

	private static final long serialVersionUID = -2153612655404127892L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ResetPasswordWindow.class);

	private String email;

	private CssLayout contentLayout;

	private TextField emailTextField;

	@Autowired
	protected ResetPasswordWindow(ComponentFactory componentFactory, I18N i18n) {
		super(componentFactory,i18n, false);
		setWidth(LayoutConstants.LARGE);
	}

	@Override
	protected String getWindowCaption() {

		return i18n.get(UIMessageKeys.RESET_PASSWORD_WINDOW_WINDOW_CAPTION);

	}

	@Override
	protected Component getContentComponent() {

		initContentLayout();

		initDescriptionLabel();

		initEmailTextField();

		setCanFinish(false);

		return contentLayout;

	}

	private void initContentLayout() {
		contentLayout = new CssLayout();
		contentLayout.setSizeFull();
	}

	private void initDescriptionLabel() {
		Label descriptionLabel = new Label(
				i18n.get(UIMessageKeys.RESET_PASSWORD_WINDOW_DESCRIPTION_LABEL_CAPTION));
		descriptionLabel.addStyleName(CustomValoTheme.PADDING_BOTTOM);
		contentLayout.addComponent(componentFactory.createRowLayout(descriptionLabel));
	}

	private void initEmailTextField() {
		emailTextField = componentFactory.createTextField(i18n.get(UIMessageKeys.USER_EMAIL));
		emailTextField.addValueChangeListener(this::handleTextChangedEvent);
		emailTextField.focus();

		CssLayout rowLayout = componentFactory.createRowLayout(emailTextField);
		rowLayout.addStyleName(CustomValoTheme.PADDING_TOP);

		contentLayout.addComponent(rowLayout);
	}

	private void handleTextChangedEvent(HasValue.ValueChangeEvent<String> event) {

		EmailValidator emailValidator = EmailValidator.getInstance();
		if (emailValidator.isValid(event.getValue())) {
			setCanFinish(emailValidator.isValid(event.getValue()));
			email = event.getValue();
		}
	}

	@Override
	protected void handleOk() {
		LOGGER.debug("inside method handleOk with {}", email);
		email = emailTextField.getValue();
	}

	public String getEmail() {
		return email;
	}

	public void clear() {
		email = "";
		emailTextField.clear();
	}

}
