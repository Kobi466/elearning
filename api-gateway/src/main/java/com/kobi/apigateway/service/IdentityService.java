package com.kobi.apigateway.service;

import com.kobi.apigateway.dto.ApiResponse;
import com.kobi.apigateway.dto.request.IntrospectRequest;
import com.kobi.apigateway.dto.response.IntrospectResponse;
import com.kobi.apigateway.repository.IdentityClient;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class IdentityService {
    IdentityClient identityClient;

    public Mono<ApiResponse<IntrospectResponse>> introspectToken(String token) {
        return identityClient.introspectToken(IntrospectRequest.builder()
                .token(token)
                .build());
    }
}
