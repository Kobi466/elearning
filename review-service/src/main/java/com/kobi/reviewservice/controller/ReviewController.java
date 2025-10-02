package com.kobi.reviewservice.controller;

import com.kobi.reviewservice.dto.ApiResponse;
import com.kobi.reviewservice.dto.PageResponse;
import com.kobi.reviewservice.dto.request.CreateReviewRequest;
import com.kobi.reviewservice.dto.response.ReviewResponse;
import com.kobi.reviewservice.exception.SuccessCode;
import com.kobi.reviewservice.service.ReviewService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {
    ReviewService reviewService;
    @PostMapping("/create")
    ApiResponse<ReviewResponse> createReview(@Valid @RequestBody CreateReviewRequest createReviewRequest){
        return ApiResponse.ok(reviewService.createReview(createReviewRequest), SuccessCode.REVIEW_CREATED);
    }
    @GetMapping("/get-reviewsForCourse")
    ApiResponse<PageResponse<ReviewResponse>> getReviewsForCourse(@RequestParam String courseId, Pageable pageable){
        return ApiResponse.ok(reviewService.getReviewsForCourse(courseId, pageable), SuccessCode.GET_REVIEWS_SUCCESS);
    }
}
