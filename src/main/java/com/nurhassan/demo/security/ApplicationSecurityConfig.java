package com.nurhassan.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
		.antMatchers("/").permitAll()
		.antMatchers("/singup/**").permitAll()
		.antMatchers("/posts/**").permitAll()
		.antMatchers("/posts/{id}/update").hasAnyAuthority("AUTHOR","ADMIN")
		.antMatchers("/api").permitAll()
		.antMatchers("/api/admin/add-user").hasAuthority("ADMIN")
		.antMatchers("/api/admin/user-data/*").hasAuthority("ADMIN")
		.antMatchers("/api/admin/users-data").hasAuthority("ADMIN")
		.antMatchers("/api/allposts/*").permitAll()
		.antMatchers(HttpMethod.GET,"/api/post/*").permitAll()
		.antMatchers("/api/post/*/comments").permitAll()
		.antMatchers(HttpMethod.GET,"/api/post/*/comment/*").permitAll()
		.antMatchers(HttpMethod.PUT,"/api/post/*").hasAnyAuthority("ADMIN","AUTHOR")
		.antMatchers(HttpMethod.POST,"/api/post/*").hasAnyAuthority("ADMIN","AUTHOR")
		.antMatchers(HttpMethod.DELETE,"/api/post/*").hasAnyAuthority("ADMIN","AUTHOR")
		.antMatchers("/api/post/*/comment/*").hasAnyAuthority("ADMIN","AUTHOR")
		.antMatchers("/api/post/*/add-comment").permitAll()
		.antMatchers("/post/{id}/tags").permitAll()
		.anyRequest().authenticated()
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
