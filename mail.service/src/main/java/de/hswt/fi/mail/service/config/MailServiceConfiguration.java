package de.hswt.fi.mail.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@EnableConfigurationProperties(value = MailProperties.class)
public class MailServiceConfiguration {

	@Autowired
	private MailProperties mailProperties;

	@Bean
	public JavaMailSender javaMailService() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(mailProperties.getHost());
		javaMailSender.setPort(mailProperties.getPort());
		javaMailSender.setDefaultEncoding("UTF-8");
		return javaMailSender;
	}

}
