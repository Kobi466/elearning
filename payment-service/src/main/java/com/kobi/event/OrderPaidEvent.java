package com.kobi.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderPaidEvent{
    String eventId;
    String orderId;
    String userId;
    String courseId;
    BigDecimal amount;
    LocalDateTime createdAt;
}
