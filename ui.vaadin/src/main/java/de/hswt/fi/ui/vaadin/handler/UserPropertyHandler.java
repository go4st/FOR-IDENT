package de.hswt.fi.ui.vaadin.handler;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.VaadinSessionScope;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.UserPropertyPayload;
import de.hswt.fi.userproperties.service.api.UserPropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.SessionEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

@SpringComponent
@VaadinSessionScope
public class UserPropertyHandler extends AbstractHandler<SessionEventBus> {

	private static final long serialVersionUID = 2793069115674901236L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserPropertyHandler.class);

	private final UserPropertiesService userPropertiesService;

	@Autowired
	public UserPropertyHandler(UserPropertiesService userPropertiesService) {
		this.userPropertiesService = userPropertiesService;
	}

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_USER_PROPERTIES_BOOLEAN)
	protected void handleBooleanValuePayload(UserPropertyPayload<Boolean> payload) {
		LOGGER.debug("entering event bus listener handleBooleanValuePayload with payload {}", payload);
		userPropertiesService.setBooleanValue(payload.getKey(), payload.getValue());

	}

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_USER_PROPERTIES_STRING)
	protected void handlStringValuePayload(UserPropertyPayload<String> payload) {
		LOGGER.debug("entering event bus listener handlStringValuePayload with payload {}", payload);
		userPropertiesService.setStringValue(payload.getKey(), payload.getValue());
	}

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_USER_PROPERTIES_DOUBLE)
	protected void handlDoubleValuePayload(UserPropertyPayload<Double> payload) {
		LOGGER.debug("entering event bus listener handlDoubleValuePayload with payload {}", payload);
		userPropertiesService.setDoubleValue(payload.getKey(), payload.getValue());
	}

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_USER_PROPERTIES_INTEGER)
	protected void handlIntegerValuePayload(UserPropertyPayload<Integer> payload) {
		LOGGER.debug("entering event bus listener handlIntegerValuePayload with payload {}", payload);
		userPropertiesService.setIntegerValue(payload.getKey(), payload.getValue());
	}

}
