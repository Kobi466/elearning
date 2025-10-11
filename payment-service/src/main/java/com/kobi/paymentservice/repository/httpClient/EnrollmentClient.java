package com.kobi.paymentservice.repository.httpClient;

import com.kobi.paymentservice.dto.ApiResponse;
import com.kobi.paymentservice.dto.request.EnrollmentRequest;
import com.kobi.paymentservice.dto.request.InternalEnrollmentRequest;
import com.kobi.paymentservice.dto.response.EnrollmentResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface EnrollmentClient {
    @PostExchange("/v1/enrollment")
    Mono<ApiResponse<EnrollmentResponse>> enrollment(@RequestBody EnrollmentRequest enrollmentRequest);
    @PostExchange("/internal/enrollment")
    Mono<ApiResponse<Void>> enrollment(@RequestBody @Valid InternalEnrollmentRequest enrollmentRequest);
}
