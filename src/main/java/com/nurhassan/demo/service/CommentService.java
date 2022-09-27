package com.nurhassan.demo.service;

import com.nurhassan.demo.entities.Comment;

public interface CommentService {
	public Comment getCommentById(int id);
	public void saveComment(Comment comment);
	public void deleteCommentById(int id);

}
