package com.nurhassan.demo.controler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.nurhassan.demo.entities.Post;
import com.nurhassan.demo.service.PostService;
import com.nurhassan.demo.service.TagService;

@RestController
@Controller
public class HomeControler {

	@Autowired
	private PostService postService;

	@Autowired
	private TagService tagService;

	@RequestMapping(value = { "/posts", "/" })
	public ModelAndView getPages(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		int start = 0;
		int limit = 2;

		Page<Post> postList;
		if (request.getParameter("pageNumber") == null) {
			postList = postService.getAllPostsOfPage(start, limit);
			modelAndView.addObject("pageNumber", start);
			modelAndView.addObject("postList", postList);
			modelAndView.addObject("totalPages", postList.getTotalPages());
		}
		if (request.getParameter("pageNumber") != null) {
			start = Integer.parseInt(request.getParameter("pageNumber"));
			postList = postService.getAllPostsOfPage(start, limit);
			modelAndView.addObject("postList", postList);
			modelAndView.addObject("pageNumber", start);
			modelAndView.addObject("totalPages", postList.getTotalPages());
		}

		modelAndView.addObject("authorList", postService.getAllAuthor());
		modelAndView.addObject("tagsList", tagService.getAlltag());
		modelAndView.addObject("limit", limit);
		modelAndView.setViewName("indexpage");

		return modelAndView;
	}

	@RequestMapping(value = { "/posts/searchedposts" }, method = RequestMethod.GET)
	public ModelAndView getSearchedPosts(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		int start = 0;
		int limit = 2;
		Page<Post> postList = null;
		String filtredTags = request.getParameter("tagName");
		String filtredAuthors = request.getParameter("authorName");
		filtredTags = filtredTags == null ? "foundNull" : filtredTags.length() > 0 ?filtredTags : "foundNull";  
		String searchArg = request.getParameter("searchArg");
		filtredAuthors = filtredAuthors == null ? "foundNull" : filtredAuthors.length() > 0 ? filtredAuthors : "foundNull";

		if (!searchArg.isEmpty() && filtredTags.equals("foundNull") && filtredAuthors.equals("foundNull")) {
			searchArg = searchArg.toUpperCase();

			if (request.getParameter("pageNumber") == null) {
				postList = postService.getSearchedPosts(searchArg, start, limit);
			}
			if (request.getParameter("pageNumber") != null) {
				start = Integer.parseInt(request.getParameter("pageNumber"));
				postList = postService.getSearchedPosts(searchArg, start, limit);
			}

		} else if (!searchArg.isEmpty() && !filtredTags.equals("foundNull") && !filtredAuthors.equals("foundNull")) {
			searchArg = request.getParameter("searchArg").toUpperCase();
			filtredTags = arrayToString(request.getParameterValues("tagName"));
			filtredAuthors = arrayToString(request.getParameterValues("authorName"));

			if (request.getParameter("pageNumber") == null) {
				postList = postService.getSearchedPostsWithSearchArgTagAuthors(searchArg, filtredTags.split(","),
						filtredAuthors.split(","), start, limit);
			}
			if (request.getParameter("pageNumber") != null) {
				start = Integer.parseInt(request.getParameter("pageNumber"));
				postList = postService.getSearchedPostsWithSearchArgTagAuthors(searchArg, filtredTags.split(","),
						filtredAuthors.split(","), start, limit);
			}

		}else if (!searchArg.isEmpty() && !filtredTags.equals("foundNull") && filtredAuthors.equals("foundNull")) {
			searchArg = request.getParameter("searchArg").toUpperCase();
			filtredTags = arrayToString(request.getParameterValues("tagName"));

			if (request.getParameter("pageNumber") == null) {
				postList = postService.getSearchedPostWithSearchArgAndTags(searchArg, filtredTags.split(","), start, limit);
			}
			if (request.getParameter("pageNumber") != null) {
				start = Integer.parseInt(request.getParameter("pageNumber"));
				postList = postService.getSearchedPostWithSearchArgAndTags(searchArg, filtredTags.split(","), start, limit);
			}

		} else if (searchArg.isEmpty() && !filtredTags.equals("foundNull") && !filtredAuthors.equals("foundNull")) {
			filtredTags = arrayToString(request.getParameterValues("tagName"));
			filtredAuthors = arrayToString(request.getParameterValues("authorName"));

			if (request.getParameter("pageNumber") == null) {
				postList = postService.getSearchedPostsWithTagAndAuthor(start, limit, filtredTags.split(","),
						filtredAuthors.split(","));
			}
			if (request.getParameter("pageNumber") != null) {
				start = Integer.parseInt(request.getParameter("pageNumber"));
				postList = postService.getSearchedPostsWithTagAndAuthor(start, limit, filtredTags.split(","),
						filtredAuthors.split(","));

			}
		}
		modelAndView.addObject("authorList", postService.getAllAuthor());
		modelAndView.addObject("tagsList",tagService.getAlltag());
		modelAndView.addObject("searchArg", searchArg);
		modelAndView.addObject("limit", limit);
		modelAndView.setViewName("searchresultpage");
		modelAndView.addObject("tagName", filtredTags);
		modelAndView.addObject("authorName", filtredAuthors);
		modelAndView.addObject("postList", postList);
		modelAndView.addObject("pageNumber", start);
		modelAndView.addObject("totalPages", postList.getTotalPages());

		return modelAndView;

	}

