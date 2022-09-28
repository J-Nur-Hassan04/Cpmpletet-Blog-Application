package com.nurhassan.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nurhassan.demo.entities.Post;

public interface PostsRepository extends JpaRepository<Post, Integer> {

	@Query(value = "select distinct author from posts", nativeQuery = true)
	public List<String> getAllAuthors();

	public List<Post> findAllByOrderByPublishedAtDesc();

	public List<Post> findAllByAuthor(String author);

	public List<Post> findAllByTagsName(String name);

	@Query("SELECT distinct post from Post post "
			+ "join "
			+ "post.tags tag "
			+ "where "
			+ "upper (tag.name) "
			+ "like concat('%',(?1),'%') "
			+ "or "
			+ "upper (post.title) "
			+ "like concat('%',(?1),'%') "
			+ "or "
			+ "upper (post.content) "
			+ "like concat('%',(?1),'%') "
			+ "or "
			+ "upper (post.author) "
			+ "like concat('%',(?1),'%') "
			+ "group by post.id")
	Page<Post> searchedPosts(String searchArg, Pageable pageable);
	
	
	@Query("SELECT distinct post "
			+ "from "
			+ "Post post "
			+ "join "
			+ "post.tags tag "
			+ "where (tag.name) in (:tags) and "
			+ "upper (post.author) "
			+ "like concat('%',(:arg),'%') "
			+ "or "
			+ "upper (post.title) "
			+ "like concat('%',(:arg),'%') "
			+ "or "
			+ "upper (post.content) "
			+ "like concat('%',(:arg),'%')")
	Page<Post> seacchedPostSearchArgAndTags(@Param("arg") String searchArgs ,@Param("tags") String[] tags,Pageable pageable);
	
	@Query("SELECT distinct post "
			+ "from "
			+ "Post post "
			+ "join "
			+ "post.tags tag "
			+ "where ("
			+ "tag.name) in (:tags) "
			+ "and "
			+ "(post.author) "
			+ "in "
			+ "(:authors)")
	Page<Post> searchedPostsWithTagAuthors(String[] tags, String[] authors,Pageable pageable);
	
	
	
	@Query("SELECT distinct post "
			+ "from "
			+ "Post post "
			+ "join "
			+ "post.tags tag "
			+ "where "
			+ "upper (tag.name) "
			+ "like concat('%',(:arg),'%') "
			+ "or "
			+ "upper (post.title) "
			+ "like concat('%',(:arg),'%') "
			+ "or "
			+ "upper (post.content) like concat('%',(:arg),'%') "
			+ "and  "
			+ "(tag.name) in (:tags) "
			+ "and "
			+ "(post.author) in (:authors)")
	Page<Post> searchedPostsWithSearchArgTagAuthors(@Param("arg") String searchArgs ,
			@Param("tags") String[] tags,@Param("authors") String[] authors,Pageable pageable);
	

	@Query("SELECT post from Post post join post.tags tag where (tag.name) = (:tags)")
	List<Post> findAllByTagsArray(@Param("tags") String[] tags);

	@Query("select post from Post post where (post.author) in (:authors)")
	List<Post> findAllByAuthorArray(String[] authors);

	Page<Post> findAll(Pageable pageable);
}
