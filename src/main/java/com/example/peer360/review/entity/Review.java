package com.example.peer360.review.entity;

import com.example.peer360.project.entity.Project;
import com.example.peer360.review.dto.ReviewDto;
import com.example.peer360.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<KeywordItem> keywordItems; // 키워드 평가 항목

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ScoreItem> scoreItems; // 수치 평가 항목 (별점)

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @ManyToOne
    @JoinColumn(name = "reviewee_id", nullable = false)
    private User reviewee;

    public ReviewDto toDto() {
        Map<String, Integer> reviewItemsMap = new HashMap<>();
        for (ScoreItem item : scoreItems) {
            reviewItemsMap.put(item.getItemName(), item.getScore());
        }

        List<String> keywordItemsList = new ArrayList<>();
        for (KeywordItem item : keywordItems) {
            keywordItemsList.add(item.getKeywordName());
        }

        return ReviewDto.builder()
                .reviewId(id)
                .reviewItems(reviewItemsMap)
                .keywordItems(keywordItemsList)
                .projectId(project.getId())
                .reviewerId(reviewer.getId())
                .build();
    }
}
