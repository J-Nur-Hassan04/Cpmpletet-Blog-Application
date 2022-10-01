package com.nurhassan.demo.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nurhassan.demo.entities.Post;
import com.nurhassan.demo.entities.User;
import com.nurhassan.demo.service.PostService;
import com.nurhassan.demo.service.UserService;

@RestController
public class MainControler {

	@Autowired
	PostService postService;
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value = {"/api"}, method = RequestMethod.GET)
	public List<Post> getAllPosts()
	{
		return postService.getAllPosts();
	}
	
	@RequestMapping(value = {"/api/admin/user-data"}, method = RequestMethod.GET)
	public List<User> getUser()
	{
		return userService.getAllUserData();
	}
	
	@RequestMapping(value = {"/api/admin/user-data/{id}"}, method = RequestMethod.GET)
	public User getUserById(@PathVariable("id") int id)
	{
		return userService.getUserById(id);
	}
	
	@RequestMapping(value = {"/api/user/post/{id}"}, method = RequestMethod.GET)
	public Post getPostById(@PathVariable("id") int id)
	{
		return postService.getPostById(id);
	}
	
	@RequestMapping(value = {"/api/user/allposts/"}, method = RequestMethod.GET)
	public Page<Post> getPostsPagination(@RequestParam("pagenumber") int n, @RequestParam("limit") int l)
	{
		System.out.println();
		return postService.getAllPostsOfPage(n, l);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
