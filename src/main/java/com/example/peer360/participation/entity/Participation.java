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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public ParticipationDto toDto() {
        return ParticipationDto.builder()
                .userId(user.getId())
                .projectId(project.getId())
                .build();
    }
}