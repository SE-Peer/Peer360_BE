package com.example.peer360.review.dto;

import com.example.peer360.project.entity.Project;
import com.example.peer360.review.entity.Review;
import com.example.peer360.user.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {

    private Long reviewId; // ID in DB
    private String content;
    private Long projectId;
    private Long reviewerId;

    public Review toEntity(Project project, User reviewer){
        return Review.builder()
                .id(reviewId)
                .content(content)
                .project(project)
                .reviewer(reviewer)
                .build();
    }
}
