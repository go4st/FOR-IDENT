package de.hswt.fi.ui.vaadin.handler.security;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.ui.vaadin.handler.ConfirmDeleteAndUploadCompoundDatabaseHandler;
import de.hswt.fi.ui.vaadin.handler.RefreshTPDatabaseWindowHandler;
import de.hswt.fi.ui.vaadin.handler.UploadCompoundDatabaseWindowHandler;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@ViewScope
@SuppressWarnings("unused")
public class UserAdministrationViewHandler {

	@Autowired
	private AddUserHandler addUserHandler;

	@Autowired
	private EditUserHandler editUserHandler;

	@Autowired
	private ChangePasswordHandler changePasswordHandler;

	@Autowired
	private DeleteUserHandler deleteUserHandler;

	@Autowired
	private RefreshTPDatabaseWindowHandler refreshTPDatabaseWindowHandler;

	@Autowired
	private UploadCompoundDatabaseWindowHandler uploadCompoundDatabaseWindowHandler;

	@Autowired
	private ConfirmDeleteAndUploadCompoundDatabaseHandler confirmDeleteAndUploadCompoundDatabaseHandler;
}
