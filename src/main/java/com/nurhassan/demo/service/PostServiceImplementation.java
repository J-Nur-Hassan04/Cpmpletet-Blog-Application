package com.nurhassan.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.nurhassan.demo.entities.Post;
import com.nurhassan.demo.repository.PostsRepository;

@Service
public class PostServiceImplementation implements PostService {

	@Autowired
	PostsRepository postRepository;

	@Override
	public void savePost(Post post) {
		postRepository.save(post);

	}

	@Override
	public Post getPostById(int id) {
		Post post = postRepository.findById(id).orElse(null);
		return post;
	}

	@Override
	public void deletePost(Post post) {
		postRepository.delete(post);
		
	}

	@Override
	public Page<Post> getAllPostsOfPage(int start, int limit) {
		Page<Post> posts = postRepository.findAll(PageRequest.of(start, limit));
		return posts;
	}

	@Override
	public List<Post> getSearchedPosts(String searchArg) {
		
		List<Post> posts = postRepository.searchedPosts(searchArg);
		
		return posts;
	}

	@Override
	public List<String> getAllAuthor() {
		List<String> authors = postRepository.getAllAuthors();
		return authors;
	}

	@Override
	public List<Post> getAllPosts() {
		List<Post> posts = postRepository.findAll();
		return posts;
	}

	@Override
	public Page<Post> getSortedPosts(int start, int limit, String column, String basic) {
		Page<Post> posts = null;
		if(basic.equals("ASC"))
		{
			posts = postRepository.findAll(PageRequest.of(start, limit,org.springframework.data.domain.Sort.by(column).ascending()));
		}else if(basic.equals("DESC"))
		{
			posts = postRepository.findAll(PageRequest.of(start, limit,org.springframework.data.domain.Sort.by(column).descending()));
		}
		return posts;
	}

}