	String arrayToString(String[] args) {
		String result = "";
		for (String arg : args) {
			result += arg + ",";
		}
		return result.substring(0, result.length() - 1);
	}

	@RequestMapping(value = { "/posts/{id}" }, method = RequestMethod.GET)
	public ModelAndView readFulPost(@PathVariable("id") int id) {
		ModelAndView modelAndView = new ModelAndView();
		Post post = postService.getPostById(id);

		modelAndView.addObject("post", post);
		modelAndView.setViewName("fullcontentpage");

		return modelAndView;
	}

	@RequestMapping(value = { "/posts/drafts" }, method = RequestMethod.GET)
	public ModelAndView getDrafts() {
		ModelAndView modelAndView = new ModelAndView();

		modelAndView.addObject("postList", postService.getAllPosts());
		modelAndView.setViewName("draftspage");

		return modelAndView;
	}

	@RequestMapping(value = { "/posts/olderposts" }, method = RequestMethod.GET)
	public ModelAndView getSortedHomePageDesc(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();

		int start = 0;
		int limit = 2;

		Page<Post> postList;

		if (request.getParameter("pageNumber") == null) {
			postList = postService.getSortedPosts(start, limit, "publishedAt", "ASC");
			modelAndView.addObject("pageNumber", start);
			modelAndView.addObject("postList", postList);
			modelAndView.addObject("totalPages", postList.getTotalPages());
		}
		if (request.getParameter("pageNumber") != null) {
			start = Integer.parseInt(request.getParameter("pageNumber"));
			postList = postService.getSortedPosts(start, limit, "publishedAt", "ASC");
			modelAndView.addObject("postList", postList);
			modelAndView.addObject("pageNumber", start);
			modelAndView.addObject("totalPages", postList.getTotalPages());
		}

		modelAndView.addObject("authorList", postService.getAllAuthor());
		modelAndView.addObject("tagsList", tagService.getAlltag());
		modelAndView.addObject("limit", limit);
		modelAndView.setViewName("indexpage");

		return modelAndView;
	}

	@RequestMapping(value = { "/posts/recentposts" }, method = RequestMethod.GET)
	public ModelAndView getSortedHomePageAsc(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();

		int start = 0;
		int limit = 2;

		Page<Post> postList;
		if (request.getParameter("pageNumber") == null) {
			postList = postService.getSortedPosts(start, limit, "publishedAt", "DESC");
			modelAndView.addObject("pageNumber", start);
			modelAndView.addObject("postList", postList);
			modelAndView.addObject("totalPages", postList.getTotalPages());
		}
		if (request.getParameter("pageNumber") != null) {
			start = Integer.parseInt(request.getParameter("pageNumber"));
			postList = postService.getSortedPosts(start, limit, "publishedAt", "DESC");
			modelAndView.addObject("postList", postList);
			modelAndView.addObject("pageNumber", start);
			modelAndView.addObject("totalPages", postList.getTotalPages());
		}

		modelAndView.addObject("authorList", postService.getAllAuthor());
		modelAndView.addObject("tagsList", tagService.getAlltag());
		modelAndView.addObject("limit", limit);
		modelAndView.setViewName("indexpage");

		return modelAndView;
	}

	@RequestMapping("/login")
	public ModelAndView getLoginForm() {
		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName("login");

		return modelAndView;
	}
}