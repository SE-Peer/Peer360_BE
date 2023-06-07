package com.example.peer360.project.controller;

import com.example.peer360.project.dto.ProjectDto;
import com.example.peer360.project.entity.Project;
import com.example.peer360.project.service.ProjectService;
import com.example.peer360.review.entity.ReviewStatus;
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

    // REVIEW_POSSIBLE로 상태를 변경하는 엔드포인트
    @PatchMapping("/{projectName}/status/review-possible")
    public ResponseEntity<ProjectDto> changeStatusToReviewPossible(@PathVariable String projectName) {
        Project project = projectService.getProjectByName(projectName);
        project.setStatus(ReviewStatus.REVIEW_POSSIBLE);
        Project updatedProject = projectService.saveProject(project);
        return new ResponseEntity<>(updatedProject.toDto(), HttpStatus.OK);
    }

    // REVIEW_COMPLETED로 상태를 변경하는 엔드포인트
    @PatchMapping("/{projectName}/status/review-completed")
    public ResponseEntity<ProjectDto> changeStatusToReviewCompleted(@PathVariable String projectName) {
        Project project = projectService.getProjectByName(projectName);
        project.setStatus(ReviewStatus.REVIEW_COMPLETED);
        Project updatedProject = projectService.saveProject(project);
        return new ResponseEntity<>(updatedProject.toDto(), HttpStatus.OK);
    }

    @ApiOperation(value = "프로젝트 삭제", notes = "프로젝트 이름에 해당하는 프로젝트를 삭제합니다.")
    @DeleteMapping("/{projectName}")
    public ResponseEntity<Void> deleteProject(@PathVariable String projectName) {
        projectService.deleteProject(projectName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}