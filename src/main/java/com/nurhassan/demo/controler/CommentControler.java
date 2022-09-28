package com.nurhassan.demo.controler;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.nurhassan.demo.entities.Comment;
import com.nurhassan.demo.entities.Post;
import com.nurhassan.demo.service.CommentService;
import com.nurhassan.demo.service.PostService;

@Controller
public class CommentControler {

	@Autowired
	PostService postService;

	@Autowired
	CommentService commentService;

	@RequestMapping(value = { "/posts/{id}/addcomment/savecomment" }, method = RequestMethod.POST)
	public ModelAndView saveNewComment(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("comment") String comment, @PathVariable("id") int postId) {
		Post post = postService.getPostById(postId);

		Comment newComment = new Comment();

		newComment.setName(name);
		newComment.setEmail(email);
		newComment.setComment(comment);
		newComment.setCreatedAt(new Date());
		newComment.setUpdatedAt(new Date());
		newComment.setPosts(post);
		post.getComments().add(newComment);

		postService.savePost(post);

		return new ModelAndView("redirect:/posts/" + postId);
	}

	@RequestMapping(value = { "/posts/{id}/{commentid}/updatecomment" }, method = RequestMethod.GET)
	public ModelAndView getCommentUpdatePage(@PathVariable("commentid") int commentId, @PathVariable("id") int postId) {
		ModelAndView mv = new ModelAndView();
		Comment comment = commentService.getCommentById(commentId);
		if (comment == null) {
			return new ModelAndView("redirect:/posts/" + postId);
		}
		mv.addObject("comment", comment);
		mv.addObject("postId", postId);
		mv.setViewName("commentform");

		return mv;
	}

	@RequestMapping(value = { "/posts/{id}/{commentid}/updatecomment/savecomment" }, method = RequestMethod.POST)
	public ModelAndView saveUpdatedComment(@PathVariable("commentid") int commentId, @PathVariable("id") int postId,
			@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("comment") String comment) {

		Comment previousComment = commentService.getCommentById(commentId);

		previousComment.setName(name);
		previousComment.setEmail(email);
		previousComment.setComment(comment);
		previousComment.setUpdatedAt(new Date());

		commentService.saveComment(previousComment);
		return new ModelAndView("redirect:/posts/" + postId);

	}

	@RequestMapping(value = { "/posts/{id}/{commentid}/deletecomment" }, method = RequestMethod.POST)
	public ModelAndView deleteComment(@PathVariable("commentid") int commentId, @PathVariable("id") int postId) {
		commentService.deleteCommentById(commentId);

		return new ModelAndView("redirect:/posts/" + postId);
	}

}