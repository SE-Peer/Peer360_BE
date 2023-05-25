package com.example.peer360.user.entity;

import com.example.peer360.user.dto.UserDto;
import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String company;

    private String school;

    @Column(name = "github_url")
    private String githubUrl;

    public UserDto toDto() {
        return UserDto.builder()
                .name(name)
                .email(email)
                .password(password)
                .company(company)
                .school(school)
                .githubUrl(githubUrl)
                .build();
    }
}
