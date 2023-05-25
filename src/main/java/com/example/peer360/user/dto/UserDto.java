package com.example.peer360.user.dto;

import com.example.peer360.user.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String name;
    private String email;
    private String password;
    private String company;
    private String school;
    private String githubUrl;

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .password(password)
                .company(company)
                .school(school)
                .githubUrl(githubUrl)
                .build();
    }
}