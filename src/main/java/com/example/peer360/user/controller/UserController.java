package com.example.peer360.user.controller;

import com.example.peer360.review.dto.ReviewDto;
import com.example.peer360.review.service.ReviewService;
import com.example.peer360.user.dto.LoginRequestDto;
import com.example.peer360.user.dto.UserDto;
import com.example.peer360.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Paths;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
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
        List<ReviewDto> reviews = userService.getUserReviews(email);

        Map<String, Integer> wordFrequencies = reviews.stream()
                .flatMap(review -> review.getKeywordItems().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(e -> 1)));

        String filename = "wordcloud.png";

        userService.generateWordCloud(wordFrequencies, filename);

        String bucketName = "unia-github-actions-s3-bucket";
        String s3Filename = "wordclouds/" + email + "/" + filename;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Filename)
                .build();

        s3Client.putObject(putObjectRequest, Paths.get(filename));

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

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}