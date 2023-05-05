package io.javabrains.bigreads;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityAdapter extends WebSecurityConfigurerAdapter {

    @Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.authorizeRequests(a -> a
				//.antMatchers("/", "/error").permitAll() //when only on / (beginning page) or error page, everyone is permitted, else you have to be authenticated 
				.anyRequest().permitAll() //any request is permitted- but what you see on the page depends on if you are authenticated or not 
			)
			.exceptionHandling(e -> e
				.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
			)
			.csrf(c -> c
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			)
			.logout(l -> l
				.logoutUrl("/logout")
				.logoutSuccessUrl("/").permitAll()
			)
			.oauth2Login()
			.defaultSuccessUrl("/home");  //user is getting authenticated by the login
		// @formatter:on
    }
    
}
