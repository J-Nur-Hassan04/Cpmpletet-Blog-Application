package com.nurhassan.demo.repo;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nurhassan.demo.entities.Posts;

public interface PostsRepo extends JpaRepository<Posts, Integer> {

	@Query(value = "select distinct author from posts", nativeQuery = true)
	public List<String> getAllAuthors();

	public List<Posts> findAllByOrderByPublishedAtDesc();

	public List<Posts> findAllByAuthor(String author);
	
	Page<Posts> findAll(Pageable pageable);

}
