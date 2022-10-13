package com.nurhassan.demo.api;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.nurhassan.demo.specification.PostSpecification;

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
		List<User> users = userService.getAllUserData();
		if (users == null) {
			return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<User>>(users, HttpStatus.FOUND);
	}

	@GetMapping("/admin/user-data/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") int userId) {
		User user = userService.getUserById(userId);
		if (user == null) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(user, HttpStatus.FOUND);
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
	public ResponseEntity<List<Post>> getPostsPagination(@RequestParam("pagenumber") int pageNumber,
			@RequestParam("limit") int limit) {
		return new ResponseEntity<List<Post>>(postService.getAllPostsOfPage(pageNumber, limit).getContent(), HttpStatus.FOUND);
	}

	@GetMapping("/allposts/sorted")
	public ResponseEntity<List<Post>> getSortedPostsPagination(@RequestParam("pagenumber") int pageNumber,
			@RequestParam("limit") int limit, @RequestParam("on") String column, @RequestParam("by") String sortBy) {
		return new ResponseEntity<List<Post>>(postService.getSortedPosts(pageNumber, limit, column, sortBy).getContent(),
				HttpStatus.FOUND);
	}

	@GetMapping("/add-post")
	public String getNewPostTemplate() {
		return "{\n" + "    \"title\": \"\",\n" + "    \"content\": \"\",\n" + "    \"author\": \"\",\n"
				+ "    \"tags\": [\n" + "        {\n" + "            \"name\": \"\"\n" + "        }\n" + "    ],\n"
				+ "    \"published\": \n" + "}";
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
			if (tag.getName().charAt(0) != '#') {
				tag.setName("#" + tag.getName());
			}
			Tag prestTag = tagService.getTagByName(tag.getName());
			if (prestTag == null) {
				Tag newTag = new Tag();
				newTag.setName(tag.getName());
				newTag.setCreatedAt(new Date());
				newTag.setUpdatedAt(new Date());
				newTag.getPost().add(newPost);
				tags.add(newTag);
			} else {
				tags.add(prestTag);
			}

		}

		newPost.setTags(tags);
		newPost.setUser(userService.getUserById(userPrincipal.getId()));

		return new ResponseEntity<Post>(postService.savePost(newPost), HttpStatus.CREATED);
	}

	@GetMapping("/post/{id}")
	public ResponseEntity<Post> getPostById(@PathVariable("id") int postId) {
		return new ResponseEntity<Post>(postService.getPostById(postId), HttpStatus.FOUND);
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

				if (tag.getName().charAt(0) != '#') {
					tag.setName("#" + tag.getName());
				}

				Tag presentTag = tagService.getTagByName(tag.getName());
				if (presentTag == null) {
					Tag newTag = new Tag();
					newTag.setName(tag.getName());
					newTag.setCreatedAt(new Date());
					newTag.setUpdatedAt(new Date());
					newTag.getPost().add(post);
					tags.add(newTag);
				} else {
					tags.add(presentTag);
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
					newCommnet.setPost(post);

					oldComment = newCommnet;

				} else {
					oldComment.setComment(comment.getComment());
					oldComment.setUpdatedAt(new Date());
					oldComment.setPost(post);

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
	public ResponseEntity<Post> deletePostById(@PathVariable("id") int postId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		Post post = postService.getPostById(postId);
		if (post != null
				&& (userPrincipal.getRole().equals("ADMIN") || userPrincipal.getId() == post.getUser().getId())) {
			postService.deletePost(post);
			return new ResponseEntity<Post>(HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<Post>(HttpStatus.UNAUTHORIZED);
	}

	@GetMapping("/post/{id}/tags")
	public ResponseEntity<List<Tag>> getTagsByPostId(@PathVariable("id") int postId) {
		Post post = postService.getPostById(postId);

		if (post != null) {
			return new ResponseEntity<List<Tag>>(post.getTags(), HttpStatus.FOUND);
		}

		return new ResponseEntity<List<Tag>>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/post/{id}/comments")
	public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable("id") int postId) {
		List<Comment> comments = postService.getPostById(postId).getComments();
		if (comments == null) {
			return new ResponseEntity<List<Comment>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Comment>>(comments, HttpStatus.FOUND);
	}

	@GetMapping("/post/{id}/comment/{commentId}")
	public ResponseEntity<Comment> getCommentOfPostById(@PathVariable("id") int postId,
			@PathVariable("commentId") int commentId) {
		Post post = postService.getPostById(postId);
		Comment comment = commentService.getCommentById(commentId);
		if (comment == null) {
			return new ResponseEntity<Comment>(HttpStatus.NOT_FOUND);
		} else if (post.getId() == comment.getPost().getId()) {
			return new ResponseEntity<Comment>(comment, HttpStatus.FOUND);
		}
		return new ResponseEntity<Comment>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/post/{id}/comment/{commentId}")
	public ResponseEntity<Comment> deleteCommentOfPostById(@PathVariable("id") int postId,
			@PathVariable("commentId") int commentId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

		Post post = postService.getPostById(postId);
		Comment commentToDelete = commentService.getCommentById(commentId);
		if (commentToDelete == null) {
			return new ResponseEntity<Comment>(HttpStatus.NOT_FOUND);
		} else if ((post.getUser().getId() == userPrincipal.getId() || userPrincipal.getRole().equals("ADMIN"))
				&& post.getId() == commentToDelete.getPost().getId()) {
			commentService.deleteCommentById(commentId);
			return new ResponseEntity<Comment>(HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<Comment>(HttpStatus.UNAUTHORIZED);
	}

	@PutMapping("/post/{id}/comment/{commentId}")
	public ResponseEntity<Comment> updateCommentById(@RequestBody Comment comment, @PathVariable("id") int postId,
			@PathVariable("commentId") int commentId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

		Post post = postService.getPostById(postId);

		if (post.getUser().getId() == userPrincipal.getId() || userPrincipal.getRole().equals("ADMIN")) {
			Comment commentToUpadate = commentService.getCommentById(commentId);
			commentToUpadate.setComment(comment.getComment());
			commentToUpadate.setUpdatedAt(new Date());

			commentService.saveComment(commentToUpadate);

			return new ResponseEntity<Comment>(commentToUpadate, HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<Comment>(HttpStatus.UNAUTHORIZED);
	}

	@PostMapping("/post/{id}/add-comment")
	public ResponseEntity<Post> addNewCommnet(@RequestBody Comment newComment, @PathVariable("id") int postId) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Post post = postService.getPostById(postId);
		List<Comment> postComments = post.getComments();

		if (authentication.getName().equals("anonymousUser")) {
			Comment comment = new Comment();
			comment.setName(newComment.getName());
			comment.setEmail(newComment.getEmail());
			comment.setComment(newComment.getComment());
			comment.setCreatedAt(new Date());
			comment.setUpdatedAt(new Date());
			comment.setPost(post);
			postComments.add(comment);
		} else {
			UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
			Comment comment = new Comment();
			comment.setName(userPrincipal.getName());
			comment.setEmail(userPrincipal.getEmail());
			comment.setComment(newComment.getComment());
			comment.setCreatedAt(new Date());
			comment.setUpdatedAt(new Date());
			comment.setPost(post);
			postComments.add(comment);
		}

		return new ResponseEntity<Post>(postService.savePost(post), HttpStatus.CREATED);
	}

	@GetMapping(value = { "/allposts/searchedposts/" })
	public ResponseEntity<List<Post>> getSearchedPosts(@RequestParam(required = false, value = "searchArg") String searchArg,
			@RequestParam(required = false, value = "tagName") String tags,
			@RequestParam(required = false, value = "authorName") String authors,
			@RequestParam(required = false, value = "pageNumber")String pageNumber) {
		
		
		
		
		int start = (pageNumber == null)? 0 :(pageNumber.length() < 1) ? 0 :Integer.parseInt(pageNumber);
		int limit = 2;
		Page<Post> postList = null;

		searchArg = (searchArg == null)? null : (searchArg.length() < 1)?null: searchArg.toUpperCase();
		tags = (tags == null)? null : (tags.length() < 1)? null : tags;
		authors = (authors == null )? null : (authors.length() < 1)?null : authors;
		
		List<String> filtredTags = null;
		filtredTags = (tags == null)? null : Arrays.asList(tags.split(","));
		
		List<String> filtredAuthors = null;
		filtredAuthors = (authors == null)? null : Arrays.asList(authors.split(","));
		

		postList = postService.getSearchAndFilterResult(PostSpecification.findSearchAndFiltredResult(searchArg, filtredAuthors, filtredTags), start, limit);

	
		return new ResponseEntity<List<Post>>(postList.getContent(), HttpStatus.OK);

	}

	String arrayToString(String[] args) {
		String result = "";
		for (String arg : args) {
			result += arg + ",";
		}
		return result.substring(0, result.length() - 1);
	}

}
