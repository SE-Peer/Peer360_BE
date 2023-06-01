package com.example.peer360.participation.service;

import com.example.peer360.handler.UserAlreadyParticipatingException;
import com.example.peer360.participation.dto.ParticipationDto;
import com.example.peer360.participation.entity.Participation;
import com.example.peer360.participation.repository.ParticipationRepository;
import com.example.peer360.project.entity.Project;
import com.example.peer360.project.repository.ProjectRepository;
import com.example.peer360.user.entity.User;
import com.example.peer360.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipationService {

    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public ParticipationDto createParticipation(ParticipationDto participationDto) {
        User user = userRepository.findById(participationDto.getUserId()).orElseThrow();
        Project project = projectRepository.findById(participationDto.getProjectId()).orElseThrow();

        List<Participation> allByUserAndProject = participationRepository.findAllByUserAndProject(user, project);
        if (!allByUserAndProject.isEmpty()) {
            throw new UserAlreadyParticipatingException("User is already participating in this project");
        }

        Participation participation = participationDto.toEntity(user, project);
        Participation savedParticipation = participationRepository.save(participation);
        return savedParticipation.toDto();
    }

    public ParticipationDto getParticipation(Long participationId) {
        return participationRepository.findById(participationId).map(Participation::toDto).orElseThrow();
    }

    public void deleteParticipation(Long participationId) {
        participationRepository.deleteById(participationId);
    }
}
