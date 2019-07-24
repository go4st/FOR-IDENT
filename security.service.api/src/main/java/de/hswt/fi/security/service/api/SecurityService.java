package de.hswt.fi.security.service.api;

import de.hswt.fi.security.service.model.Database;
import de.hswt.fi.security.service.model.Group;
import de.hswt.fi.security.service.model.RegisteredUser;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;


/**
 * The Interface SISecurityService defines methods to handle user CRUD
 * operations, login and logout
 *
 * Users have roles. The roles are assigned to the users with groups. If a user
 * is in a group, say A, he gets all roles assigned which are part of the group
 * A.
 *
 * @author Marco Luthardt
 */
public interface SecurityService {

	/** The Constant ROLE_ANONYMOUS. */
	String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

	/**
	 * Returns the authenticated user of the current session.
	 *
	 * @return the user of the current session
	 */
	@PreAuthorize("isAnonymous() OR isAuthenticated()")
	RegisteredUser getCurrentUser();

	/**
	 * Checks if the user of the current session has the given role.
	 *
	 * @param role
	 *            the role
	 * @return true, if user has the role assigned, otherwise false
	 */
	boolean currentUserHasRole(String role);

	/**
	 * Checks if the user of the current session has all given roles.
	 *
	 * @param role
	 *            the roles
	 * @return true, if the user has all roles assigned, otherwise false
	 */
	boolean currentUserHasRole(List<String> role);

	/**
	 * Creates a new user with user name and password, is only allowed to user
	 * how has the role admin.
	 *
	 * @param user
	 *            the user to create
	 * @return the user object of the new created user, updated with the id in
	 *         the DB
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasPermission(#user, 'create')")
	RegisteredUser createUser(RegisteredUser user);

	/**
	 * Creates a new disabled user without a password.
	 *
	 * @param user
	 *            the user to create
	 * @return true, if the user creation was successful, otherwise false
	 */
	boolean requestUserAccount(RegisteredUser user);

	/**
	 * Deletes the given user. It is only allowed to user with the role admin.
	 *
	 * @param user
	 *            the user to delete
	 * @return true, if successful, otherwise false
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	boolean deleteUser(RegisteredUser user);


	/**
	 * Resets the current password of the user with the given mail address.
	 *
	 * @param mail
	 *            the mail to reset password for
	 * @param newPassword
	 *            the new password to set
	 * @return true, if the password was set, otherwise false
	 */
	@PreAuthorize("isAnonymous() OR isAuthenticated()")
	boolean resetPasswordTo(String mail, String newPassword);

	/**
	 * Returns a list with all available groups.
	 *
	 * @return the list of available groups
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	List<Group> findAllGroups();

	/**
	 * Returns a list with all available users.
	 *
	 * It is only usable for users with role admin.
	 *
	 * @return the list of all users
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	List<RegisteredUser> findAllUsers();

	/**
	 * Updates the user data in the DB, with the given user data. It is only
	 * allowed to users with the role admin or the current user if he want to
	 * change his password himself.
	 *
	 * @param user
	 *            the user data to update
	 * @return the updated user object
	 */
	@PreAuthorize("(isFullyAuthenticated() AND #user == principal.User) OR hasRole('ROLE_ADMIN')")
	RegisteredUser updateUser(RegisteredUser user);

	/**
	 * Checks if a user with the given user name exists.
	 *
	 * @param username
	 *            the user name to look for
	 * @return true, if the user name already exists, otherwise false
	 */
	@PreAuthorize("isAnonymous() OR isAuthenticated()")
	boolean userExists(String username);

	/**
	 * Checks if a user with the given user name exists.
	 *
	 * @param mail
	 *            the mail to look for
	 * @return true, if the user mail exists, otherwise false
	 */
	@PreAuthorize("isAnonymous() OR isAuthenticated()")
	boolean mailExists(String mail);

	/**
	 * Checks if a user with the given user id exists.
	 *
	 * @param id
	 *            the id to look for
	 * @return true, if the user id exists, otherwise false
	 */
	@PreAuthorize("isAuthenticated()")
	boolean userExistsById(Long id);

	void hashPassword(RegisteredUser user);

	List<Database> findAllDatabases();

    Group createGroup(Group group);
}
