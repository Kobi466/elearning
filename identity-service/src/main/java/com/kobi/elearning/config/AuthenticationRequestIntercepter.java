//package com.kobi.elearning.config;
//
//import feign.RequestInterceptor;
//import feign.RequestTemplate;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//@Configuration
////Global interceptor for feign client
//public class AuthenticationRequestIntercepter implements RequestInterceptor {
//    @Override
//    public void apply(RequestTemplate requestTemplate) {
//        ServletRequestAttributes attr =
//                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        var authHeader = attr.getRequest().getHeader("Authorization");
//        if (authHeader != null) {
//            requestTemplate.header("Authorization", authHeader);
//        }
//    }
//}
////Co the config internal feign client