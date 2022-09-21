package com.nurhassan.demo.controler;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.nurhassan.demo.entities.Comments;
import com.nurhassan.demo.entities.Posts;
import com.nurhassan.demo.repo.ComentsRepo;
import com.nurhassan.demo.repo.PostsRepo;

@Controller
public class CommentControler {

	@Autowired
	ComentsRepo commentRepo;
	@Autowired
	PostsRepo postRepo;

	@RequestMapping("/posts/{id}/{details}/addcomment")
	public ModelAndView addComment(@PathVariable("id") int id, @PathVariable("details") String title) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("commentform");
		mv.addObject("postId", id);
		mv.addObject("title", title);

		return mv;
	}

	@RequestMapping("/posts/{id}/{details}/addcomment/savecomment")
	public ModelAndView updateComment(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("comment") String comment, @PathVariable("id") int postId,
			@PathVariable("details") String title) {
		Posts post = postRepo.findById(postId).orElse(null);

		Comments newComment = new Comments();
		newComment.setName(name);
		newComment.setEmail(email);
		newComment.setComment(comment);
		newComment.setCreatedAt(new Date());
		newComment.setUpdatedAt(new Date());
		newComment.setPosts(post);

		post.getComments().add(newComment);
		postRepo.save(post);
		return new ModelAndView("redirect:/posts/" + postId + "/" + title);
	}
	
	public ModelAndView saveComment(Comments comment, @PathVariable("id") int postId,
			@PathVariable("details") String title) {

		Posts post = postRepo.findById(postId).orElse(null);
		post.getId();

		comment.setCreatedAt(new Date());
		comment.setUpdatedAt(new Date());
		comment.setPosts(post);
		post.getComments().add(comment);
		postRepo.save(post);

		return new ModelAndView("redirect:/posts/" + postId + "/" + title);

	}

}
