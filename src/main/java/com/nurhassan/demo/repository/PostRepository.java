package com.nurhassan.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nurhassan.demo.entities.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {

	@Query("select distinct post from Post post where is_published = true")
	Page<Post> findAll(Pageable pageable);

	@Query(value = "select distinct author from posts", nativeQuery = true)
	public List<String> getAllAuthors();

	public List<Post> findAllByOrderByPublishedAtDesc();

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
			+ "and "
			+ "is_published = true "
			+ "group by post.id")
	Page<Post> searchedPosts(String searchArg, Pageable pageable);
	
	
	@Query("SELECT distinct post "
			+ "from "
			+ "Post post "
			+ "join "
			+ "post.tags tag "
			+ "where (tag.name) in (:tags) "
			+ "and "
			+ "upper (post.author) "
			+ "like concat('%',(:arg),'%') "
			+ "or "
			+ "upper (post.title) "
			+ "like concat('%',(:arg),'%') "
			+ "or "
			+ "upper (post.content) "
			+ "like concat('%',(:arg),'%') "
			+ "and "
			+ "is_published = true")
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
			+ "(:authors) "
			+ "and "
			+ "is_published = true")
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
			+ "(post.author) in (:authors) "
			+ "and "
			+ "is_published = true")
	Page<Post> searchedPostsWithSearchArgTagAuthors(@Param("arg") String searchArgs ,
			@Param("tags") String[] tags,@Param("authors") String[] authors,Pageable pageable);
	

	@Query("select distinct post "
			+ "from "
			+ "Post post "
			+ "where "
			+ "post.author in (:authors) "
			+ "and "
			+ "is_published = true")
	Page<Post> searchedPostsByAuthors(@Param("authors") String[] authors, Pageable pageable);
	
	@Query("select distinct post "
			+ "from "
			+ "Post post "
			+ "join "
			+ "post.tags tag "
			+ "where "
			+ "tag.name in (:tags) "
			+ "and "
			+ "is_published = true")
	Page<Post> searchedPostsByTags(@Param("tags") String[] tags, Pageable pageable);
	
	@Query("select distinct post "
			+ "from "
			+ "Post post "
			+ "where "
			+ "(post.author) in (:authors) and "
			+ "upper (post.title) like concat('%',(:arg),'%') "
			+ "or "
			+ "upper (post.content) like concat('%',(:arg),'%') "
			+ "and "
			+ "is_published = true")
	Page<Post> searchedPostsBySearchArgAndAuthors(@Param("arg")String searchArg,@Param("authors") String[] authors, Pageable pageable);
	
	@Query("SELECT post "
			+ "from "
			+ "Post post "
			+ "join "
			+ "post.tags tag "
			+ "where (tag.name) = (:tags) "
			+ "and "
			+ "is_published = true")
	List<Post> findAllByTagsArray(@Param("tags") String[] tags);

	@Query("select post "
			+ "from "
			+ "Post post "
			+ "where (post.author) in (:authors) "
			+ "and "
			+ "is_published = true")
	List<Post> findAllByAuthorArray(String[] authors);
	
	@Query("select post from Post post where user_id = :userId and is_published = false")
	List<Post> findDraftsByUId(@Param("userId") int userId);

}
