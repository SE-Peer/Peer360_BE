package com.example.peer360.review.entity;

import com.example.peer360.project.entity.Project;
import com.example.peer360.review.dto.ReviewDto;
import com.example.peer360.user.entity.User;
import lombok.*;

import javax.persistence.*;

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

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    public ReviewDto toDto() {
        return ReviewDto.builder()
                .content(content)
                .projectId(project.getId())
                .reviewerId(reviewer.getId())
                .build();
    }
}
