package de.hswt.fi.security.service.vaadin.configuration;

import de.hswt.fi.common.spring.Profiles;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This class configures the spring security behaviour for
 * integration tests, enabling url path to h2 in memory database console.
 *
 * @author August Gilg
 */

@Profile(value = {Profiles.TEST})
@Configuration
public class TestConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/h2_console/**").permitAll()
                .antMatchers("/api/**").anonymous();

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        auth.inMemoryAuthentication()
                .passwordEncoder(encoder)
                .withUser("admin")
                .password(encoder.encode("123456"))
                .roles("ADMIN", "USER");
    }

}



