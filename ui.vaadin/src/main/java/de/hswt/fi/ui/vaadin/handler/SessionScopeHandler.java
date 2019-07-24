package de.hswt.fi.ui.vaadin.handler;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.VaadinSessionScope;
import de.hswt.fi.ui.vaadin.handler.security.LogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@VaadinSessionScope
@SuppressWarnings("unused")
public class SessionScopeHandler {

	@Autowired
	private ChangeLocaleHandler changeLocaleHandler;

	@Autowired
	private UserPropertyHandler userPropertyHandler;

	@Autowired
	private LogoutHandler logoutHandler;

}
