package com.example.peer360.review.service;

import com.example.peer360.project.entity.Project;
import com.example.peer360.project.repository.ProjectRepository;
import com.example.peer360.review.dto.ReviewDto;
import com.example.peer360.review.entity.KeywordItem;
import com.example.peer360.review.entity.ReviewItem;
import com.example.peer360.review.entity.ScoreItem;
import com.example.peer360.review.entity.Review;
import com.example.peer360.review.repository.ReviewRepository;
import com.example.peer360.user.entity.User;
import com.example.peer360.user.repository.UserRepository;
import com.example.peer360.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final UserService userService;

    @Transactional
    public ReviewDto createReview(ReviewDto reviewDto) {
        User reviewer = userRepository.findByEmail(reviewDto.getReviewerEmail());
        User reviewee = userRepository.findByEmail(reviewDto.getRevieweeEmail());
        Project project = projectRepository.findByName(reviewDto.getProjectName())
                .orElseThrow(() -> new NoSuchElementException("Project with name " + reviewDto.getProjectName() + " not found"));

        List<KeywordItem> keywordItems = new ArrayList<>();
        for (String keyword : reviewDto.getKeywordItems()) {
            KeywordItem keywordItem = new KeywordItem();
            keywordItem.setKeywordName(keyword);
            keywordItems.add(keywordItem);
        }
        List<ScoreItem> scoreItems = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : reviewDto.getReviewItems().entrySet()) {
            if(entry.getValue() < 1 || entry.getValue() > 5) {
                throw new IllegalArgumentException("Score should be between 1 and 5. Received: " + entry.getValue());
            }
            ScoreItem scoreItem = new ScoreItem();
            scoreItem.setItemName(entry.getKey());
            scoreItem.setScore(entry.getValue());
            scoreItems.add(scoreItem);
        }
        Review review = Review.builder()
                .reviewer(reviewer)
                .reviewee(reviewee)
                .project(project)
                .keywordItems(keywordItems)
                .scoreItems(scoreItems)
                .build();

        for (KeywordItem keywordItem : keywordItems) {
            keywordItem.setReview(review);
        }
        for (ScoreItem scoreItem : scoreItems) {
            scoreItem.setReview(review);
        }

        Review savedReview = reviewRepository.save(review);
        return savedReview.toDto();
    }

    public ReviewDto getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).map(Review::toDto).orElseThrow();
    }


    public Map<String, Double> getAverageScoresByItemName(String email) {
        User user = userService.findUserByEmail(email);
        List<Review> reviews = reviewRepository.findByRevieweeEmail(user.getEmail());

        Map<String, List<ScoreItem>> scoresByItemName = new HashMap<>();
        for (Review review : reviews) {
            for (ScoreItem scoreItem : review.getScoreItems()) {
                scoresByItemName.computeIfAbsent(scoreItem.getItemName(), k -> new ArrayList<>()).add(scoreItem);
            }
        }

        Map<String, Double> averageScoresByItemName = new HashMap<>();
        for (Map.Entry<String, List<ScoreItem>> entry : scoresByItemName.entrySet()) {
            double average = entry.getValue().stream().mapToInt(ScoreItem::getScore).average().orElse(0.0);
            averageScoresByItemName.put(entry.getKey(), average);
        }

        return averageScoresByItemName;
    }

    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(Review::toDto)
                .collect(Collectors.toList());
    }
}
