package com.nurhassan.demo.controler;

import java.awt.Taskbar.State;
//import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
//import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpRange;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.nurhassan.demo.entities.Posts;
import com.nurhassan.demo.entities.Tags;
import com.nurhassan.demo.repo.PostsRepo;
import com.nurhassan.demo.repo.TagsRepo;

@RestController
@Controller
public class HomeControler {

	@Autowired
	PostsRepo postRepo;
	@Autowired
	TagsRepo tagRepo;

//	private Sort.Direction getSortDirection(String direction)
//	{
//		if(direction.equals("asc"))
//		{
//			return Sort.Direction.ASC;
//		}else if(direction.equals("desc"))
//		{
//			return Sort.Direction.DESC;
//		}
//		return Sort.Direction.ASC;
//	}

	@RequestMapping(value = { "/posts", "/" })
	public ModelAndView getPages(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();

		int start = 0;
		int limit = 2;

		Page<Posts> postList;
		if (request.getParameter("pageNumber") == null) {
			postList = postRepo.findAll(PageRequest.of(start, limit));
			mv.addObject("pageNumber", start);
			mv.addObject("postList", postList);
			mv.addObject("totalPages", postList.getTotalPages());
		}
		if (request.getParameter("pageNumber") != null) {
			start = Integer.parseInt(request.getParameter("pageNumber"));
			postList = postRepo.findAll(PageRequest.of(start, limit));
			mv.addObject("postList", postList);
			mv.addObject("pageNumber", start);
			mv.addObject("totalPages", postList.getTotalPages());
		}

		mv.addObject("limit", 3);
		mv.setViewName("testpage");

		return mv;
	}

	@RequestMapping("/posts/searchedposts")
	public ModelAndView getSearchedPosts(@RequestParam("searchArg") String searchArg) {

		return getSearchedPostsByAny(searchArg);
	}

	public ModelAndView getSearchedPostsByAny(String searchArg) {
		ModelAndView mv = new ModelAndView();
		Set<Posts> tagNamedPosts = new HashSet<>();
		searchArg = searchArg.toLowerCase();

		List<Posts> posts = postRepo.findAll();
		for (Posts post : posts) {
			List<Tags> tags = post.getTags();
			for (Tags tag : tags) {
				if (tag.getName().toLowerCase().contains(searchArg) || post.getTitle().toLowerCase().contains(searchArg)
						|| post.getAuthor().toLowerCase().contains(searchArg)
						|| post.getContent().toLowerCase().contains(searchArg)) {
					tagNamedPosts.add(post);
				}

			}
		}

		mv.addObject("postList", tagNamedPosts);
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
		for (Posts post : posts) {
			List<Tags> tags = post.getTags();
			for (Tags tag : tags) {
				if (tag.getName().contains(tagName)) {
					tagNamedPosts.add(post);
				}
			}
		}

		mv.addObject("postList", tagNamedPosts);
		mv.setViewName("homepage");

		return mv;
	}
}
