package com.example.peer360.user.repository;

import com.example.peer360.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long creatorId);

    User findByEmail(String email);
}