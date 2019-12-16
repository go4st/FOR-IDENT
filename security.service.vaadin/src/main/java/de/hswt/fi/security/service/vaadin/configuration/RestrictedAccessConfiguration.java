package de.hswt.fi.security.service.vaadin.configuration;

import de.hswt.fi.application.properties.ApplicationProperties;
import de.hswt.fi.common.spring.Profiles;
import de.hswt.fi.security.service.vaadin.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.vaadin.spring.security.annotation.EnableVaadinSharedSecurity;
import org.vaadin.spring.security.shared.VaadinSessionClosingLogoutHandler;

/**
 * This class configures the spring security behaviour.
 * This configuration can be enabled to ensure the following
 * - The Search, Quicksearch and IndexSearch functionality is available for anonymous
 * - The Processing functionality is only accessible for registered and logged in user
 * - IFrame is disabled using the SAME-ORIGIN Header
 *
 * @author Tobias Placht
 */

@Profile({Profiles.LC, Profiles.DEVELOPMENT_LC, Profiles.GC, Profiles.DEVELOPMENT_GC})
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
@EnableVaadinSharedSecurity
public class RestrictedAccessConfiguration extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;
    private final ApplicationProperties applicationProperties;

    @Autowired
    public RestrictedAccessConfiguration(CustomUserDetailsService customUserDetailsService, ApplicationProperties applicationProperties) {
        this.customUserDetailsService = customUserDetailsService;
        this.applicationProperties = applicationProperties;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.authorizeRequests().antMatchers("/login/**").anonymous().antMatchers("/api/**").anonymous().antMatchers("/vaadinServlet/UIDL/**").permitAll().antMatchers("/vaadinServlet/HEARTBEAT/**").permitAll();
        http.httpBasic().disable().formLogin().disable();
        http.logout().addLogoutHandler(new VaadinSessionClosingLogoutHandler()).logoutUrl(applicationProperties.getSecurity().getLogoutUrl()).logoutSuccessUrl(applicationProperties.getSecurity().getRedirectUrl()).permitAll();
        http.headers().frameOptions().sameOrigin();
        http.cors().disable();
        http.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));

        // Instruct Spring Security to use the same authentication strategy as Vaadin4Spring
        http.sessionManagement().sessionAuthenticationStrategy(sessionAuthenticationStrategy());
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/VAADIN/**");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * The {@link SessionAuthenticationStrategy} must be available as a Spring bean for Vaadin4Spring.
     */
    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new SessionFixationProtectionStrategy();
    }
}
