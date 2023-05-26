package com.example.peer360.review.service;

import com.example.peer360.project.entity.Project;
import com.example.peer360.project.repository.ProjectRepository;
import com.example.peer360.review.dto.ReviewDto;
import com.example.peer360.review.entity.Review;
import com.example.peer360.review.repository.ReviewRepository;
import com.example.peer360.user.entity.User;
import com.example.peer360.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    public ReviewDto createReview(ReviewDto reviewDto) {
        User reviewer = userRepository.findById(reviewDto.getReviewerId()).orElseThrow();
        Project project = projectRepository.findById(reviewDto.getProjectId()).orElseThrow();
        Review review = reviewDto.toEntity(project, reviewer);
        Review savedReview = reviewRepository.save(review);
        return savedReview.toDto();
    }

    public ReviewDto getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).map(Review::toDto).orElseThrow();
    }

}
