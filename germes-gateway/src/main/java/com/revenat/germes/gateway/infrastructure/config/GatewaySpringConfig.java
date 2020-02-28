package com.revenat.germes.gateway.infrastructure.config;

import com.revenat.germes.gateway.domain.model.route.RouteProvider;
import com.revenat.germes.gateway.domain.model.route.StaticRouteProvider;
import com.revenat.germes.gateway.domain.model.token.TokenProcessor;
import com.revenat.germes.gateway.presentation.routing.RequestRouter;
import com.revenat.germes.gateway.presentation.routing.impl.DefaultRequestRouter;
import com.revenat.germes.gateway.presentation.security.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
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
    @DependsOn("jwtInterceptor")
    public static class GatewayConfig {

        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }

        @Bean
        public RouteProvider routeProvider() {
            return new StaticRouteProvider();
        }

        @Bean
        public RequestRouter requestRouter(final RestTemplate restTemplate, final RouteProvider routeProvider) {
            return new DefaultRequestRouter(restTemplate, routeProvider);
        }

        @Bean
        public HandlerMapping gatewayHandlerMapping(final RouteProvider routeProvider,
                                                    final RequestRouter requestRouter,
                                                    final JwtInterceptor jwtInterceptor) {
            return new GatewayHandlerMapping(routeProvider, requestRouter, jwtInterceptor);
        }
    }

    @Configuration
    public static class SecurityConfig {

        @Bean
        public HandlerInterceptor jwtInterceptor(final TokenProcessor tokenProcessor) {
            return new JwtInterceptor(tokenProcessor);
        }
    }
}
