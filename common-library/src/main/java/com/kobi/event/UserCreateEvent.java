package com.kobi.event;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserCreateEvent {
    String userId;
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
