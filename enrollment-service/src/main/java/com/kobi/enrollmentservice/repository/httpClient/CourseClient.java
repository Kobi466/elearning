package com.kobi.enrollmentservice.repository.httpClient;

import com.kobi.enrollmentservice.dto.ApiResponse;
import com.kobi.enrollmentservice.dto.response.CourseResponse;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface CourseClient {
    @GetExchange(value = "/internal/{courseId}")
    Mono<ApiResponse<CourseResponse>> getCourseById(@PathVariable("courseId") String courseId);
    @PostExchange(value = "/internal/get-by-ids")
    Mono<ApiResponse<List<CourseResponse>>> getCourseByIds(@RequestBody List<String> courseIds);
}
