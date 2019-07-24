package de.hswt.fi.security.service.vaadin;

import de.hswt.fi.security.service.api.SpringUserWrapper;
import de.hswt.fi.security.service.api.repositories.RegisteredUserRepository;
import de.hswt.fi.security.service.model.Group;
import de.hswt.fi.security.service.model.RegisteredUser;
import de.hswt.fi.userproperties.service.api.UserPropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

	private final RegisteredUserRepository registeredUserRepository;

	private final UserPropertiesService userPropertiesService;

	@Autowired
	public CustomUserDetailsService(RegisteredUserRepository registeredUserRepository,
									UserPropertiesService userPropertiesService) {
		this.registeredUserRepository = registeredUserRepository;
		this.userPropertiesService = userPropertiesService;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) {

		Optional<RegisteredUser> optionalUser = registeredUserRepository.findByUsername(username);

		if (optionalUser.isPresent()) {

			RegisteredUser user = optionalUser.get();

			LOGGER.info("found user with username: {}", user.getUsername());
			Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
			for (String authority : getUserAuthoritiesFromGroups(user)) {
				grantedAuthorities.add(new SimpleGrantedAuthority(authority));
			}

			userPropertiesService.loadPropertiesForUser(user);

			return new SpringUserWrapper(user, grantedAuthorities);

		}
		LOGGER.info("found no user with username: {}", username);
		throw new UsernameNotFoundException("Username not found");
	}

	private List<String> getUserAuthoritiesFromGroups(RegisteredUser user) {
		if (user == null) {
			return new ArrayList<>();
		}

		Set<String> authorities = new HashSet<>();

		for (Group group : user.getGroups()) {
			authorities.addAll(group.getAuthorities());
		}

		return new ArrayList<>(authorities);
	}

}
