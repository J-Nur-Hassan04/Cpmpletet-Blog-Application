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

import com.nurhassan.demo.entities.Comment;
import com.nurhassan.demo.entities.Post;
import com.nurhassan.demo.entities.Tag;
import com.nurhassan.demo.entities.User;
import com.nurhassan.demo.security.UserPrincipal;
import com.nurhassan.demo.service.CommentService;
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
	private TagService tagService;

	@Autowired
	private CommentService commentService;

	@GetMapping
	public ResponseEntity<List<Post>> getAllPosts() {
		return new ResponseEntity<List<Post>>(postService.getAllPosts(), HttpStatus.FOUND);
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
		return new ResponseEntity<List<User>>(userService.getAllUserData(), HttpStatus.FOUND);
	}

	@GetMapping("/admin/user-data/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") int userId) {
		return new ResponseEntity<User>(userService.getUserById(userId), HttpStatus.FOUND);
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

	@GetMapping("/allposts")
	public ResponseEntity<Page<Post>> getPostsPagination(@RequestParam("pagenumber") int pageNumber,
			@RequestParam("limit") int limit) {
		return new ResponseEntity<Page<Post>>(postService.getAllPostsOfPage(pageNumber, limit), HttpStatus.FOUND);
	}

	@GetMapping("/add-post")
	public String getNewPostTemplate()
	{
		return "{\n"
				+ "    \"title\": \"\",\n"
				+ "    \"content\": \"\",\n"
				+ "    \"author\": \"\",\n"
				+ "    \"tags\": [\n"
				+ "        {\n"
				+ "            \"name\": \"\"\n"
				+ "        }\n"
				+ "    ],\n"
				+ "    \"published\": \n"
				+ "}";
	}
	
	@PostMapping("/add-post")
	public ResponseEntity<Post> addNewPost(@RequestBody Post post) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

		Post newPost = new Post();
		newPost.setTitle(post.getTitle());
		int cutUpto = (post.getContent().length() > 250) ? 250 : post.getContent().length();
		newPost.setExcerpt(post.getContent().substring(0, cutUpto) + "...");
		newPost.setContent(post.getContent());
		if (userPrincipal.getRole().equals("ADMIN")) {
			newPost.setAuthor(post.getAuthor());
		} else {
			newPost.setAuthor(userPrincipal.getName());
		}
		newPost.setPublishedAt(new Date());
		newPost.setCreatedAt(new Date());
		newPost.setUpdatedAt(new Date());
		newPost.setPublished(post.isPublished());

		List<Tag> tags = new ArrayList<>();
		for (Tag tag : post.getTags()) {
			Tag t = tagService.getTagByName(tag.getName());
			if (t == null) {
				Tag t1 = new Tag();
				t1.setName(tag.getName());
				t1.setCreatedAt(new Date());
				t1.setUpdatedAt(new Date());
				t1.getPost().add(newPost);
				tags.add(t1);
			} else {
				tags.add(t);
			}

		}

		newPost.setTags(tags);
		newPost.setUser(userService.getUserById(userPrincipal.getId()));

		return new ResponseEntity<Post>(postService.savePost(newPost), HttpStatus.CREATED);
	}

	@GetMapping("/post/{id}")
	public ResponseEntity<Post> getPostById(@PathVariable("id") int postId) {
		return new ResponseEntity<Post>(postService.getPostById(postId),HttpStatus.FOUND);
	}

	@PutMapping("/post/{id}")
	public ResponseEntity<Post> updatePostById(@RequestBody Post updatedPost, @PathVariable("id") int postId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		Post post = postService.getPostById(postId);

		if ((userPrincipal.getId() == post.getUser().getId()) || userPrincipal.getRole().equals("ADMIN")) {
			post.setTitle(updatedPost.getTitle());
			post.setContent(updatedPost.getContent());
			post.setAuthor(updatedPost.getAuthor());
			post.setUpdatedAt(new Date());
			post.setPublishedAt(new Date());
			int cutUpto = (updatedPost.getContent().length() > 250) ? 250 : updatedPost.getContent().length();
			post.setExcerpt(post.getContent().substring(0, cutUpto) + "...");
			post.setPublished(updatedPost.isPublished());

			List<Tag> tags = new ArrayList<>();
			for (Tag tag : updatedPost.getTags()) {
				Tag t = tagService.getTagByName(tag.getName());
				if (t == null) {
					Tag t1 = new Tag();
					t1.setName(tag.getName());
					t1.setCreatedAt(new Date());
					t1.setUpdatedAt(new Date());
					t1.getPost().add(post);
					tags.add(t1);
				} else {
					tags.add(t);
				}

			}

			post.setTags(tags);

			List<Comment> comments = new ArrayList<>();
			for (Comment comment : updatedPost.getComments()) {
				Comment oldComment = commentService.getCommentById(comment.getId());
				if (oldComment == null) {
					Comment newCommnet = new Comment();
					newCommnet.setName(userPrincipal.getName());
					newCommnet.setEmail(userPrincipal.getEmail());
					newCommnet.setComment(comment.getComment());
					newCommnet.setCreatedAt(new Date());
					newCommnet.setUpdatedAt(new Date());
					newCommnet.setPosts(post);

					oldComment = newCommnet;

				} else {
					oldComment.setComment(comment.getComment());
					oldComment.setUpdatedAt(new Date());
					oldComment.setPosts(post);

				}

				comments.add(oldComment);
			}
			post.setComments(comments);
			postService.savePost(post);

			return new ResponseEntity<Post>(post, HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<Post>(post, HttpStatus.UNAUTHORIZED);
	}

	@DeleteMapping("/post/{id}")
	public ResponseEntity<Post> deletePostById(@PathVariable("id")int postId)
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		Post post = postService.getPostById(postId);
		if(post != null && (userPrincipal.getRole().equals("ADMIN") || userPrincipal.getId() == post.getUser().getId()))
		{
			postService.deletePost(post);
			return new ResponseEntity<Post>(HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<Post>(HttpStatus.UNAUTHORIZED);
	}

}
