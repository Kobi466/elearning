package com.kobi.paymentservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VnPayRefundResponse {
    @JsonProperty("vnp_ResponseId")
    String responseId;
    @JsonProperty("vnp_Command")
    String command;
    @JsonProperty("vnp_TmnCode")
    String tmnCode;
    @JsonProperty("vnp_TxnRef")
    String txnRef;
    @JsonProperty("vnp_Amount")
    String amount;
    @JsonProperty("vnp_OrderInfo")
    String orderInfo;
    @JsonProperty("vnp_ResponseCode")
    String rspCode;
    @JsonProperty("vnp_Message")
    String message;
    @JsonProperty("vnp_BankCode")
    String bankCode;
    @JsonProperty("vnp_PayDate")
    String payDate;
    @JsonProperty("vnp_TransactionNo")
    String transactionNo;
    @JsonProperty("vnp_TransactionType")
    String transactionType;
    @JsonProperty("vnp_TransactionStatus")
    String transactionStatus;
    @JsonProperty("vnp_SecureHash")
    String secureHash;
}
