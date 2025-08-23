package com.kobi.elearning.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    @Size(min = 4, message = "Username must be at least {min} characters")
    String userName;
    @NotBlank
    @Email
    String email;
    @Size(min = 8, message = "Password must be at least {min} characters")
    String passWord;
}
