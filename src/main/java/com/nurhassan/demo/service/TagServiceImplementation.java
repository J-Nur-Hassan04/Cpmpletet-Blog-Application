package com.nurhassan.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nurhassan.demo.entities.Tag;
import com.nurhassan.demo.repository.TagsRepository;

@Service
public class TagServiceImplementation implements TagService {

	@Autowired
	TagsRepository tagRepository;
	
	@Override
	public Tag getTagByName(String tagName) {
		
		Tag tag = tagRepository.findByName(tagName);
		
		return tag;
	}

	@Override
	public List<String> getAlltag() {
		List<String> allTags = tagRepository.getAllTags();
		return allTags;
	}

}
