package com.kobi.paymentservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VnPayIPNResponse {
    @JsonProperty("vnp_ResponseCode")
    String rspCode;
    @JsonProperty("vnp_Message")
    String message;
}
