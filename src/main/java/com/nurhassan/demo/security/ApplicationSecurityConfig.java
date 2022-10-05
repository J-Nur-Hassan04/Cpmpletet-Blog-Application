package com.nurhassan.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService userDetailsService;
	//UserDetailsService is a interface
	
	@Bean
	public AuthenticationProvider authProvider()
	{
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		
		
		return provider;
	}
		
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
				.antMatchers("/api/admin/**").hasAuthority("ADMIN")
				.antMatchers("/api/add-post").hasAnyAuthority("ADMIN","AUTHOR")
				.antMatchers("/posts/{id}/update").hasAnyAuthority("AUTHOR","ADMIN")
				.antMatchers("/", "/singup/**","/posts/**","/api", "/api/post/*")
				.permitAll()
				.anyRequest()
				.authenticated()
				.and()
				.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
				.logout()
				.permitAll()
				.and()
				.httpBasic();
	}

}
