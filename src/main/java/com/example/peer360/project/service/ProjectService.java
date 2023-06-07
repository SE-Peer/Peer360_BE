package com.example.peer360.project.service;

import com.example.peer360.participation.entity.Participation;
import com.example.peer360.participation.repository.ParticipationRepository;
import com.example.peer360.project.dto.ProjectDto;
import com.example.peer360.project.entity.Project;
import com.example.peer360.project.repository.ProjectRepository;
import com.example.peer360.user.entity.User;
import com.example.peer360.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ParticipationRepository participationRepository;

    public ProjectDto createProject(ProjectDto projectDto) {
        User creator = userRepository.findById(projectDto.getCreatorEmail()).orElseThrow();
        Project project = projectDto.toEntity(creator);
        Project savedProject = projectRepository.save(project);
        return savedProject.toDto();
    }

    public ProjectDto getProject(String projectName) {
        return projectRepository.findByName(projectName).map(Project::toDto).orElseThrow();
    }

    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(Project::toDto)
                .collect(Collectors.toList());
    }

    public List<ProjectDto> getParticipatedProjects(String email) {
        User user = userRepository.findById(email).orElseThrow();
        List<Participation> byUser = participationRepository.findByUser(user);
        return byUser.stream()
                .map(Participation::getProject)
                .map(Project::toDto)
                .collect(Collectors.toList());
    }

    public Project getProjectByName(String projectName) {
        return projectRepository.findByName(projectName)
                .orElseThrow(() -> new IllegalArgumentException("No project found with name: " + projectName));
    }

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public void deleteProject(String projectName) {
        Project project = getProjectByName(projectName);
        projectRepository.delete(project);
    }

}