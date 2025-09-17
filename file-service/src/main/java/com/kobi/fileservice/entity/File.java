package com.kobi.fileservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
public class File {
    @Id
    String id;
    String url;
    String objectKey;
    String uploaderId;
    String fileType;
    Long fileSize;
    LocalDateTime createdDate;
}
