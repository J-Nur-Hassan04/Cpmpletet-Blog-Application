package com.nurhassan.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nurhassan.demo.entities.Post;

public interface PostsRepository extends JpaRepository<Post, Integer> {

	@Query(value = "select distinct author from posts", nativeQuery = true)
	public List<String> getAllAuthors();

	public List<Post> findAllByOrderByPublishedAtDesc();

	public List<Post> findAllByAuthor(String author);

	public List<Post> findAllByTagsName(String name);

	@Query("SELECT post from Post post join post.tags tag where upper (tag.name) like concat('%',(?1),'%') or upper (post.title) like concat('%',(?1),'%') or upper (post.content) like concat('%',(?1),'%') or upper (post.author) like concat('%',(?1),'%') group by post.id")
	List<Post> searchedPosts(String searchArg);
	//	Page<Posts> searchedPosts(String searchArg,Pageable pageable);
	
	@Query("SELECT post from Post post join post.tags tag where (tag.name) in (:tags)")
	List<Post> findAllByTagsArray(String[] tags);
	
	@Query("select post from Post post where (post.author) in (:authors)")
	List<Post> findAllByAuthorArray(String[] authors);

	Page<Post> findAll(Pageable pageable);

}
