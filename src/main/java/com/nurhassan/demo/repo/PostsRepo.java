package com.nurhassan.demo.repo;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nurhassan.demo.entities.Posts;

public interface PostsRepo extends JpaRepository<Posts, Integer> {

	ArrayList<Posts> findByTitle(String title);

	@Query(value = "SELECT * FROM posts WHERE title LIKE %?1% ", nativeQuery = true)
	ArrayList<Posts> findByMatchingTitle(String subTitle);
	
	@Query(value = "SELECT * FROM posts WHERE id IN (SELECT id FROM tags WHERE name LIKE %?1%)", nativeQuery = true)
	ArrayList<Posts> findPostByTag(String tag);
	
//	@Query(value = "select id from posts where title = ?1", nativeQuery = true)
//	ArrayList<Integer> findPostId(String title);
	
//	@Query( value = "select * from posts" , nativeQuery = true)
//	public ResultSet findResult();
	

}
