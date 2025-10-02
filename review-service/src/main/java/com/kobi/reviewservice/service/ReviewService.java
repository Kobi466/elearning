package com.kobi.reviewservice.service;

import com.kobi.reviewservice.dto.PageResponse;
import com.kobi.reviewservice.dto.request.CreateReviewRequest;
import com.kobi.reviewservice.dto.response.ProfileResponse;
import com.kobi.reviewservice.dto.response.ReviewResponse;
import com.kobi.reviewservice.entity.Review;
import com.kobi.reviewservice.exception.AppException;
import com.kobi.reviewservice.exception.ErrorCode;
import com.kobi.reviewservice.mapper.ReviewMapper;
import com.kobi.reviewservice.repository.ReviewRepository;
import com.kobi.reviewservice.repository.httpClient.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ReviewService {
    ReviewRepository reviewRepository;
    ProfileClient profileClient;
    ReviewMapper reviewMapper;

    public ReviewResponse createReview(CreateReviewRequest createReviewRequest) {
        if (reviewRepository.existsByUserIdAndCourseId(getUserId(), createReviewRequest.getCourseId()))
            throw new AppException(ErrorCode.REVIEW_EXISTS);
        return reviewMapper.toResponse(reviewRepository
                .save(Review.builder()
                        .userId(getUserId())
                        .courseId(createReviewRequest.getCourseId())
                        .rating(createReviewRequest.getRating())
                        .comment(createReviewRequest.getComment())
                        .build())
        );
    }
    public PageResponse<ReviewResponse> getReviewsForCourse(String courseId, Pageable pageable){
        Page<Review> reviewPage = reviewRepository.findByCourseId(courseId, pageable);
        List<String> userIds = reviewPage.stream()
                .map(Review::getUserId)
                .distinct().toList();
        List<ProfileResponse> listProfile = Objects.requireNonNull(profileClient.getProfilesByIds(userIds)
                .block()).getData().stream().toList();
        Map<String, ProfileResponse> profileMap = listProfile.stream().
                collect(Collectors.toMap(ProfileResponse::getId, profile -> profile));
        List<ReviewResponse> reviewResponses = reviewPage.stream().map(review -> {
            ReviewResponse reviewResponse = reviewMapper.toResponse(review);
            ProfileResponse profile = profileMap.get(review.getUserId());
            if(profile!=null){
                reviewResponse.setAvatar(profile.getAvatar());
                reviewResponse.setUserName(profile.getUserName());
            }
            return reviewResponse;
        }).toList();
        return PageResponse.<ReviewResponse>builder()
                .content(reviewResponses)
                .totalElement(reviewPage.getTotalElements())
                .totalPages(reviewPage.getTotalPages())
                .pageNo(reviewPage.getNumber())
                .pageSize(reviewPage.getSize())
                .build();
    }

    private String getUserId(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
