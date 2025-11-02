package com.kobi.profileservice.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kobi.profileservice.dto.ApiResponse;
import com.kobi.profileservice.exception.ErrorCode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	private static final String[] PUBLIC_URLS = {
			"/internal/get-by-ids"
	};

	CustomJwtDecoder jwtDecoder;
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(requests ->
				requests
						.requestMatchers(PUBLIC_URLS)
						.permitAll()
						.anyRequest()
						.authenticated()
		);
		http.oauth2ResourceServer(resourceServer -> resourceServer
				.jwt(jwt -> jwt
						.decoder(jwtDecoder)
						.jwtAuthenticationConverter(jwtAuthenticationConverter()))
				.authenticationEntryPoint((AuthenticationEntryPoint) (request, response, authException) -> {
                    ErrorCode errorCode = ErrorCode.AUTHENTICATION_FAILED;
                    response.setStatus(errorCode.getHttpStatus().value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    ApiResponse<?> apiResponse = ApiResponse.builder()
                            .status(errorCode.getStatus())
                            .message(errorCode.getMessage())
                            .build();
                    ObjectMapper objectMapper = new ObjectMapper();
                    response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
                    response.flushBuffer();
                })

		);
		http.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.csrf(AbstractHttpConfigurer::disable);
		return http.build();
	}

	@Bean
	JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
		jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("scope");
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}
}
