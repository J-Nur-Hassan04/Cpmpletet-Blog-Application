package com.nurhassan.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nurhassan.demo.entities.Comment;
import com.nurhassan.demo.repository.ComentsRepository;

@Service
public class CommentServiceImplementation implements CommentService {

	@Autowired
	private ComentsRepository commentRepository;
	@Override
	public Comment getCommentById(int id) {
		Comment comment = commentRepository.findById(id).orElse(null);
		return comment;
	}
	@Override
	public void saveComment(Comment comment) {
		commentRepository.save(comment);
		
	}
	@Override
	public void deleteCommentById(int id) {
		commentRepository.deleteById(id);
		
	}

}
