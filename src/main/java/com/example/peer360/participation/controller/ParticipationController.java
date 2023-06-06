package com.example.peer360.participation.controller;

import com.example.peer360.participation.dto.ParticipationDto;
import com.example.peer360.participation.service.ParticipationService;
import com.example.peer360.project.dto.ProjectDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/participations")
public class ParticipationController {

    private final ParticipationService participationService;

    @PostMapping
    public ResponseEntity<ParticipationDto> createParticipation(@RequestBody ParticipationDto participationDto) {
        ParticipationDto createdParticipation = participationService.createParticipation(participationDto);
        return new ResponseEntity<>(createdParticipation, HttpStatus.CREATED);
    }

    @GetMapping("/{participationId}")
    public ResponseEntity<ParticipationDto> getParticipation(@PathVariable Long participationId) {
        ParticipationDto participation = participationService.getParticipation(participationId);
        return new ResponseEntity<>(participation, HttpStatus.OK);
    }

    @DeleteMapping("/{participationId}")
    public ResponseEntity<Void> deleteParticipation(@PathVariable Long participationId) {
        participationService.deleteParticipation(participationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "전체 참여 조회", notes = "회원이 어느 프로젝트에 참여했는지 모두 조회")
    @GetMapping("/list")
    public ResponseEntity<List<ParticipationDto>> getAllParticipations() {
        List<ParticipationDto> participations = participationService.getAllParticipations();
        return new ResponseEntity<>(participations, HttpStatus.OK);
    }
}