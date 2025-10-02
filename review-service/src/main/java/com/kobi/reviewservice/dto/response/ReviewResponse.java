package com.kobi.reviewservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {
    String id;
    String userId;
    String courseId;
    int rating;
    String comment;
    LocalDateTime createdAt;
    String userName;
    String avatar;
}
