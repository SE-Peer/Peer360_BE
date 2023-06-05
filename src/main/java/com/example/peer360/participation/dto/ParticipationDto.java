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

    private Long participationId;
    private String userEmail;
    private String projectName;


    public Participation toEntity(User user, Project project) {
        return Participation.builder()
                .id(participationId)
                .user(user)
                .project(project)
                .build();
    }
}
