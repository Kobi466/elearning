package com.kobi.profileservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.LocalDate;

@Node("user_profile")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Profile {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String profileId;
    @Property("userId")
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
