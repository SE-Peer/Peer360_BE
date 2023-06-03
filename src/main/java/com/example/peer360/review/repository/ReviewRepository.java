package com.example.peer360.review.repository;

import com.example.peer360.review.entity.Review;
import com.example.peer360.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByReviewee(User user);
}
