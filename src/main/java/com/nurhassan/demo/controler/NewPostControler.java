package com.nurhassan.demo.controler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.nurhassan.demo.entities.Posts;
import com.nurhassan.demo.entities.Tags;
import com.nurhassan.demo.repo.PostsRepo;
import com.nurhassan.demo.repo.TagsRepo;

@Controller
public class NewPostControler {

	@Autowired
	PostsRepo postRepo;
	@Autowired
	TagsRepo tagRepo;

	@RequestMapping(value = { "/newpost", "/posts/newpost" })
	public ModelAndView getNewBlogForm() {
		ModelAndView mv = new ModelAndView();

		mv.addObject("post", new Posts());
		mv.setViewName("newpostform");

		return mv;
	}

	@RequestMapping("/newpost/savenewpost")
	public String saveNewPost(Posts post, @RequestParam("tagsname") String tagsName,
			@RequestParam("postType") String postType) {

		post.setPublishedAt(new Date());
		post.setCreatedAt(new Date());
		post.setUpdatedAt(new Date());
		int cutUpto = (post.getContent().length() > 250) ? 250 : post.getContent().length();
		post.setExcerpt(post.getContent().substring(0, cutUpto) + "...");

		post.setPublished(postType.equals("publish"));

		String[] tags = tagsName.split(",");

		List<Tags> newTags = new ArrayList<>();
		for (String tag : tags) {
			tag = tag.trim();
			Tags postTag = new Tags();
			postTag.setName(tag);
			postTag.setCreatedAt(new Date());
			postTag.setUpdatedAt(new Date());
			postTag.getPost().add(post);
			post.getTags().add(postTag);

			newTags.add(checkAvailablity(tag));

		}

		post.setTags(newTags);
		postRepo.save(post);

		return "redirect:/";
	}

	public Tags checkAvailablity(String tagName) {
		Tags tag = tagRepo.findByName(tagName);

		if (tag == null) {
			Tags newTag = new Tags();
			newTag.setName(tagName);
			newTag.setCreatedAt(new Date());
			newTag.setUpdatedAt(new Date());
			return newTag;
		}

		return tag;
	}

	@RequestMapping("/posts/{id}/{details}/update")
	public ModelAndView getUpdatePostForm(Posts post, @PathVariable("id") int id, @PathVariable("details") String title,
			@RequestParam("previoustags") String tags) {
		ModelAndView mv = new ModelAndView();

		mv.addObject("post", post);
		mv.addObject("tags", tags);
		mv.setViewName("updateform");

		return mv;
	}

	@RequestMapping(value = { "/posts/{id}/{details}/update/storenewpost" })
	public String storeupdatedPost(Posts postToUpdate, @RequestParam("tagsname") String tagsName,
			@RequestParam("postType") String postType, @PathVariable("id") int id) {
		Posts previousPost = postRepo.findById(id).orElse(postToUpdate);

		previousPost.setTitle(postToUpdate.getTitle());
		previousPost.setContent(postToUpdate.getContent());
		previousPost.setAuthor(postToUpdate.getAuthor());
		previousPost.setUpdatedAt(new Date());
		int cutUpto = (postToUpdate.getContent().length() > 250) ? 250 : postToUpdate.getContent().length();
		previousPost.setExcerpt(postToUpdate.getContent().substring(0, cutUpto) + "...");
		previousPost.setPublished(postType.equals("publish"));

		String[] tags = tagsName.split(",");

		List<Tags> newTags = new ArrayList<>();
		for (String tag : tags) {
			tag = tag.trim();
			Tags postTag = new Tags();
			postTag.setName(tag);
			postTag.setCreatedAt(new Date());
			postTag.setUpdatedAt(new Date());
			postTag.getPost().add(previousPost);
			previousPost.getTags().add(postTag);

			newTags.add(checkAvailablity(tag));

		}

		previousPost.setTags(newTags);
		postRepo.save(previousPost);

		return "redirect:/";
	}

	@RequestMapping("/posts/{id}/{details}/delete")
	public ModelAndView deletePost(@PathVariable("id") int id, @PathVariable("details") String title) {
		ModelAndView mv = new ModelAndView();

		Posts postToBeDelete = postRepo.findById(id).orElse(null);
		postRepo.delete(postToBeDelete);
		mv.setViewName("redirect:/");

		return mv;
	}
}
