package com.kobi.reviewservice.config;

import com.kobi.reviewservice.repository.httpClient.ProfileClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {
    @Bean
    ProfileClient profileClient(WebClient.Builder webClientBuilder) {
        ServletBearerExchangeFilterFunction filter = new ServletBearerExchangeFilterFunction();
        WebClient webClient = webClientBuilder.
                baseUrl("http://localhost:8082/profile").
                filter(filter)
                .build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient)).build();
        return httpServiceProxyFactory.createClient(ProfileClient.class);
    }
}
