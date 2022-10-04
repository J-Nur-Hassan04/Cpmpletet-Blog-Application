package com.nurhassan.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nurhassan.demo.entities.User;
import com.nurhassan.demo.service.UserService;

 @Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	UserService userService;
	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
		
		User user = userService.getUserByEmail(userEmail);
		
		if(user == null)
			throw new UsernameNotFoundException("user not found");
		
		
		return new UserPrincipal(user);
	}

}
