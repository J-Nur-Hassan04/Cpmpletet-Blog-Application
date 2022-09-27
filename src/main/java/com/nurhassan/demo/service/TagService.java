package com.nurhassan.demo.service;

import java.util.List;

import com.nurhassan.demo.entities.Tag;

public interface TagService {
	public Tag getTagByName(String tagName);
	public List<String> getAlltag();
}
