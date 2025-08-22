package com.kobi.elearning.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GoogleInfoUserResponse {
	String id;
	String email;
	boolean verifiedEmail;
	String name;
	String givenName;
	String familyName;
	String picture;
	String locale;
}
