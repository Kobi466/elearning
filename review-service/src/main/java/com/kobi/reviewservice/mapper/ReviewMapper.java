package com.kobi.reviewservice.mapper;

import com.kobi.reviewservice.dto.response.ReviewResponse;
import com.kobi.reviewservice.entity.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {Review.class})
public interface ReviewMapper {
    ReviewResponse toResponse(Review review);
}
