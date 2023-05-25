package com.example.peer360.participation.controller;

import com.example.peer360.participation.dto.ParticipationDto;
import com.example.peer360.participation.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/participations")
public class ParticipationController {

    private final ParticipationService participationService;

    @PostMapping
    public ParticipationDto createParticipation(@RequestBody ParticipationDto participationDto) {
        return participationService.createParticipation(participationDto);
    }

    @GetMapping("/{id}")
    public ParticipationDto getParticipation(@PathVariable Long id) {
        return participationService.getParticipation(id);
    }


}
