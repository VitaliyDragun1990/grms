package com.revenat.germes.gateway.infrastructure.config;

import com.revenat.germes.gateway.presentation.routing.DefaultRequestComposer;
import com.revenat.germes.gateway.presentation.routing.DefaultRequestRouter;
import com.revenat.germes.gateway.presentation.routing.http.HttpRequestDispatcher;
import com.revenat.germes.gateway.presentation.security.interceptor.JwtInterceptor;
import com.revenat.germes.infrastructure.http.RestClient;
import com.revenat.germes.infrastructure.http.impl.JavaRestClient;
import com.revenat.germes.infrastructure.json.impl.GsonJsonClient;
import com.revenat.germes.user.presentation.rest.client.UserFacade;
import com.revenat.germes.user.presentation.rest.client.impl.UserClient;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.client.RestTemplate;

/**
 * @author Vitaliy Dragun
 */
public class GatewayContextInitializer implements ApplicationContextInitializer<GenericApplicationContext> {

    @Override
    public void initialize(final GenericApplicationContext ctx) {
        ctx.registerBean(RestTemplate.class);
        ctx.registerBean(DefaultRequestComposer.class);
        ctx.registerBean(HttpRequestDispatcher.class);
        ctx.registerBean(DefaultRequestRouter.class);
        ctx.registerBean(GatewayHandlerMapping.class);
        ctx.registerBean(JwtInterceptor.class);
        ctx.registerBean(UserFacade.class, () -> createUserFacade(ctx));
    }

    private UserFacade createUserFacade(final GenericApplicationContext ctx) {
        final String authUrl = ctx.getEnvironment().getProperty("germes.gateway.authentication.url");
        final int connTimeout = ctx.getEnvironment().getRequiredProperty("germes.gateway.authentication.timeout", Integer.class);
        final RestClient restClient = new JavaRestClient(connTimeout, new GsonJsonClient());

        return new UserClient(authUrl, restClient);
    }
}
