package com.kobi.paymentservice.config;

import com.kobi.paymentservice.repository.httpClient.CourseClient;
import com.kobi.paymentservice.repository.httpClient.VnPayClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {
    @Bean
    CourseClient courseClient(WebClient.Builder builder) {
        ServletBearerExchangeFilterFunction bearer = new ServletBearerExchangeFilterFunction();
        WebClient webClient = builder
                .baseUrl("http://localhost:8085/course")
                .filter(bearer)
                .build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter
                        .create(webClient))
                .build();
        return httpServiceProxyFactory.createClient(CourseClient.class);
    }
    @Bean
    VnPayClient vnPayClient(WebClient.Builder builder) {
        WebClient webClient = builder
                .build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter
                        .create(webClient))
                .build();
        return httpServiceProxyFactory.createClient(VnPayClient.class);
    }
}
