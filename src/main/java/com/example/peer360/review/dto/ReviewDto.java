package com.example.peer360.review.dto;

import com.example.peer360.project.entity.Project;
import com.example.peer360.review.entity.KeywordItem;
import com.example.peer360.review.entity.Review;
import com.example.peer360.review.entity.ScoreItem;
import com.example.peer360.user.entity.User;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {

    private Long reviewId; // ID in DB
    private List<String> keywordItems; // 키워드 리스트
    private Map<String, Integer> reviewItems; // 평가 항목과 점수
    private Long projectId;
    private Long reviewerId;
    private Long revieweeId;

    public Review toEntity(Project project, User reviewer, User reviewee, List<ScoreItem> scoreItems, List<KeywordItem> keywordItems){
        return Review.builder()
                .id(reviewId)
                .scoreItems(scoreItems)
                .keywordItems(keywordItems)
                .project(project)
                .reviewer(reviewer)
                .reviewee(reviewee)
                .build();
    }
}
