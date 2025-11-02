package com.kobi.notificationservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SendEmailRequest {
    Recipient to;
    String subject;
    String htmlContent;
}
