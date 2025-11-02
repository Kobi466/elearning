package com.kobi.paymentservice.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "vnpay")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VnPayConfig {
    String tmnCode;
    String hashSecret;
    String url;
    String returnUrl;
    String apiUrl;
}
