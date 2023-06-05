package com.example.peer360.review.dto;

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
    private String projectName;
    private String reviewerEmail;
    private String revieweeEmail;
}