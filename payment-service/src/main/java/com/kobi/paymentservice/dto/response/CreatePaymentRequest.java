package com.kobi.paymentservice.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreatePaymentRequest {
    @NotBlank(message = "Course ID không được để trống")
    String courseId;
}
