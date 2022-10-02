package com.nurhassan.demo.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	public List<Post> getAllPosts()
	{
		return postService.getAllPosts();
	}
	
	@GetMapping("/admin/user-data")
	public List<User> getUser()
	{
		return userService.getAllUserData();
	}
	
	@PostMapping(value = {"/admin/add-user"})
	public ResponseEntity<User> addUser(@RequestBody User user)
	{

		User newUser = new User();
		newUser.setName(user.getName());
		newUser.setEmail(user.getEmail());
		newUser.setPassword(user.getPassword());
		newUser.setRole(user.getRole());
		
		userService.saveUserDetails(newUser);
		
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@GetMapping("/admin/user-data/{id}")
	public User getUserById(@PathVariable("id") int userId)
	{
		return userService.getUserById(userId);
	}
	
	@GetMapping("/user/post/{id}")
	public Post getPostById(@PathVariable("id") int postId)
	{
		return postService.getPostById(postId);
	}
	
	@GetMapping("/user/allposts/")
	public Page<Post> getPostsPagination(@RequestParam("pagenumber") int pageNumber, @RequestParam("limit") int limit)
	{
		return postService.getAllPostsOfPage(pageNumber, limit);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
