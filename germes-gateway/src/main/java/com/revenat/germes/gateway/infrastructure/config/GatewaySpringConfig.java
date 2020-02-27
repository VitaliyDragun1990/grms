package com.revenat.germes.gateway.infrastructure.config;

import com.revenat.germes.gateway.application.security.token.TokenProcessor;
import com.revenat.germes.gateway.presentation.security.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Java-based configuration for Gateway application
 *
 * @author Vitaliy Dragun
 */
@Configuration
public class GatewaySpringConfig {

    @Configuration
    public static class SecurityConfig implements WebMvcConfigurer {

        @Autowired
        private List<HandlerInterceptor> interceptors;

        @Override
        public void addInterceptors(final InterceptorRegistry registry) {
            interceptors.forEach(registry::addInterceptor);
        }

        @Bean
        public HandlerInterceptor jwtInterceptor(final TokenProcessor tokenProcessor) {
            return new JwtInterceptor(tokenProcessor);
        }
    }
}
