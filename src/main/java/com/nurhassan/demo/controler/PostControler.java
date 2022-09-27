
package com.nurhassan.demo.controler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.nurhassan.demo.entities.Post;
import com.nurhassan.demo.entities.Tag;
import com.nurhassan.demo.service.PostService;
import com.nurhassan.demo.service.TagService;

@Controller
public class PostControler {

	@Autowired
	PostService postService;

	@Autowired
	TagService tagService;

	@RequestMapping(value = { "/newpost" })
	public ModelAndView getNewBlogForm() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("post", new Post());
		mv.setViewName("newpostform");

		return mv;
	}

	@RequestMapping(value = { "/newpost/savenewpost" }, method = RequestMethod.POST)
	public String saveNewPost(Post post, @RequestParam("tagsname") String postTags,
			@RequestParam("postType") String postType) {

		post.setPublishedAt(new Date());
		post.setCreatedAt(new Date());
		post.setUpdatedAt(new Date());
		int cutUpto = (post.getContent().length() > 250) ? 250 : post.getContent().length();
		post.setExcerpt(post.getContent().substring(0, cutUpto) + "...");
		post.setPublished(postType.equals("publish"));
		String[] tags = postTags.split(",");

		List<Tag> newTags = new ArrayList<>();
		for (String tag : tags) {
			tag = tag.trim();
			if (tag.charAt(0) != '#') {
				tag = "#" + tag;
			}
			Tag postTag = new Tag();
			postTag.setName(tag);
			postTag.setCreatedAt(new Date());
			postTag.setUpdatedAt(new Date());
			postTag.getPost().add(post);
			post.getTags().add(postTag);

			newTags.add(checkAvailablity(tag));
		}

		post.setTags(newTags);
//		postRepo.save(post);
		postService.savePost(post);

		return "redirect:/";
	}

	public Tag checkAvailablity(String tagName) {
		Tag tag = tagService.getTagByName(tagName);

		if (tag == null) {
			Tag newTag = new Tag();
			newTag.setName(tagName);
			newTag.setCreatedAt(new Date());
			newTag.setUpdatedAt(new Date());
			return newTag;
		}
		return tag;
	}

	@RequestMapping(value = { "/posts/{id}/update" }, method = RequestMethod.GET)
	public ModelAndView getUpdatePostForm(Post post, @PathVariable("id") int id,
			@RequestParam("previoustags") String tags) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("post", post);
		mv.addObject("tags", tags);
		mv.setViewName("updateform");

		return mv;
	}

	@RequestMapping(value = { "/posts/{id}/update/storenewpost" }, method = RequestMethod.POST)
	public String storeupdatedPost(Post postToUpdate, @RequestParam("tagsname") String tagsName,
			@RequestParam("postType") String postType, @PathVariable("id") int id) {
		Post previousPost = postService.getPostById(id);

		previousPost.setTitle(postToUpdate.getTitle());
		previousPost.setContent(postToUpdate.getContent());
		previousPost.setAuthor(postToUpdate.getAuthor());
		previousPost.setUpdatedAt(new Date());
		int cutUpto = (postToUpdate.getContent().length() > 250) ? 250 : postToUpdate.getContent().length();
		previousPost.setExcerpt(postToUpdate.getContent().substring(0, cutUpto) + "...");
		previousPost.setPublished(postType.equals("publish"));

		String[] tags = tagsName.split(",");

		List<Tag> newTags = new ArrayList<>();
		for (String tag : tags) {
			tag = tag.trim();
			if (tag.charAt(0) != '#') {
				tag = "#" + tag;
			}
			Tag postTag = new Tag();
			postTag.setName(tag);
			postTag.setCreatedAt(new Date());
			postTag.setUpdatedAt(new Date());
			postTag.getPost().add(previousPost);
			previousPost.getTags().add(postTag);

			newTags.add(checkAvailablity(tag));

		}

		previousPost.setTags(newTags);
		postService.savePost(previousPost);

		return "redirect:/";
	}

	@RequestMapping(value = { "/posts/{id}/delete" }, method = RequestMethod.POST)
	public ModelAndView deletePost(@PathVariable("id") int id) {
		Post postToBeDelete = postService.getPostById(id);
		postService.deletePost(postToBeDelete);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/");

		return mv;
	}
}