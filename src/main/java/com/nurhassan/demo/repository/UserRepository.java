package com.nurhassan.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nurhassan.demo.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
