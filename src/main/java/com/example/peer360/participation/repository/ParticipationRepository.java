package com.example.peer360.participation.repository;

import com.example.peer360.participation.entity.Participation;
import com.example.peer360.project.entity.Project;
import com.example.peer360.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    List<Participation> findAllByUserAndProject(User user, Project project);

    List<Participation> findByUser(User user);
}



