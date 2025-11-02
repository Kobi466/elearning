package com.kobi.progressservice.config;

import com.kobi.progressservice.repository.httpClient.EnrollmentClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {
    @Bean
    EnrollmentClient enrollmentClient(WebClient.Builder webClientBuilder) {
        ServletBearerExchangeFilterFunction bearer = new ServletBearerExchangeFilterFunction();
        WebClient webClient = webClientBuilder
                .baseUrl("http://localhost:8086/enrollment")
                .filter(bearer)
                .build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build();
        return httpServiceProxyFactory.createClient(EnrollmentClient.class);
    }
}
