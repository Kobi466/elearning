package com.kobi.notificationservice.service.httpclient;


import org.springframework.web.service.annotation.PostExchange;

public interface EmailClient {
    @PostExchange("/sendEmail")
    Object sendEmail(Object emailRequest);
}
