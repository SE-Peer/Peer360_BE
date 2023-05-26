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

    private Long projectId;
    private String name;
    private String url;
    private String status;
    private Long creatorId;

    public Project toEntity(User creator){
        return Project.builder()
                .id(projectId)
                .name(name)
                .url(url)
                .status(ReviewStatus.valueOf(status))
                .creator(creator)
                .build();
    }

}
