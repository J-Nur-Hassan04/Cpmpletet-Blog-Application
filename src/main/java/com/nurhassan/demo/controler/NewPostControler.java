package com.nurhassan.demo.controler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

	@RequestMapping("/newpost")
	public String getNewBlogForm() {
		return "newblogform";
	}

	@RequestMapping("/storenewpost")
	public String storeNewPost(Posts post, @RequestParam("tagsname") String tagsName,
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

	public Tags checkAvailablity(String tagName) 
	{
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

}
