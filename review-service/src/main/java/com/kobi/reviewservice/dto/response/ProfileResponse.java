package com.kobi.reviewservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProfileResponse {
    String id;
    String userName;
    String email;
    String avatar;
    String firstName;
    String lastName;
    String fullName;
    String gender;
    LocalDate dob;
    String locale;
}
