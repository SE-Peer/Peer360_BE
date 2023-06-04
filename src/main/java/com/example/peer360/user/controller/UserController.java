package com.example.peer360.user.controller;

import com.example.peer360.review.dto.ReviewDto;
import com.example.peer360.review.service.ReviewService;
import com.example.peer360.user.dto.UserDto;
import com.example.peer360.user.service.UserService;
import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.palette.LinearGradientColorPalette;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final S3Client s3Client;

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto){
        return userService.createUser(userDto);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        return userService.getUser(userId);
    }


    @PostMapping("/login")
    public String login(@RequestBody UserDto userDto, HttpSession session) {
        UserDto user = userService.validateUser(userDto.getEmail(), userDto.getPassword());
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/";
        } else {
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }

    @GetMapping("/{userId}/average-scores")
    @ResponseBody
    public Map<String, Double> getAverageScoresByItemName(@PathVariable Long userId) {
        return reviewService.getAverageScoresByItemName(userId);
    }


    @GetMapping("/{userId}/reviews/wordcloud")
    public ResponseEntity<String> getUserReviewsWordCloud(@PathVariable Long userId) throws IOException {
        List<ReviewDto> reviews = userService.getUserReviews(userId);

        Map<String, Integer> wordFrequencies = reviews.stream()
                .flatMap(review -> review.getKeywordItems().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(e -> 1)));

        String filename = "wordcloud.png";
        generateWordCloud(wordFrequencies, filename);

        String bucketName = "unia-github-actions-s3-bucket"; // TODO: replace with your S3 bucket name
        String s3Filename = "wordclouds/" + userId + "/" + filename; // path in S3 bucket

        // Upload file to S3
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Filename)
                .build();

        s3Client.putObject(putObjectRequest, Paths.get(filename));

        // Get the file URL
        String fileUrl = s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(s3Filename)).toExternalForm();

        return new ResponseEntity<>(fileUrl, HttpStatus.OK);
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
