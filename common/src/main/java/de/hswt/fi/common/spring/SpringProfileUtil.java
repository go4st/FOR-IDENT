package de.hswt.fi.common.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;

@Component
public class SpringProfileUtil {

	private List<String> activeProfiles;

	@Autowired
	public SpringProfileUtil(Environment environment) {
		activeProfiles = Arrays.asList(environment.getActiveProfiles());
	}

	public boolean isProfileActive(String profile) {
		if (isNullOrEmpty(profile)) {
			throw new InvalidParameterException("Profile must not be null or empty");
		}
		return activeProfiles.contains(profile);
	}

	public boolean isDevelopmentProfile() {
		return activeProfiles.contains(Profiles.DEVELOPMENT_GC) || activeProfiles.contains(Profiles.DEVELOPMENT_LC);
	}

	public List<String> getActiveProfiles() {
		return activeProfiles;
	}

}
