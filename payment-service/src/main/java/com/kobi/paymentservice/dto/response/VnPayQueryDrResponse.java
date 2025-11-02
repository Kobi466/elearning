package com.kobi.paymentservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VnPayQueryDrResponse {

    @JsonProperty("vnp_ResponseId")
    private String responseId;

    @JsonProperty("vnp_Command")
    private String command;

    @JsonProperty("vnp_ResponseCode")
    private String responseCode;

    @JsonProperty("vnp_Message")
    private String message;

    @JsonProperty("vnp_TmnCode")
    private String tmnCode;

    @JsonProperty("vnp_TxnRef")
    private String txnRef;

    @JsonProperty("vnp_Amount")
    private String amount;

    @JsonProperty("vnp_OrderInfo")
    private String orderInfo;

    @JsonProperty("vnp_TransactionNo")
    private String transactionNo;

    // TRƯỜNG QUAN TRỌNG NHẤT
    @JsonProperty("vnp_TransactionStatus")
    private String transactionStatus;
}
