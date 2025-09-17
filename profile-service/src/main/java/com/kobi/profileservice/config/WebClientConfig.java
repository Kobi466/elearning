package com.kobi.profileservice.config;

import com.kobi.profileservice.repository.httpClient.FileClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {
//    @Bean
//    WebClient webClient() {
//        return WebClient.builder()
//                .baseUrl("http://localhost:8084/file")
//                .build();
//    }

    @Bean
    FileClient fileClient(WebClient.Builder builder) {
        // Lấy token từ SecurityContext và thêm vào header của request
        ServletBearerExchangeFilterFunction bearer = new ServletBearerExchangeFilterFunction();
        // 2. Xây dựng một WebClient với cấu hình RIÊNG cho FileClient
        WebClient webClient = builder
                .baseUrl("http://localhost:8084/file")
                .filter(bearer)
                .build();
        // 3. Dùng factory để tạo ra client từ interface và WebClient đã cấu hình
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter
                        .create(webClient))
                .build();
        return httpServiceProxyFactory.createClient(FileClient.class);
    }
}
