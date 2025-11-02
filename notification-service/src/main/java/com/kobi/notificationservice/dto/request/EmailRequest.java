package com.kobi.notificationservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class EmailRequest {
    Sender sender;
    List<Recipient> to;
    String subject;
    String htmlContent;
}
