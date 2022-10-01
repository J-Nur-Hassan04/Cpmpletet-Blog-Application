package com.nurhassan.demo.service;

import com.nurhassan.demo.entities.User;

public interface UserService {

	public void saveUserDetails(User user);
	
	public User getUserByEmail(String email);
	
	public User getUserById(int id);
}
