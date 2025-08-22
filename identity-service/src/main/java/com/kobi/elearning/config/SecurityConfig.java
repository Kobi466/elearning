package com.kobi.elearning.config;


import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kobi.elearning.dto.response.ApiResponse;
import com.kobi.elearning.exception.ErrorCode;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	private static final String[] PUBLIC_URLS = {
			"/api/v1/auth/login",
			"/api/v1/users/register",
			"/api/v1/auth/introspect",
			"/api/v1/auth/logout",
			"/api/v1/auth/refresh",
			"/oauth2/callback"
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
				.authenticationEntryPoint(new AuthenticationEntryPoint() {
					@Override
					public void commence(HttpServletRequest request,
										HttpServletResponse response,
										AuthenticationException authException
					) throws IOException, ServletException {
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
					}
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

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
}
