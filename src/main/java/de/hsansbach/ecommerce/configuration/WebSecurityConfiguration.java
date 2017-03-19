package de.hsansbach.ecommerce.configuration;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.headers()
				.frameOptions().disable() // Required for H2 console
				.and()
			.csrf()
				.disable() // CSRF protection is not supported by Camunda Webapps
			.formLogin()
				.loginPage("/login.jsf")
				.loginProcessingUrl("/login")
				.failureUrl("/login.jsf?error=true")
				.successForwardUrl("/home.jsf")
				.and()
			.logout()
				.logoutSuccessUrl("/login.jsf?logout=true")
				.and()
			.authorizeRequests()
				.antMatchers("/login.jsf").permitAll()
				.antMatchers("/app/**", "/lib/**", "/api/**").permitAll() // Required for Camunda Webapps
				.antMatchers("/h2-console/**").permitAll() // Required for H2 console
				.antMatchers("/javax.faces.resource/**", "/img/**", "/css/**", "/js/**", "/fonts/**").permitAll() // Static resources
				.anyRequest().authenticated();
		// @formatter:on
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// @formatter:off
		auth
			.inMemoryAuthentication()
				.withUser("admin").password("admin").roles("ADMIN", "USER")
				.and()
				.withUser("kermit").password("kermit").roles("USER")
				.and()
				.withUser("gonzo").password("gonzo").roles("USER");
		// @formatter:on
	}

}
