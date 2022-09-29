package com.nurhassan.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.nurhassan.demo.entities.Post;
import com.nurhassan.demo.repository.PostRepository;

@Service
public class PostServiceImplementation implements PostService {

	@Autowired
	private PostRepository postRepository;

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
	public Page<Post> getSearchedPosts(String searchArg, int start, int limit) {

		Page<Post> posts = postRepository.searchedPosts(searchArg, PageRequest.of(start, limit));

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
		if (basic.equals("ASC")) {
			posts = postRepository
					.findAll(PageRequest.of(start, limit, org.springframework.data.domain.Sort.by(column).ascending()));
		} else if (basic.equals("DESC")) {
			posts = postRepository.findAll(
					PageRequest.of(start, limit, org.springframework.data.domain.Sort.by(column).descending()));
		}
		return posts;
	}

	@Override
	public Page<Post> getSearchedPostsWithTagAndAuthor(int start, int limit, String[] tags, String[] authors) {
		Page<Post> posts = postRepository.searchedPostsWithTagAuthors(tags, authors, PageRequest.of(start, limit));
		return posts;
	}

	@Override
	public Page<Post> getSearchedPostsWithSearchArgTagAuthors(String searchArg, String[] tags, String[] authors,
			int start, int limit) {
		Page<Post> posts = postRepository.searchedPostsWithSearchArgTagAuthors(searchArg, tags, authors,
				PageRequest.of(start, limit));
		return posts;
	}

	@Override
	public Page<Post> getSearchedPostWithSearchArgAndTags(String searchArg, String[] tags, int start, int limit) {
		Page<Post> posts = postRepository.seacchedPostSearchArgAndTags(searchArg, tags, PageRequest.of(start, limit));
		return posts;
	}

	@Override
	public Page<Post> getAllPostsByAuthor(String[] author, int start, int limit) {
		Page<Post> posts = postRepository.searchedPostsByAuthors(author, PageRequest.of(start, limit));
		return posts;
	}

	@Override
	public Page<Post> getSearchedPostsByTags(String[] tags, int start, int limit) {
		Page<Post> posts = postRepository.searchedPostsByTags(tags, PageRequest.of(start, limit));
		return posts;
	}

	@Override
	public Page<Post> getSearchedPostsBySearchArgAndAuthors(String searchArg, String[] authors, int start, int limit) {
		Page<Post> posts = postRepository.searchedPostsBySearchArgAndAuthors(searchArg, authors,
				PageRequest.of(start, limit));
		return posts;
	}

}
