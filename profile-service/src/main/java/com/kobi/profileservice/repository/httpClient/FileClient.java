package com.kobi.profileservice.repository.httpClient;

import com.kobi.profileservice.dto.ApiResponse;
import com.kobi.profileservice.dto.repsonse.FileResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;


public interface FileClient {
    @PostExchange(value = "/upload", contentType = MediaType.MULTIPART_FORM_DATA_VALUE)
            //Kem token toi File service
//    Mono<ApiResponse<FileResponse>> uploadFile(
//            @RequestHeader("Authorization") String token,
//            @RequestPart("file") MultipartFile file
//    );
    Mono<ApiResponse<FileResponse>> uploadFile(
            @RequestPart("file") MultipartFile file
    );
}
