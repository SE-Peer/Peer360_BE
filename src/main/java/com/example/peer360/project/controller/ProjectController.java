package com.example.peer360.project.controller;

import com.example.peer360.project.dto.ProjectDto;
import com.example.peer360.project.service.ProjectService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @ApiOperation(value = "전체 프로젝트 조회", notes = "저장된 모든 프로젝트 정보를 조회")
    @GetMapping("/list")
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        List<ProjectDto> projects = projectService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @ApiOperation(value = "특정 유저가 참여한 프로젝트 조회")
    @GetMapping("/participated/{email}")
    public ResponseEntity<List<ProjectDto>> getParticipatedProjects(@PathVariable String email) {
        List<ProjectDto> participatedProjects = projectService.getParticipatedProjects(email);
        return new ResponseEntity<>(participatedProjects, HttpStatus.OK);
    }

}