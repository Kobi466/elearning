package com.kobi.profileservice.controller;

import com.kobi.profileservice.dto.repsonse.ApiResponse;
import com.kobi.profileservice.exception.SuccessCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {
    @GetMapping("/hello")
    public ApiResponse<String> hello() {
        return ApiResponse.ok("Hello from Profile Service!", SuccessCode.USER_FETCH_SUCCESS);
    }
}
