package com.kobi.apigateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kobi.apigateway.dto.ApiResponse;
import com.kobi.apigateway.service.IdentityService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {
    IdentityService identityService;
    ObjectMapper objectMapper;
    @Value("${app.api-prefix}")
    @NonFinal
    String apiPrefix;
    @NonFinal
    String[] publicApi = {
            "/course/v1",
            "/course/v1/getCourse",
            "/identity/api/v1/auth/.*",
            "/identity/api/v1/users/register"
    };

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("AuthenticationFilter.....");
        //Public API
        if (isPublicApi(exchange.getRequest()))
            return chain.filter(exchange);

        //Get token from header
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeader))
            return unauthenticatedResponse(exchange.getResponse());

        String token = authHeader.getFirst().replace("Bearer ", "");
        log.info("Token : {}", token);
        //Introspect token
        return identityService.introspectToken(token)
                //Process response from identity service
                .flatMap(introspectResponseApiResponse ->
                        {
                            if (introspectResponseApiResponse.getData().isValid())
                                return chain.filter(exchange);
                            else
                                return unauthenticatedResponse(exchange.getResponse());
                        }
                ).onErrorResume(throwable -> unauthenticatedResponse(exchange.getResponse()));
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private boolean isPublicApi(ServerHttpRequest request) {
        return Arrays.stream(publicApi).anyMatch(s ->
                request.getURI().getPath().matches(apiPrefix + s)
        );
    }

    Mono<Void> unauthenticatedResponse(ServerHttpResponse response) {
        //Viết thư (Tạo ApiResponse):
        // Bạn viết một lá thư báo lỗi với nội dung "Unauthenticated".
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .status(1401)
                .message("Unauthenticated")
                .build();
        //Cho vào phong bì (Serialize thành JSON):
        // Bạn cho lá thư vào một phong bì trong suốt có ghi nội dung thư bên
        // ngoài dưới dạng mã hóa (JSON) mà người đưa thư có thể đọc.
        String body = null;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //Dán tem và địa chỉ (Set Status & Headers):
        // Bạn dán một con tem "KHẨN - 401" (HTTP Status) và
        // ghi chú "Bên trong là tài liệu JSON" (Content-Type).
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(
                HttpHeaders.CONTENT_TYPE,
                MediaType.APPLICATION_JSON_VALUE);
        //Đưa cho người đưa thư (Ghi vào response):
        // Bạn đưa gói hàng (DataBuffer) cho một người đưa thư (Mono) và
        // người này sẽ đảm nhận việc chuyển nó đi (writeWith).
        // Khi người đưa thư báo đã gửi xong (Mono<Void> hoàn thành),
        // nhiệm vụ của bạn kết thúc.
        byte[] bytes = body.getBytes();
        DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);
        Mono<DataBuffer> mono = Mono.just(dataBuffer);
        return response.writeWith(mono);
//        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}
