package com.nurhassan.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nurhassan.demo.entities.Comment;

public interface ComentRepository extends JpaRepository<Comment, Integer>{

}
