package com.example.peer360.user.controller;

import com.example.peer360.review.dto.ReviewDto;
import com.example.peer360.review.service.ReviewService;
import com.example.peer360.user.dto.LoginRequestDto;
import com.example.peer360.user.dto.UserDto;
import com.example.peer360.user.service.UserService;
import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.palette.LinearGradientColorPalette;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.S3Client;

import javax.servlet.http.HttpSession;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final S3Client s3Client;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        UserDto createdUser = userService.createUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }


    @GetMapping("/{email}")
    public ResponseEntity<UserDto> getUser(@PathVariable String email) {
        UserDto user = userService.getUser(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{email}/reviews")
    public ResponseEntity<List<ReviewDto>> getUserReviews(@PathVariable String email) {
        List<ReviewDto> reviews = userService.getUserReviews(email);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto, HttpSession session) {
        UserDto user = userService.validateUser(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        if (user != null) {
            session.setAttribute("user", user);
            return new ResponseEntity<>("login success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("login fail", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.removeAttribute("user");
        return new ResponseEntity<>("logout success", HttpStatus.OK);
    }

    @GetMapping("/{email}/average-scores")
    public ResponseEntity<Map<String, Integer>> getAverageScoresByItemName(@PathVariable String email) {
        Map<String, Integer> averageScores = reviewService.getAverageScoresByItemName(email);
        return new ResponseEntity<>(averageScores, HttpStatus.OK);
    }


    @GetMapping("/{email}/reviews/wordcloud")
    public ResponseEntity<String> getUserReviewsWordCloud(@PathVariable String email) {

        String filename = "wordcloud.png";
        String bucketName = "unia-github-actions-s3-bucket";
        String s3Filename = "wordclouds/" + email + "/" + filename;

        String fileUrl = s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(s3Filename)).toExternalForm();
        System.out.println(fileUrl);
        return new ResponseEntity<>(fileUrl, HttpStatus.OK);
    }

    @ApiOperation(
            value = "전체 회원 조회 (실제 기능 X)")
    @GetMapping("/list")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    private void generateWordCloud(Map<String, Integer> wordFrequencies, String filename) {
        Dimension dimension = new Dimension(600, 600);
        WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setColorPalette(new LinearGradientColorPalette(Color.WHITE, Color.BLUE, Color.GREEN, 30, 30));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 40));

        List<WordFrequency> wordFrequencyList = wordFrequencies.entrySet().stream()
                .map(entry -> new WordFrequency(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        wordCloud.build(wordFrequencyList);
        wordCloud.writeToFile(filename);
    }
}