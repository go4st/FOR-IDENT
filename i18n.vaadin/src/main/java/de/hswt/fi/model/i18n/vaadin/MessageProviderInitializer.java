package de.hswt.fi.model.i18n.vaadin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;
import org.vaadin.spring.i18n.ResourceBundleMessageProvider;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class MessageProviderInitializer implements BeanDefinitionRegistryPostProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageProviderInitializer.class);

	private static final String RESOURCE_SUFFIX = "/build/resources/main";

	private static final String I18N_PACKAGE_ROOT = "/de/hswt/fi/";

	private static final String I18N_DIRECTORY = "i18n";

	private static final String I18N_MESSAGES_PREFIX = "messages";

	private static final String I18N_PATTERN = "classpath*:" + I18N_PACKAGE_ROOT + "**/" + I18N_DIRECTORY + "/" + I18N_MESSAGES_PREFIX + "*";

	private ResourceLoader resourceLoader;

	@Autowired
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
			throws BeansException {

		Set<String> i18nResources = new HashSet<>();

		try {
			Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(I18N_PATTERN);
			for (Resource resource : resources) {
				String path = resource.getURL().toString();
				i18nResources.add(path.substring(path.lastIndexOf(I18N_PACKAGE_ROOT) + 1, path.lastIndexOf("/" + I18N_MESSAGES_PREFIX)));
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}

		i18nResources.forEach(r -> beanFactory.registerSingleton(r.replace("/", "."),
				new ResourceBundleMessageProvider(r.replace("/", ".") + "/" + I18N_MESSAGES_PREFIX)));
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
			throws BeansException {
	}
}
