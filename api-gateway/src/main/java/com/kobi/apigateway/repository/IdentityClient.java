package com.kobi.apigateway.repository;

import com.kobi.apigateway.dto.ApiResponse;
import com.kobi.apigateway.dto.request.IntrospectRequest;
import com.kobi.apigateway.dto.response.IntrospectResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface IdentityClient {
    @PostExchange(value = "/api/v1/auth/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<IntrospectResponse>> introspectToken(@RequestBody IntrospectRequest request);
}
