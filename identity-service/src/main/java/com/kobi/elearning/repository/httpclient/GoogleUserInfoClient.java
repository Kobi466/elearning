package com.kobi.elearning.repository.httpclient;

import com.kobi.elearning.dto.response.GoogleInfoUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "outbound-user-client", url = "https://www.googleapis.com")
public interface GoogleUserInfoClient {
	@GetMapping(value = "/oauth2/v1/userinfo")
	GoogleInfoUserResponse getUserInfo(@RequestParam("alt") String alt,
									@RequestParam("access_token") String accessToken);
}
