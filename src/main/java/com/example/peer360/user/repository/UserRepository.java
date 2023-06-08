package com.example.peer360.user.repository;

import com.example.peer360.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);

    void deleteByEmail(String email);
}