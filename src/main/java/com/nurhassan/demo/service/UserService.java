package com.nurhassan.demo.service;

import java.util.List;

import com.nurhassan.demo.entities.User;

public interface UserService {
	
	public List<User> getAllUserData();

	public void saveUserDetails(User user);
	
	public User getUserByEmail(String email);
	
	public User getUserById(int id);
}
