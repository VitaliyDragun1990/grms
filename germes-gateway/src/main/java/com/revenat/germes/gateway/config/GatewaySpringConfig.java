package com.revenat.germes.gateway.config;

import com.revenat.germes.gateway.core.route.RouteProvider;
import com.revenat.germes.gateway.core.routing.RequestComposer;
import com.revenat.germes.gateway.core.routing.RequestDispatcher;
import com.revenat.germes.gateway.core.routing.RequestRouter;
import com.revenat.germes.gateway.core.token.TokenProcessor;
import com.revenat.germes.gateway.ui.routing.DefaultRequestComposer;
import com.revenat.germes.gateway.ui.routing.HttpRequestDispatcher;
import com.revenat.germes.gateway.ui.routing.DefaultRequestRouter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

/**
 * Java-based configuration for Gateway application.
 * Deprecated in favor of {@link GatewayContextInitializer}
 *
 * @author Vitaliy Dragun
 */
//@Configuration
public class GatewaySpringConfig {

//    @Configuration
    @DependsOn("jwtInterceptor")
    public static class GatewayConfig {

        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }

        @Bean
        public RequestComposer requestComposer() {
            return new DefaultRequestComposer();
        }

        @Bean
        public RequestDispatcher requestDispatcher(final RouteProvider routeProvider, final RestTemplate restTemplate) {
            return new HttpRequestDispatcher(routeProvider, restTemplate);
        }

        @Bean
        public RequestRouter requestRouter(final RequestComposer requestComposer, final RequestDispatcher requestDispatcher) {
            return new DefaultRequestRouter(requestComposer, requestDispatcher);
        }

        @Bean
        public HandlerMapping gatewayHandlerMapping(final RouteProvider routeProvider,
                                                    final RequestRouter requestRouter,
                                                    final JwtInterceptor jwtInterceptor) {
            return new GatewayHandlerMapping(routeProvider, requestRouter, jwtInterceptor);
        }
    }

//    @Configuration
    public static class SecurityConfig {

        @Bean
        public HandlerInterceptor jwtInterceptor(final TokenProcessor tokenProcessor) {
            return new JwtInterceptor(tokenProcessor);
        }
    }
}
