package com.kobi.paymentservice.controller;

import com.kobi.paymentservice.dto.ApiResponse;
import com.kobi.paymentservice.dto.request.CreatePaymentResponse;
import com.kobi.paymentservice.dto.response.CreatePaymentRequest;
import com.kobi.paymentservice.dto.response.VnPayIPNResponse;
import com.kobi.paymentservice.exception.SuccessCode;
import com.kobi.paymentservice.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    PaymentService paymentService;

    @PostMapping("/create-payment")
    ApiResponse<CreatePaymentResponse> createPayment(@RequestBody @Valid CreatePaymentRequest createPaymentRequest, HttpServletRequest httpServletRequest) {
        return ApiResponse.ok(paymentService.createPayment(createPaymentRequest, httpServletRequest), SuccessCode.CREATE_PAYMENT_SUCCESS);
    }

    @GetMapping("/vpn-ipn")
    ApiResponse<VnPayIPNResponse> handleVnPayIPN(@RequestParam Map<String, String> allRequestParams) {
        System.out.println(">>>>>>>>>>>>>>>>> VNPAY IPN CALLED! <<<<<<<<<<<<<<<<<");
        return ApiResponse.ok(paymentService.handleVnPayIPN(allRequestParams), SuccessCode.VN_PAY_IPN_SUCCESS);
    }

    @GetMapping("/vnpay-return")
    public void handleVnpayReturn(HttpServletResponse httpServletResponse, @RequestParam Map<String, String> allRequestParams) throws IOException {
        String redirectUrl = paymentService.handleVnPayReturn(allRequestParams);
        httpServletResponse.sendRedirect(redirectUrl);
    }
}
