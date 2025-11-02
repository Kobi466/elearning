package com.kobi.notificationservice.listener;

import com.kobi.notificationservice.dto.request.Recipient;
import com.kobi.notificationservice.dto.request.SendEmailRequest;
import com.kobi.notificationservice.service.EmailService;
import event.NotificationEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationEmail {
    EmailService emailService;

    @KafkaListener(topics = "onboard-success", groupId = "notification-service-group")
    public void handleNotificationEmail(NotificationEvent event) {
        emailService.sendEmail(SendEmailRequest.builder()
                .to(Recipient.builder()
                        .email(event.getRecipient())
                        .build()
                )
                .subject(event.getTemplate())
                .htmlContent("Chào mừng " + event.getParam().get("userName").toString() + " đến với công ty Hy SoftWare")
                .build()
        );
    }
}
