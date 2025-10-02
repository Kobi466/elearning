package com.kobi.progressservice.repository.httpClient;

import com.kobi.progressservice.dto.ApiResponse;
import com.kobi.progressservice.dto.response.EnrollmentStatusResponse;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

@Repository
public interface EnrollmentClient {
    @GetExchange("/v1/status")
    Mono<ApiResponse<EnrollmentStatusResponse>> getEnrollmentStatus(@RequestParam String courseId);
}
