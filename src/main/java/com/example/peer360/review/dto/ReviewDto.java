package com.example.peer360.review.dto;

import com.example.peer360.review.entity.Review;
import com.example.peer360.review.entity.KeywordItem;
import com.example.peer360.review.entity.ScoreItem;
import lombok.*;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {

    private Long reviewId; // ID in DB
    private List<String> keywordItems; // 키워드 리스트
    private Map<String, Integer> reviewItems; // 평가 항목과 점수
    private String projectName;
    private String reviewerEmail;
    private String revieweeEmail;

    public ReviewDto(Review review) {
        this.reviewId = review.getId();
        this.keywordItems = review.getKeywordItems().stream()
                .map(KeywordItem::getKeywordName)
                .collect(Collectors.toList());
        this.reviewItems = review.getScoreItems().stream()
                .collect(Collectors.toMap(ScoreItem::getItemName, ScoreItem::getScore));
        this.projectName = review.getProject().getName();
        this.revieweeEmail = review.getReviewee().getEmail();
        this.reviewerEmail = review.getReviewer().getEmail();
    }
}