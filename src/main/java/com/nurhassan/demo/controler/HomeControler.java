package com.nurhassan.demo.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.nurhassan.demo.entities.Posts;
import com.nurhassan.demo.repo.PostsRepo;

@Controller
public class HomeControler {

	@Autowired
	PostsRepo postRepo;

	@RequestMapping( value =  {"/", "/posts"})
	public ModelAndView getHomePage() {
		ModelAndView mv = new ModelAndView();

		mv.addObject("postList", postRepo.findAll());
		mv.setViewName("homepage");
		return mv;
	}
	
	@RequestMapping("/posts/{id}/{details}")
	public ModelAndView readFulPost(@PathVariable("id") int id)
	{
		ModelAndView mv = new ModelAndView();
		Posts post = postRepo.findById(id).orElse(null);
		mv.addObject("post", post);
		mv.setViewName("fullcontentpage");
		return mv;
	}
}
