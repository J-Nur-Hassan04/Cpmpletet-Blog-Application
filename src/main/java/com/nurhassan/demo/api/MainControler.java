package com.nurhassan.demo.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.nurhassan.demo.entities.Tag;
import com.nurhassan.demo.entities.User;
import com.nurhassan.demo.security.UserPrincipal;
import com.nurhassan.demo.service.PostService;
import com.nurhassan.demo.service.TagService;
import com.nurhassan.demo.service.UserService;

@RestController
@RequestMapping("/api")
public class MainControler {

	@Autowired
	private PostService postService;

	@Autowired
	private UserService userService;
	
	@Autowired
	TagService tagService;

	@GetMapping
	public ResponseEntity<List<Post>> getAllPosts() {
		return new ResponseEntity<List<Post>>(postService.getAllPosts(), HttpStatus.OK);
	}

	@PostMapping(value = { "/admin/add-user" })
	public ResponseEntity<User> addUser(@RequestBody User user) {
		User newUser = new User();
		newUser.setName(user.getName());
		newUser.setEmail(user.getEmail());
		newUser.setPassword(user.getPassword());
		newUser.setRole(user.getRole());

		userService.saveOrUpdateUserDetails(newUser);

		return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
	}

	@GetMapping("/admin/users-data")
	public ResponseEntity<List<User>> getAllUser() {
		return new ResponseEntity<List<User>>(userService.getAllUserData(), HttpStatus.OK);
	}

	@GetMapping("/admin/user-data/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") int userId) {
		return new ResponseEntity<User>(userService.getUserById(userId), HttpStatus.OK);
	}

	@PutMapping("/admin/user-data/{id}")
	public ResponseEntity<User> updateUserDetails(@PathVariable("id") int usrId, @RequestBody User newDetails) {

		User user = userService.getUserById(usrId);
		user.setName(newDetails.getName());
		user.setEmail(newDetails.getEmail());
		user.setRole(newDetails.getRole());
		user.setPassword(newDetails.getPassword());

		userService.saveOrUpdateUserDetails(user);

		return new ResponseEntity<User>(HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/admin/user-data/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable("id") int userId) {
		userService.deleteUserBYId(userId);
		return new ResponseEntity<User>(HttpStatus.ACCEPTED);
	}


	@GetMapping("/allposts/")
	public ResponseEntity<Page<Post>> getPostsPagination(@RequestParam("pagenumber") int pageNumber,
			@RequestParam("limit") int limit) {
		return new ResponseEntity<Page<Post>>(postService.getAllPostsOfPage(pageNumber, limit), HttpStatus.OK);
	}
	@GetMapping("/post/{id}")
	public Post getPostById(@PathVariable("id") int postId) {
		return postService.getPostById(postId);
	}
	@PutMapping("/post/{id}")
	public ResponseEntity<Post> updatePostById( @RequestBody Post updatedPost, @PathVariable("id")int postId)
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		Post post = postService.getPostById(postId);
		
		if((userPrincipal.getId() == post.getUser().getId()) || userPrincipal.getRole().equals("ADMIN"))
		{
			post.setTitle(updatedPost.getTitle());
			post.setContent(updatedPost.getContent());
			post.setAuthor(updatedPost.getAuthor());
			post.setUpdatedAt(new Date());
			int cutUpto = (updatedPost.getContent().length() > 250) ? 250 : updatedPost.getContent().length();
			post.setExcerpt(post.getContent().substring(0, cutUpto) + "...");
			post.setPublished(updatedPost.isPublished());
			
			
			List<Tag> tags = new ArrayList<>();
			for(Tag tag : updatedPost.getTags())
			{
				Tag t = tagService.getTagByName(tag.getName());
				if(t == null)
				{
					Tag t1 = new Tag();
					t1.setName(tag.getName());
					t1.setCreatedAt(new Date());
					t1.setUpdatedAt(new Date());
					t1.getPost().add(post);
					tags.add(t1);
				}
				else
				{
					tags.add(t);
				}
				
			}
			post.setTags(tags);			
			postService.savePost(post);
			
			return new ResponseEntity<Post>(post, HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<Post>(post,HttpStatus.FORBIDDEN);
	}
	


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
