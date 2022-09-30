package com.nurhassan.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nurhassan.demo.entities.User;
import com.nurhassan.demo.repository.UserRepository;

 @Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User u = userRepository.findByEmail(username);
		
		if(u == null)
			throw new UsernameNotFoundException("user not found");
		
		
		return new UserPrincipal_(u);
	}

}
