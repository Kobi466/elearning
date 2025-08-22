package com.kobi.elearning.dto.response.auth;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
public class IntrospectResponse {
	boolean valid;
}
