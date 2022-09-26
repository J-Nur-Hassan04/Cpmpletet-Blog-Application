package com.nurhassan.demo.controler;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.nurhassan.demo.entities.Posts;
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

		mv.addObject("authorList", postRepo.getAllAuthors());
		mv.addObject("tagsList", tagRepo.getAllTags());
		mv.addObject("limit", limit);
		mv.setViewName("indexpage");

		return mv;
	}

//	@RequestMapping(value = { "/posts/searchedposts" }, method = RequestMethod.GET)
//	public ModelAndView getSearchedPosts(HttpServletRequest request) {
//		ModelAndView mv = new ModelAndView();
//		String searchArg = request.getParameter("searchArg").toUpperCase();
//		int start = 0;
//		int limit = 2;
//
//		Page<Posts> postList;
//		if (request.getParameter("pageNumber") == null) {
//			postList = postRepo.searchedPosts(searchArg, PageRequest.of(start, limit));
//			mv.addObject("pageNumber", start);
//			mv.addObject("postList", postList);
//			mv.addObject("totalPages", postList.getTotalPages());
//		}
//		if (request.getParameter("pageNumber") != null) {
//			start = Integer.parseInt(request.getParameter("pageNumber"));
//			postList = postRepo.searchedPosts(searchArg, PageRequest.of(start, limit));
//			mv.addObject("postList", postList);
//			mv.addObject("pageNumber", start);
//			mv.addObject("totalPages", postList.getTotalPages());
//		}
//		mv.addObject("searchArg", searchArg);
//		mv.addObject("limit", limit);
//		mv.setViewName("resultpage");
//
//		return mv;
//
//	}
	@RequestMapping(value = { "/posts/searchedposts" }, method = RequestMethod.GET)
	public ModelAndView getSearchedPostsByAny(String searchArg) {
		ModelAndView mv = new ModelAndView();
		searchArg = searchArg.toUpperCase();
		System.out.println(searchArg);

		List<Posts> tagNamedPosts = postRepo.searchedPosts(searchArg);

		mv.addObject("postList", tagNamedPosts);
		mv.setViewName("resultpage");

		return mv;

	}

	@RequestMapping(value = { "/posts/{id}/{details}", "/posts/{id}" }, method = RequestMethod.GET)
	public ModelAndView readFulPost(@PathVariable("id") int id) {
		ModelAndView mv = new ModelAndView();
		Posts post = postRepo.findById(id).orElse(null);

		mv.addObject("post", post);
		mv.setViewName("fullcontentpage");

		return mv;
	}

	@RequestMapping(value = { "/posts/drafts" }, method = RequestMethod.GET)
	public ModelAndView getDrafts() {
		ModelAndView mv = new ModelAndView();

		mv.addObject("postList", postRepo.findAll());
		mv.setViewName("draftspage");

		return mv;
	}

	@RequestMapping(value = { "/posts/olderposts" }, method = RequestMethod.GET)
	public ModelAndView getSortedHomePageDesc(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();

		int start = 0;
		int limit = 2;

		Page<Posts> postList;
		
		if (request.getParameter("pageNumber") == null) {
			postList = postRepo.findAll(PageRequest.of(start, limit, Sort.by("publishedAt").ascending()));
			mv.addObject("pageNumber", start);
			mv.addObject("postList", postList);
			mv.addObject("totalPages", postList.getTotalPages());
		}
		if (request.getParameter("pageNumber") != null) {
			start = Integer.parseInt(request.getParameter("pageNumber"));
			postList = postRepo.findAll(PageRequest.of(start, limit, Sort.by("publishedAt").ascending()));
			mv.addObject("postList", postList);
			mv.addObject("pageNumber", start);
			mv.addObject("totalPages", postList.getTotalPages());
		}
		
		mv.addObject("authorList", postRepo.getAllAuthors());
		mv.addObject("tagsList", tagRepo.getAllTags());
		mv.addObject("limit", limit);
		mv.setViewName("indexpage");

		return mv;
	}

	@RequestMapping(value = { "/posts/recentposts" }, method = RequestMethod.GET)
	public ModelAndView getSortedHomePageAsc(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();

		int start = 0;
		int limit = 2;

		Page<Posts> postList;
		if (request.getParameter("pageNumber") == null) {
			postList = postRepo.findAll(PageRequest.of(start, limit, Sort.by("publishedAt").descending()));
			mv.addObject("pageNumber", start);
			mv.addObject("postList", postList);
			mv.addObject("totalPages", postList.getTotalPages());
		}
		if (request.getParameter("pageNumber") != null) {
			start = Integer.parseInt(request.getParameter("pageNumber"));
			postList = postRepo.findAll(PageRequest.of(start, limit, Sort.by("publishedAt").descending()));
			mv.addObject("postList", postList);
			mv.addObject("pageNumber", start);
			mv.addObject("totalPages", postList.getTotalPages());
		}

		mv.addObject("authorList", postRepo.getAllAuthors());
		mv.addObject("tagsList", tagRepo.getAllTags());
		mv.addObject("limit", limit);
		mv.setViewName("indexpage");

		return mv;
	}

	@RequestMapping(value = { "/posts/tag" }, method = RequestMethod.GET)
	public ModelAndView getTagedPosts(
			@RequestParam(name = "tagName", required = false, defaultValue = "#") String[] tagName) {
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("postList", postRepo.findAllByTagsArray(tagName));	
		mv.setViewName("resultpage");
		
		return mv;
	}

	@RequestMapping(value = { "/posts/author" }, method = RequestMethod.GET)
	public ModelAndView getFilteredAuthor(
			@RequestParam(name = "authorName", required = false, defaultValue = "author") String[] authors) {
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("postList", postRepo.findAllByAuthorArray(authors));
		mv.setViewName("resultpage");
		
		return mv;
	}

	@RequestMapping("/login")
	public ModelAndView getLoginForm() {
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("login");
		
		return mv;
	}
}