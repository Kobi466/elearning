package com.kobi.paymentservice.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

@Builder
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CreatePaymentResponse {
    String paymentUrl;
}
