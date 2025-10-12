package com.kobi.paymentservice.repository.httpClient;

import com.kobi.paymentservice.dto.response.VnPayIPNResponse;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Repository
public interface VnPayClient {
    @PostExchange("/merchant_webapi/api/transaction")
    Mono<VnPayIPNResponse> callRefund(@RequestBody Map<String, String> params);
}
