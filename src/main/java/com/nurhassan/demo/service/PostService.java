package com.nurhassan.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.nurhassan.demo.entities.Post;

public interface PostService {
	public Post savePost(Post post);

	public Post getPostById(int id);

	public void deletePost(Post post);
	
	public List<Post> getDraftsOfUser(int id);

	public Page<Post> getAllPostsOfPage(int start, int limit);

	public Page<Post> getSearchedPosts(String searchArg, int start, int limit);

	public List<String> getAllAuthor();

	public List<Post> getAllPosts();
	
	public Page<Post> getAllPostsByAuthor(String[] author,int start, int limit);
	
	public Page<Post> getSearchedPostsByTags(String[] tags, int start, int limit);
	
	public Page<Post> getSearchedPostsBySearchArgAndAuthors(String searchArg, String[] authors, int start, int limit);

	public Page<Post> getSortedPosts(int start, int limit, String column, String basic);
	
	public Page<Post> getSearchedPostsWithTagAndAuthor(int start, int limit, String[] tags, String[] authors);
	
	public Page<Post> getSearchedPostWithSearchArgAndTags(String searchArg,String[] tags, int start, int limit);
	
	public Page<Post> getSearchedPostsWithSearchArgTagAuthors(String searchArg,String[] tags, String[] authors, int start, int limit);
}
