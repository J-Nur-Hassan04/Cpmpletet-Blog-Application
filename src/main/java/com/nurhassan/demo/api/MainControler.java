package com.nurhassan.demo.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nurhassan.demo.entities.Post;
import com.nurhassan.demo.entities.User;
import com.nurhassan.demo.service.PostService;
import com.nurhassan.demo.service.UserService;

@RestController
@RequestMapping("/api")
public class MainControler {

	@Autowired
	PostService postService;
	
	@Autowired
	UserService userService;
	
	@GetMapping
	public ResponseEntity<List<Post>> getAllPosts()
	{
		return new ResponseEntity<List<Post>>(postService.getAllPosts(),HttpStatus.OK);
	}
	
	@GetMapping("/admin/user-data")
	public ResponseEntity<List<User>> getUser()
	{
		return new ResponseEntity<List<User>>(userService.getAllUserData(),HttpStatus.OK);
	}
	
	@PostMapping(value = {"/admin/add-user"})
	public ResponseEntity<User> addUser(@RequestBody User user)
	{
		User newUser = new User();
		newUser.setName(user.getName());
		newUser.setEmail(user.getEmail());
		newUser.setPassword(user.getPassword());
		newUser.setRole(user.getRole());
		
		userService.saveOrUpdateUserDetails(newUser);
		
		return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/admin/user-data/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable("id") int  userId)
	{
		userService.deleteUserBYId(userId);
		return new ResponseEntity<User>(HttpStatus.ACCEPTED);
	}
	
	
	@GetMapping("/admin/user-data/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") int userId)
	{
		return new ResponseEntity<User>(userService.getUserById(userId),HttpStatus.OK);
	}
	
	@PutMapping("/admin/user-data/{id}")
	public ResponseEntity<User> updateUserDetails(@PathVariable("id")int usrId, @RequestBody User newDetails)
	{
		
		User user = userService.getUserById(usrId);
		user.setName(newDetails.getName());
		user.setEmail(newDetails.getEmail());
		user.setRole(newDetails.getRole());
		
		userService.saveOrUpdateUserDetails(user);
		
		return new ResponseEntity<User>(HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/user/post/{id}")
	public Post getPostById(@PathVariable("id") int postId)
	{
		return postService.getPostById(postId);
	}
	
	@GetMapping("/user/allposts/")
	public ResponseEntity<Page<Post>> getPostsPagination(@RequestParam("pagenumber") int pageNumber, @RequestParam("limit") int limit)
	{
		return new ResponseEntity<Page<Post>>(postService.getAllPostsOfPage(pageNumber, limit),HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
