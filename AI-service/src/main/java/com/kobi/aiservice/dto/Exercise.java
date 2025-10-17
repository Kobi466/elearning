package com.kobi.aiservice.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Exercise {
    String question;
    String answerA;
    String answerB;
    String answerC;
    String answerD;
    String correctAnswer;
}
