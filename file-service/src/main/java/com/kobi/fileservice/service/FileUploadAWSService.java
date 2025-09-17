package com.kobi.fileservice.service;

import com.kobi.fileservice.dto.response.FileResponse;
import com.kobi.fileservice.entity.File;
import com.kobi.fileservice.repository.FileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class FileUploadAWSService {
    FileRepository fileRepository;
    S3Client s3Client;
    @NonFinal
    @Value("${aws.s3.bucket-name}")
    String bucketName;
    @NonFinal
    @Value("${aws.region}")
    String region;

    public FileResponse uploadFile(MultipartFile file) {
        //lay userId token
        String userId = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        String objKey = userId + "/" + UUID.randomUUID().toString()
                + "-" + file.getOriginalFilename();
        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(objKey)
                            .contentType(file.getContentType())
                            .build()
                    , RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
            String url = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, objKey);

            var fileEntity = File.builder()
                    .id(UUID.randomUUID().toString())
                    .url(url)
                    .objectKey(objKey)
                    .uploaderId(userId)
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .createdDate(java.time.LocalDateTime.now())
                    .build();
            fileRepository.save(fileEntity);
            return FileResponse.builder()
                    .fileId(fileEntity.getId())
                    .url(fileEntity.getUrl())
                    .build();

        } catch (IOException e) {
            log.error("Failed to upload file for user {}", userId, e);
            throw new RuntimeException(e);
        }
    }
}
