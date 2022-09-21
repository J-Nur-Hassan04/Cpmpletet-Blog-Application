package com.nurhassan.demo.repo;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nurhassan.demo.entities.Tags;

public interface TagsRepo extends JpaRepository<Tags, Integer> {

//	@Query(value = "SELECT DISTINCT name FROM tags", nativeQuery = true)
//	ArrayList<String> findAllTags();
//
//	@Query(value = "select name from tags", nativeQuery = true)
//	ArrayList<String> findAllByName();
//
//	@Query(value = "select id from tags where name in (:tags)",nativeQuery = true)
//	ArrayList<Integer> findIdOfTags(@Param("tags") String[] tags);
//	
//	@Query(value = "select t.name from tags t join post_tags pt on pt.tag_id = t.id where post_id = ?1",nativeQuery = true)
//	ArrayList<String> getTagsNames(int post_id);
	
	public Tags findByName(String tagName);

}
