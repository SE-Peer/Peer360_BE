package com.example.peer360.project.dto;

import com.example.peer360.project.entity.Project;
import com.example.peer360.review.entity.ReviewStatus;
import com.example.peer360.user.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDto {

    private String name;
    private String url;
    private String status;
    private String creatorEmail;

    public Project toEntity(User creator){
        return Project.builder()
                .name(name)
                .url(url)
                .status(ReviewStatus.valueOf(status))
                .creator(creator)
                .build();
    }
}