package de.hswt.fi.security.service.model;

/**
 * The Enum Role.add .
 */
public enum Role {

	/** The role user. */
	ROLE_USER,

	/** The role scientist. */
	ROLE_SCIENTIST,

	/** The role admin. */
	ROLE_ADMIN;

	public static String[] getAllRolesAsString() {
		return new String[] {ROLE_USER.name(), ROLE_SCIENTIST.name(), ROLE_ADMIN.name()};
	}}