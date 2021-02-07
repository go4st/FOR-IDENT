package de.hswt.fi.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.vaadin.spring.i18n.annotation.EnableI18N;
import org.vaadin.spring.sidebar.annotation.EnableSideBar;

@SpringBootApplication(scanBasePackages = Application.BASE_PACKAGE)
@EntityScan(basePackages = Application.BASE_PACKAGE)
@EnableSideBar
@EnableI18N
public class Application extends SpringBootServletInitializer {

	static final String BASE_PACKAGE = "de.hswt.fi";

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}