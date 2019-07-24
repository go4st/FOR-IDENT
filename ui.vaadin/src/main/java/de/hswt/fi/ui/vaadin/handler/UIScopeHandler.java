package de.hswt.fi.ui.vaadin.handler;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import de.hswt.fi.ui.vaadin.handler.security.EditUserSettingsHandler;
import de.hswt.fi.ui.vaadin.handler.security.LoginHandler;
import de.hswt.fi.ui.vaadin.handler.security.RequestUserAccountHandler;
import de.hswt.fi.ui.vaadin.handler.security.ResetPasswordHandler;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
@SuppressWarnings("unused")
public class UIScopeHandler {

	@Autowired
	private ResetPasswordHandler resetPasswordHandler;

	@Autowired
	private RequestUserAccountHandler requestUserAccountHandler;

	@Autowired
	private EditUserSettingsHandler editUserSettingsHandler;

	@Autowired
	private LoginHandler loginHandler;

}
