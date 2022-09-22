package com.nurhassan.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.nurhassan.demo.entities.Tags;

public interface TagsRepo extends JpaRepository<Tags, Integer> {
	
	public Tags findByName(String tagName);
	
	@Query(value = "select name from tags" , nativeQuery = true)
	public List<String> getAllTags();

}
