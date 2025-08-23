package com.kobi.elearning.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
public class IntrospectRequest {
	String token;
}
