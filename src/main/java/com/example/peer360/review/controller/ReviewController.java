package com.example.peer360.review.controller;

import com.example.peer360.review.dto.ReviewDto;
import com.example.peer360.review.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @ApiOperation(
            value = "리뷰 작성"
    , notes = "[ReviewDto] reviewId : 임의 숫자 (ex. 1, 2, 3 ..), keywordItems : 친절, 적극적, 열정있음, 답장안함 등," +
            "reviewItems: https://docs.google.com/document/d/1Yy9PjSuH3KyWTDSNbevhs6tSqer74tE73LG1Pe3rRl8/edit?usp=sharing")
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewDto reviewDto) {
        ReviewDto createdReview = reviewService.createReview(reviewDto);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @ApiOperation(
            value = "실제 기능 X")
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> getReview(@PathVariable Long reviewId) {
        ReviewDto review = reviewService.getReview(reviewId);
        return new ResponseEntity<>(review, HttpStatus.OK);
    }
}
