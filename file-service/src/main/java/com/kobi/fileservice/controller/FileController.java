package com.kobi.fileservice.controller;

import com.kobi.fileservice.dto.ApiResponse;
import com.kobi.fileservice.dto.response.FileResponse;
import com.kobi.fileservice.exception.SuccessCode;
import com.kobi.fileservice.service.FileUploadAWSService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal=true)
public class FileController {
    FileUploadAWSService fileUploadAWSService;
    @PostMapping("/upload")
    ApiResponse<FileResponse> uploadFile(@RequestPart("file") MultipartFile file) {
        return ApiResponse.ok(fileUploadAWSService.uploadFile(file), SuccessCode.UPLOAD_SUCCESS);
    }
}
