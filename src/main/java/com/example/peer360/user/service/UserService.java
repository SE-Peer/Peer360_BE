package com.example.peer360.user.service;

import com.example.peer360.review.dto.ReviewDto;
import com.example.peer360.review.repository.ReviewRepository;
import com.example.peer360.user.dto.UserDto;
import com.example.peer360.user.entity.User;
import com.example.peer360.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final S3Client s3Client;
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

    public void generateWordCloud(String email) {
        String currentDir = System.getProperty("user.dir");
        String scriptPath = currentDir + "/scripts/result.py";
        String filename = "wordcloud.png";
        String s3Filename = "wordclouds/" + email + "/" + filename;

        List<String> cmdList = new ArrayList<>();
        cmdList.add("/usr/bin/python3");
        cmdList.add(scriptPath);
        cmdList.add(email);

        ProcessBuilder pb = new ProcessBuilder(cmdList);
        try {
            Process p = pb.start();
            int exitCode = p.waitFor();

            // Read output
            BufferedReader outputReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String outputLine;
            while ((outputLine = outputReader.readLine()) != null) {
                System.out.println("Output: " + outputLine);
            }

            // Read error
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println("Error: " + errorLine);
            }

            // Python error handling
            if (exitCode != 0) {
                throw new RuntimeException("Python script execution failed with exit code: " + exitCode);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error running python script", e);
        }
        uploadFileToS3(s3Filename, filename);
    }

    public void uploadFileToS3(String s3Filename, String localFilename) {
        String bucketName = "unia-github-actions-s3-bucket";
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Filename)
                .build();

        try {
            s3Client.putObject(putObjectRequest, Paths.get(localFilename));
        } catch (S3Exception e) {
            throw new RuntimeException("Error uploading file to S3", e);
        }
    }

}
