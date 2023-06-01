package com.example.peer360.review.service;

import com.example.peer360.project.entity.Project;
import com.example.peer360.project.repository.ProjectRepository;
import com.example.peer360.review.dto.ReviewDto;
import com.example.peer360.review.entity.KeywordItem;
import com.example.peer360.review.entity.ScoreItem;
import com.example.peer360.review.entity.Review;
import com.example.peer360.review.repository.ReviewRepository;
import com.example.peer360.user.entity.User;
import com.example.peer360.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    public ReviewDto createReview(ReviewDto reviewDto) {
        User reviewer = userRepository.findById(reviewDto.getReviewerId()).orElseThrow();
        User reviewee = userRepository.findById(reviewDto.getRevieweeId()).orElseThrow();
        Project project = projectRepository.findById(reviewDto.getProjectId()).orElseThrow();

        List<ScoreItem> reviewItems = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : reviewDto.getReviewItems().entrySet()) {
            ScoreItem reviewItem = new ScoreItem();
            reviewItem.setItemName(entry.getKey());
            reviewItem.setScore(entry.getValue());
            reviewItems.add(reviewItem);
        }

        List<KeywordItem> keywordItems = new ArrayList<>();
        for (String keyword : reviewDto.getKeywordItems()) {
            KeywordItem keywordItem = new KeywordItem();
            keywordItem.setKeywordName(keyword);
            keywordItems.add(keywordItem);
        }

        Review review = reviewDto.toEntity(project, reviewer, reviewee, reviewItems, keywordItems);
        Review savedReview = reviewRepository.save(review);
        return savedReview.toDto();
    }

    public ReviewDto getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).map(Review::toDto).orElseThrow();
    }

}
