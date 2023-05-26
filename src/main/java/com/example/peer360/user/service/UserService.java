package com.example.peer360.user.service;


import com.example.peer360.user.dto.UserDto;
import com.example.peer360.user.entity.User;
import com.example.peer360.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    public UserDto createUser(UserDto userDto) {
        User user = userDto.toEntity();
        User savedUser = userRepository.save(user);
        return savedUser.toDto();
    }
    public UserDto getUser(Long userId) {
        return userRepository.findById(userId).map(User::toDto).orElseThrow();
    }
}
