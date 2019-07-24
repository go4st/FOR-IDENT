package de.hswt.fi.ui.vaadin.configuration;

import de.hswt.fi.beans.BeanComponentMapper;
import de.hswt.fi.beans.BeanComponentRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.i18n.I18N;

@Configuration
public class BeanMappingConfiguration {

	@Bean
	@Autowired
	public BeanComponentMapper getBeanComponentMapper(I18N i18n, BeanComponentRegistry beanComponentRegistry) {
		return new BeanComponentMapper(i18n::get, i18n::getLocale, beanComponentRegistry);
	}
}
