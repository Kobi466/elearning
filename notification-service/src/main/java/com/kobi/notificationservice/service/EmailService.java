package com.kobi.notificationservice.service;

import com.kobi.notificationservice.dto.request.EmailRequest;
import com.kobi.notificationservice.dto.request.SendEmailRequest;
import com.kobi.notificationservice.dto.request.Sender;
import com.kobi.notificationservice.dto.response.EmailResponse;
import com.kobi.notificationservice.repository.httpclient.EmailClient;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    EmailClient emailClient;
    @NonFinal
    @Value("${notification.email.brevo-api-key}")
    String brevoApiKey;
    @NonFinal
    @Value("${notification.sender.name}")
    String senderName;
    @NonFinal
    @Value("${notification.sender.email}")
    String senderMail;

    public EmailResponse sendEmail(SendEmailRequest request) {
        return emailClient.sendEmail(brevoApiKey,
                EmailRequest.builder()
                        .sender(Sender.builder()
                                .name(senderName)
                                .email(senderMail)
                                .build()
                        )
                        .to(List.of(request.getTo()))
                        .subject(request.getSubject())
                        .htmlContent(request.getHtmlContent())
                        .build()
        );
    }
}

