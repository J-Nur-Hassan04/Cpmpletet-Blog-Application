package com.nurhassan.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nurhassan.demo.entities.User;
import com.nurhassan.demo.repository.UserRepository;

@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public List<User> getAllUserData() {
		
		return userRepository.findAll();
	}
	@Override
	public void saveUserDetails(User user) {
		userRepository.save(user);
		
	}

	@Override
	public User getUserByEmail(String email) {
		User user = userRepository.findByEmail(email);
		return user;
	}

	@Override
	public User getUserById(int id) {
		User user = userRepository.findById(id).orElse(null);
		return user;
	}


}
