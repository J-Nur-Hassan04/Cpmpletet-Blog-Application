package com.nurhassan.demo.controler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.nurhassan.demo.entities.Posts;
import com.nurhassan.demo.entities.Tags;
import com.nurhassan.demo.repo.PostsRepo;
import com.nurhassan.demo.repo.TagsRepo;

@Controller
public class HomeControler {

	@Autowired
	PostsRepo postRepo;
	@Autowired
	TagsRepo tagRepo;

	@RequestMapping(value = { "/", "/posts" })
	public ModelAndView getHomePage() {
		ModelAndView mv = new ModelAndView();

		mv.addObject("postList", postRepo.findAll());
		mv.setViewName("homepage");
		return mv;
	}

	@RequestMapping(value = { "/posts/{id}/{details}", "/posts/{id}" })
	public ModelAndView readFulPost(@PathVariable("id") int id) {
		ModelAndView mv = new ModelAndView();
		Posts post = postRepo.findById(id).orElse(null);
		mv.addObject("post", post);
		mv.setViewName("fullcontentpage");
		return mv;
	}

	@RequestMapping("/posts/drafts")
	public ModelAndView getDrafts() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("postList", postRepo.findAll());
		mv.setViewName("draftspage");
		return mv;
	}

	@RequestMapping("/posts/sorted")
	public ModelAndView getSortedHomePage() {
		ModelAndView mv = new ModelAndView();

		mv.addObject("postList", postRepo.findAllByOrderByPublishedAtDesc());
		mv.setViewName("homepage");
		return mv;
	}

	@RequestMapping("/posts/authors")
	public ModelAndView getAuthors() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("authorList", postRepo.getAllAuthors());
		mv.setViewName("authors");
		return mv;
	}

	@RequestMapping("/posts/author/{authorname}")
	public ModelAndView getPostsOfAuthor(@PathVariable("authorname") String authorName) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("postList", postRepo.findAllByAuthor(authorName));
		mv.setViewName("homepage");
		return mv;
	}

	@RequestMapping("/posts/tags")
	public ModelAndView getTags() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("tagsList", tagRepo.getAllTags());
		mv.setViewName("tags");
		return mv;
	}

	@RequestMapping("/posts/tag/{tagname}")
	public ModelAndView getTagedPosts(@PathVariable("tagname") String tagName) {
		ModelAndView mv = new ModelAndView();

		List<Posts> tagNamedPosts = new ArrayList<>();
		
		List<Posts> posts = postRepo.findAll();
		for(Posts post : posts)
		{
			List<Tags> tags = post.getTags();
			for(Tags tag : tags)
			{
				if(tag.getName().contains(tagName))
				{
					tagNamedPosts.add(post);
				}
			}
		}
		mv.addObject("postList", tagNamedPosts);

		mv.setViewName("homepage");
		return mv;
	}

}
