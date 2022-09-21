package com.nurhassan.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nurhassan.demo.entities.User;

public interface UserRepo extends JpaRepository<User, Integer> {

}
