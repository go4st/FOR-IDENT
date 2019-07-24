package de.hswt.fi.security.service.api;

import de.hswt.fi.security.service.model.RegisteredUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Objects;


/**
 * The Class SpringUserWrapper is a wrapper of the
 * {@link de.hswt.fi.security.service.model.RegisteredUser} class for the {@link User}
 * class of the spring security framework.
 * 
 * @author Marco Luthardt
 */
public class SpringUserWrapper extends User {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -567720931312907037L;

	/** The user. */
	private RegisteredUser user;

	/**
	 * Instantiates a new SI user spring.
	 *
	 * @param authorities
	 *            the authorities
	 */
	public SpringUserWrapper(RegisteredUser user, Collection<GrantedAuthority> authorities) {
		super(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true,
				authorities);
		this.user = user;
	}

	/**
	 * Sets the SIUser.
	 *
	 */
	public void setUser(RegisteredUser user) {
		Assert.notNull(user, "user must not be null");
		this.user = user;
	}

	/**
	 * Gets the SIUser.
	 *
	 * @return the SIUser
	 */
	public RegisteredUser getUser() {
		return user;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		SpringUserWrapper that = (SpringUserWrapper) o;
		return Objects.equals(user, that.user);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), user);
	}
}
