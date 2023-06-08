package com.example.peer360.project.entity;

import com.example.peer360.project.dto.ProjectDto;
import com.example.peer360.review.entity.Review;
import com.example.peer360.review.entity.ReviewStatus;
import com.example.peer360.participation.entity.Participation;
import com.example.peer360.user.dto.UserDto;
import com.example.peer360.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "project")
public class Project {

    @Id
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @ManyToOne
    @JoinColumn(name = "creator_email", referencedColumnName = "email", nullable = false)
    private User creator;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Participation> participations;

    public ProjectDto toDto() {
        ProjectDto projectDto = ProjectDto.builder()
                .name(this.name)
                .url(this.url)
                .status(this.status.name())
                .creatorEmail(this.creator.getEmail())
                .build();

        if(this.participations != null) {
            List<UserDto> participants = this.participations.stream()
                    .map(Participation::getUser)
                    .map(User::toDto)
                    .collect(Collectors.toList());
            projectDto.setParticipants(participants);
        } else {
            projectDto.setParticipants(new ArrayList<>());
        }

        return projectDto;
    }

}
