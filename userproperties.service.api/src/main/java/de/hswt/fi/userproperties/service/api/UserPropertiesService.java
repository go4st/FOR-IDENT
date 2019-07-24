package de.hswt.fi.userproperties.service.api;

import de.hswt.fi.security.service.model.RegisteredUser;

public interface UserPropertiesService {

	void loadPropertiesForUser(RegisteredUser user);

	void setStringValue(String key, String value);

	void setDoubleValue(String key, Double value);

	void setIntegerValue(String key, Integer value);

	void setBooleanValue(String key, Boolean value);

	String getStringValueOrDefault(String key, String defaultValue);

	Double getDoubleValueOrDefault(String key, Double defaultValue);

	Integer getIntegerValueOrDefault(String key, Integer defaultValue);

	Boolean getBooleanValueOrDefault(String key, Boolean defaultValue);

}
