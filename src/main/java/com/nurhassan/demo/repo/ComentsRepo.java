package com.nurhassan.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nurhassan.demo.entities.Comments;

public interface ComentsRepo extends JpaRepository<Comments, Integer>{

}
