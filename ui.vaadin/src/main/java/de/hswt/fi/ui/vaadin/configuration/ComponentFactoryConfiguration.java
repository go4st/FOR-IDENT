package de.hswt.fi.ui.vaadin.configuration;

import com.vaadin.spring.annotation.VaadinSessionScope;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ComponentFactoryConfiguration {

	@Bean
	@VaadinSessionScope
	public ComponentFactory getComponentFactory() {
		return new ComponentFactory();
	}

}
