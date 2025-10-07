package com.kobi.paymentservice.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePaymentRequest {
    @NotBlank(message = "Course ID không được để trống")
    String courseId;
}
