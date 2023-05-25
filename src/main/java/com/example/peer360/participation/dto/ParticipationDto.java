package com.example.peer360.participation.dto;

import com.example.peer360.participation.entity.Participation;
import com.example.peer360.project.entity.Project;
import com.example.peer360.user.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipationDto {

    private Long userId;
    private Long projectId;

    public Participation toEntity(User user, Project project) {
        return Participation.builder()
                .user(user)
                .project(project)
                .build();
    }
}
