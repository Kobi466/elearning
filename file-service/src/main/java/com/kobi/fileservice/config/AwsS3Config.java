package com.kobi.fileservice.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AwsS3Config {
    @Value( "${aws.region}")
    String region;
    @Value( "${aws.credentials.access-key}")
    String accessKey;
    @Value( "${aws.credentials.secret-key}")
    String secretKey;

    @Bean
    public S3Client s3Client(){
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .build();
    }
}
