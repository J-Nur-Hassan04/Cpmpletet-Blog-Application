package com.nurhassan.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nurhassan.demo.entities.User;
import com.nurhassan.demo.repository.UserRepository;

@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public void saveUserDetails(User user) {
		userRepository.save(user);
		
	}

}
