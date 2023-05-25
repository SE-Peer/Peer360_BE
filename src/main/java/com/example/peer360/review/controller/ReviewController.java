package com.example.peer360.review.controller;

import com.example.peer360.review.dto.ReviewDto;
import com.example.peer360.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ReviewDto createReview(@RequestBody ReviewDto reviewDto) {
        return reviewService.createReview(reviewDto);
    }

    @GetMapping("/{id}")
    public ReviewDto getReview(@PathVariable Long id) {
        return reviewService.getReview(id);
    }
}
