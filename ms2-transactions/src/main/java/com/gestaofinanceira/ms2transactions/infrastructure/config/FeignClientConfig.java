package com.gestaofinanceira.ms2transactions.infrastructure.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {

                String targetUrl = template.url();
                String targetHost = template.feignTarget().url();

                boolean isInternalCall = targetHost.contains("ms1-usuarios") || targetUrl.contains("/users");

                if (isInternalCall) {
                    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

                    if (attributes != null) {
                        HttpServletRequest request = attributes.getRequest();
                        String authorizationHeader = request.getHeader("Authorization");

                        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
                            template.header("Authorization", authorizationHeader);
                        }
                    }
                }
            }
        };
    }
}