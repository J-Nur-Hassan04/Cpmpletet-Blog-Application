package com.nurhassan.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.nurhassan.demo.entities.Tag;

public interface TagRepository extends JpaRepository<Tag, Integer> {
	
	public Tag findByName(String tagName);
	
	@Query(value = "select name from tags" , nativeQuery = true)
	public List<String> getAllTags();

}
