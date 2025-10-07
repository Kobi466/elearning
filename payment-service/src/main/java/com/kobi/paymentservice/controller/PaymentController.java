package com.kobi.paymentservice.controller;

import com.kobi.paymentservice.dto.ApiResponse;
import com.kobi.paymentservice.dto.request.CreatePaymentResponse;
import com.kobi.paymentservice.dto.response.CreatePaymentRequest;
import com.kobi.paymentservice.exception.SuccessCode;
import com.kobi.paymentservice.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    PaymentService paymentService;

    @PostMapping("/create-payment")
    ApiResponse<CreatePaymentResponse> createPayment(@RequestBody @Valid CreatePaymentRequest createPaymentRequest, HttpServletRequest httpServletRequest) {
        return ApiResponse.ok(paymentService.createPayment(createPaymentRequest, httpServletRequest), SuccessCode.CREATE_PAYMENT_SUCCESS);
    }
}
