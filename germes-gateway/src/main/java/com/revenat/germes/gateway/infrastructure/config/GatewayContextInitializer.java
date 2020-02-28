package com.revenat.germes.gateway.infrastructure.config;

import com.revenat.germes.gateway.presentation.routing.DefaultRequestComposer;
import com.revenat.germes.gateway.presentation.routing.DefaultRequestRouter;
import com.revenat.germes.gateway.presentation.routing.http.HttpRequestDispatcher;
import com.revenat.germes.gateway.presentation.security.interceptor.JwtInterceptor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.client.RestTemplate;

/**
 * @author Vitaliy Dragun
 */
public class GatewayContextInitializer implements ApplicationContextInitializer<GenericApplicationContext> {

    @Override
    public void initialize(GenericApplicationContext ctx) {
        ctx.registerBean(RestTemplate.class);
        ctx.registerBean(DefaultRequestComposer.class);
        ctx.registerBean(HttpRequestDispatcher.class);
        ctx.registerBean(DefaultRequestRouter.class);
        ctx.registerBean(GatewayHandlerMapping.class);
        ctx.registerBean(JwtInterceptor.class);
    }
}
