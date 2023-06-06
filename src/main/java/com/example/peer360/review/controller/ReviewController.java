package com.example.peer360.review.controller;

import com.example.peer360.review.dto.ReviewDto;
import com.example.peer360.review.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @ApiOperation(
            value = "리뷰 작성"
    , notes = "[ReviewDto] reviewId : 임의 숫자 (ex. 1, 2, 3 ..), keywordItems : 친절, 적극적, 열정있음, 답장안함 등," +
            "reviewItems:     \"업무에 필요한 지식이 풍부한가?\": 5,\n" +
            "    \"자신의 능력을 바탕으로 주어진 업무를 잘 수행하는가?\": 4,\n" +
            "    \"업무 전반적인 진행 상황을 이해하고, 계획에 따라 실천하는 능력이 탁월한가?\": 4,\n" +
            "    \"개인의 창의성을 토대로, 업무의 효율 및 효과를 극대화하는 능력이 탁월한가?\": 5,\n" +
            "    \"주어진 환경의 변화에 대해 대처 능력이 뛰어난가?\": 4,\n" +
            "    \"타인과의 업무 진행이 효율적인가?\": 5,\n" +
            "    \"타인과의 원만한 관계를 이끌어내는가?\": 5,\n" +
            "    \"주어진 업무에 대해 책임감을 갖고 있는가?\": 5,\n" +
            "    \"리더십이 있나요?\": 5,\n" +
            "    \"도덕성이 있나요?\": 5")
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

    @ApiOperation(
            value = "전체 리뷰 조회 (실제 기능 X)"
            , notes = "reviewId 까먹었을 때 확인용")
    @GetMapping("/list")
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        List<ReviewDto> reviews = reviewService.getAllReviews();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
}
