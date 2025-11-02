package com.kobi.notificationservice.config;

import com.kobi.notificationservice.repository.httpclient.EmailClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {
    @Value("${notification.email.brevo-url}")
    private String brevoUrl;

    @Bean
    WebClient webClient() {
        return WebClient.builder()
                .baseUrl(brevoUrl)
                .build();
    }

    @Bean
    EmailClient emailClient(WebClient client) {
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter
                        .create(client))
                .build();
        return httpServiceProxyFactory.createClient(EmailClient.class);
    }
}


