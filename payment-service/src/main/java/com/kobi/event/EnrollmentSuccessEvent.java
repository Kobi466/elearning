package com.kobi.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollmentSuccessEvent {
    String orderId;
    String courseId;
    String userId;
    String eventType;
}
