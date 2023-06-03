package com.example.peer360.user.service;

import com.example.peer360.review.dto.ReviewDto;
import com.example.peer360.review.entity.Review;
import com.example.peer360.review.repository.ReviewRepository;
import com.example.peer360.user.dto.UserDto;
import com.example.peer360.user.entity.User;
import com.example.peer360.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    public UserDto createUser(UserDto userDto) {
        User user = userDto.toEntity();
        User savedUser = userRepository.save(user);
        return savedUser.toDto();
    }
    public UserDto getUser(Long userId) {
        return userRepository.findById(userId).map(User::toDto).orElseThrow();
    }

    public List<ReviewDto> getUserReviews(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        List<Review> reviews = reviewRepository.findByReviewee(user);
        return reviews.stream().map(Review::toDto).collect(Collectors.toList());
    }

    public UserDto validateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && password.equals(user.getPassword())) { // basic password checking. In real applications, passwords should be hashed!
            return user.toDto();
        }
        return null;
    }

}
