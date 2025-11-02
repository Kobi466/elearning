package com.kobi.paymentservice.repository.httpClient;

import com.kobi.paymentservice.dto.ApiResponse;
import com.kobi.paymentservice.dto.response.CoursePriceResponse;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

@Repository
public interface CourseClient {
    @GetExchange("/internal/price/{courseId}")
    Mono<ApiResponse<CoursePriceResponse>> getCoursePrice(@PathVariable String courseId);
}
