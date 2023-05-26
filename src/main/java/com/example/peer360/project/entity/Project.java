package com.example.peer360.project.entity;

import com.example.peer360.project.dto.ProjectDto;
import com.example.peer360.review.entity.Review;
import com.example.peer360.review.entity.ReviewStatus;
import com.example.peer360.participation.entity.Participation;
import com.example.peer360.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status; //ReviewStatus is an enum with values REVIEW_POSSIBLE, REVIEW_COMPLETED

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @OneToMany(mappedBy = "project")
    private Set<Review> reviews;

    @OneToMany(mappedBy = "project")
    private Set<Participation> participations;

    public ProjectDto toDto() {
        return ProjectDto.builder()
                .name(name)
                .url(url)
                .status(status.toString())
                .creatorId(creator.getId())
                .build();
    }
}