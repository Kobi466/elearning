package com.kobi.reviewservice.repository.httpClient;

import com.kobi.reviewservice.dto.ApiResponse;
import com.kobi.reviewservice.dto.response.ProfileResponse;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface ProfileClient {
    @PostExchange("/internal/get-by-ids")
    Mono<ApiResponse<List<ProfileResponse>>> getProfilesByIds(@RequestBody List<String> userIds);
}
