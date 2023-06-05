package com.example.peer360.project.controller;

import com.example.peer360.project.dto.ProjectDto;
import com.example.peer360.project.service.ProjectService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @ApiOperation(
            value = "프로젝트 등록"
            , notes = "name: 프로젝트 이름, status: REVIEW_POSSIBLE 아니면 REVIEW_COMPLETED, url: 프로젝트 깃허브 주소")
    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectDto projectDto) {
        ProjectDto createdProject = projectService.createProject(projectDto);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @GetMapping("/{projectName}")
    public ResponseEntity<ProjectDto> getProject(@PathVariable String projectName) {
        ProjectDto project = projectService.getProject(projectName);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }
}