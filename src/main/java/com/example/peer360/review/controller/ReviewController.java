package com.example.peer360.review.controller;

import com.example.peer360.review.dto.ReviewDto;
import com.example.peer360.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewDto reviewDto) {
        ReviewDto createdReview = reviewService.createReview(reviewDto);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> getReview(@PathVariable Long reviewId) {
        ReviewDto review = reviewService.getReview(reviewId);
        return new ResponseEntity<>(review, HttpStatus.OK);
    }
}
