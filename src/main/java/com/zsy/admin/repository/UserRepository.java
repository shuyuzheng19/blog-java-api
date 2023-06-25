package com.zsy.admin.repository;

import com.zsy.admin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    Optional<User> findUserByUsernameAndPassword(String username,String password);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}