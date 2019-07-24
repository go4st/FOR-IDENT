package de.hswt.fi.userproperties.service.def;

import de.hswt.fi.security.service.api.SecurityService;
import de.hswt.fi.security.service.model.RegisteredUser;
import de.hswt.fi.userproperties.service.api.UserPropertiesService;
import de.hswt.fi.userproperties.service.api.repository.UserPropertiesRepository;
import de.hswt.fi.userproperties.service.model.UserProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Optional;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

@Component
@SessionScope
@SuppressWarnings("unused")
public class DefaultUserPropertiesService implements UserPropertiesService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DefaultUserPropertiesService.class);

	private final SecurityService securityService;

	private UserPropertiesRepository userPropertiesRepository;

	private UserProperties userProperties;

	@Autowired
	public DefaultUserPropertiesService(UserPropertiesRepository userPropertiesRepository, SecurityService securityService) {
		this.userPropertiesRepository = userPropertiesRepository;
		this.securityService = securityService;
	}

	@Override
	public void loadPropertiesForUser(RegisteredUser user) {

		LOGGER.debug("Looking for properties for user: {}", user.getUsername());
		Optional<UserProperties> optionalUserProperties = userPropertiesRepository.findByUserID(user.getId());
		if (optionalUserProperties.isPresent()) {
			LOGGER.debug("Found properties for user: {}", user.getUsername());
			userProperties = optionalUserProperties.get();
			return;
		}
		LOGGER.debug("Found no properties for user: {}", user.getUsername());
		UserProperties userProperties = new UserProperties(user.getId(), new Properties());
		userProperties = userPropertiesRepository.save(userProperties);
		this.userProperties = userProperties;
	}

	@Override
	public void setStringValue(String key, String value) {

		if (securityService.getCurrentUser() == null) {
			return;
		}

		validateStringParameter(key, value);
		setAndUpdate(key, value);

	}

	@Override
	public void setDoubleValue(String key, Double value) {

		if (securityService.getCurrentUser() == null) {
			return;
		}

		validateParameter(key, value);
		setAndUpdate(key, value);

	}

	@Override
	public void setIntegerValue(String key, Integer value) {

		if (securityService.getCurrentUser() == null) {
			return;
		}

		validateParameter(key, value);
		setAndUpdate(key, value);

	}

	@Override
	public void setBooleanValue(String key, Boolean value) {

		if (securityService.getCurrentUser() == null) {
			return;
		}

		validateParameter(key, value);
		setAndUpdate(key, value);
	}

	@Override
	public String getStringValueOrDefault(String key, String defaultValue) {

		if (userProperties == null ||userProperties.getProperties().isEmpty()) {
			return defaultValue;
		}

		validateParameter(key, defaultValue);

		if (!userProperties.getProperties().containsKey(key)) {
			LOGGER.debug("Key {} is not present. Add with value {}", key, defaultValue);
			setStringValue(key, String.valueOf(defaultValue));
			return userProperties.getProperties().getProperty(key);
		}
		LOGGER.debug("Key {} is present. return value {}", key,
				userProperties.getProperties().getProperty(key));
		return userProperties.getProperties().getProperty(key);

	}

	@Override
	public Double getDoubleValueOrDefault(String key, Double defaultValue) {

		if (userProperties == null ||userProperties.getProperties().isEmpty()) {
			return defaultValue;
		}

		validateParameter(key, defaultValue);

		if (!userProperties.getProperties().containsKey(key)) {
			LOGGER.debug("Key {} is not present. Add with value {}", key, defaultValue);
			setStringValue(key, String.valueOf(defaultValue));
			return Double.valueOf(userProperties.getProperties().getProperty(key));
		}
		LOGGER.debug("Key {} is present. return value {}", key,
				userProperties.getProperties().getProperty(key));
		return Double.valueOf(userProperties.getProperties().getProperty(key));
	}

	@Override
	public Integer getIntegerValueOrDefault(String key, Integer defaultValue) {

		if (userProperties == null ||userProperties.getProperties().isEmpty()) {
			return defaultValue;
		}

		validateParameter(key, defaultValue);

		if (!userProperties.getProperties().containsKey(key)) {
			LOGGER.debug("Key {} is not present. Add with value {}", key, defaultValue);
			setStringValue(key, String.valueOf(defaultValue));
			return Integer.valueOf(userProperties.getProperties().getProperty(key));
		}
		LOGGER.debug("Key {} is present. return value {}", key,
				userProperties.getProperties().getProperty(key));
		return Integer.valueOf(userProperties.getProperties().getProperty(key));
	}

	@Override
	public Boolean getBooleanValueOrDefault(String key, Boolean defaultValue) {

		if (userProperties == null ||userProperties.getProperties().isEmpty()) {
			return defaultValue;
		}

		validateParameter(key, defaultValue);

		if (!userProperties.getProperties().containsKey(key)) {
			LOGGER.debug("Key {} is not present. Add with value {}", key, defaultValue);
			setStringValue(key, String.valueOf(defaultValue));
			return Boolean.valueOf(userProperties.getProperties().getProperty(key));
		}
		LOGGER.debug("Key {} is present. return value {}", key,
				userProperties.getProperties().getProperty(key));
		return Boolean.valueOf(userProperties.getProperties().getProperty(key));
	}

	private <T> void validateParameter(String key, T defaultValue) {
		checkArgument(!isNullOrEmpty(key), "Key must not be null or empty");
		checkNotNull(defaultValue, "Value must not be null or empty");
	}

	private void validateStringParameter(String key, String value) {
		checkArgument(!isNullOrEmpty(key), "Key must not be null or empty");
		checkArgument(!isNullOrEmpty(value), "Value must not be null or empty");
	}

	private <T> void setAndUpdate(String key, T value) {

		if (value instanceof String) {
			userProperties.getProperties().setProperty(key, (String) value);
		}

		else {
			userProperties.getProperties().setProperty(key, String.valueOf(value));
		}
		userProperties = userPropertiesRepository.save(userProperties);
	}

}
