package com.example.peer360.project.controller;

import com.example.peer360.project.dto.ProjectDto;
import com.example.peer360.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectDto projectDto) {
        ProjectDto createdProject = projectService.createProject(projectDto);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDto> getProject(@PathVariable Long projectId) {
        ProjectDto project = projectService.getProject(projectId);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }
}
