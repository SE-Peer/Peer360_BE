package com.example.peer360.user.service;

import com.example.peer360.participation.service.ParticipationService;
import com.example.peer360.review.dto.ReviewDto;
import com.example.peer360.review.entity.Review;
import com.example.peer360.review.repository.ReviewRepository;
import com.example.peer360.review.service.ReviewService;
import com.example.peer360.user.controller.RandomAngleGenerator;
import com.example.peer360.user.dto.UserDto;
import com.example.peer360.user.entity.User;
import com.example.peer360.user.repository.UserRepository;
import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.palette.ColorPalette;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ParticipationService participationService;
    public UserDto createUser(UserDto userDto) {
        User user = userDto.toEntity();
        User savedUser = userRepository.save(user);
        return savedUser.toDto();
    }
    public UserDto getUser(String email) {
        User user = userRepository.findByEmail(email);
        return user.toDto();
    }

    public List<ReviewDto> getUserReviews(String email) {
        User user = userRepository.findByEmail(email);

        return reviewRepository.findByReviewee(user).stream()
                .map(ReviewDto::new)
                .collect(Collectors.toList());
    }

    public UserDto validateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && password.equals(user.getPassword())) { // basic password checking. In real applications, passwords should be hashed!
            return user.toDto();
        }
        return null;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(User::toDto)
                .collect(Collectors.toList());
    }

    public void generateWordCloud(Map<String, Integer> wordFrequencies, String filename) {
        Dimension dimension = new Dimension(300, 300);
        WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);

        Font font = new Font("Malgun Gothic", Font.PLAIN, 10);
        wordCloud.setKumoFont(new KumoFont(font));

        wordCloud.setBackground(new CircleBackground(300));

        // 더 다양한 색상 팔레트를 사용
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1),
                new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40E9F1), new Color(0xFFFFFF)));
        // 단어의 빈도에 따라 폰트 크기를 조절
        wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
        wordCloud.setAngleGenerator(new RandomAngleGenerator(0, 90));

        // Map을 List<WordFrequency>로 변환
        List<WordFrequency> wordFrequencyList = wordFrequencies.entrySet().stream()
                .map(entry -> new WordFrequency(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        wordCloud.build(wordFrequencyList);
        wordCloud.writeToFile(filename);
    }

    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email);

        participationService.deleteAllByUser(user);

        userRepository.delete(user);
    }
}