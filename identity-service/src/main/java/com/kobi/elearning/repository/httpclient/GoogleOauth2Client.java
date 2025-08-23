package com.kobi.elearning.repository.httpclient;

import com.kobi.elearning.dto.request.ExchangeTokenRequest;
import com.kobi.elearning.dto.response.ExchangeTokenResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "google-oauth2", url = "https://oauth2.googleapis.com")
public interface GoogleOauth2Client {
	@PostMapping(value = "/token", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	ExchangeTokenResponse exchangeToken(@QueryMap ExchangeTokenRequest request);
}
