package com.example.peer360.participation.entity;

import com.example.peer360.participation.dto.ParticipationDto;
import com.example.peer360.project.entity.Project;
import com.example.peer360.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "participation")
public class Participation {

    @Id
    @Column(nullable = false, unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_email", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_name", nullable = false)
    private Project project;

    public ParticipationDto toDto() {
        return ParticipationDto.builder()
                .participationId(id)
                .userEmail(user.getEmail())
                .projectName(project.getName())
                .build();
    }
}