package com.example.peer360.project.service;

import com.example.peer360.project.dto.ProjectDto;
import com.example.peer360.project.entity.Project;
import com.example.peer360.project.repository.ProjectRepository;
import com.example.peer360.user.entity.User;
import com.example.peer360.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectDto createProject(ProjectDto projectDto) {
        User creator = userRepository.findById(projectDto.getCreatorEmail()).orElseThrow();
        Project project = projectDto.toEntity(creator);
        Project savedProject = projectRepository.save(project);
        return savedProject.toDto();
    }

    public ProjectDto getProject(String projectName) {
        return projectRepository.findById(projectName).map(Project::toDto).orElseThrow();
    }
}