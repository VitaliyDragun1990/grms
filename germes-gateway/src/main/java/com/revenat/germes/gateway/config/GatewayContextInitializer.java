package com.revenat.germes.gateway.config;

import com.revenat.germes.gateway.ui.routing.DefaultRequestComposer;
import com.revenat.germes.gateway.ui.routing.DefaultRequestRouter;
import com.revenat.germes.gateway.ui.routing.HttpRequestDispatcher;
import com.revenat.germes.common.infrastructure.http.RestClient;
import com.revenat.germes.common.infrastructure.http.impl.JavaRestClient;
import com.revenat.germes.common.infrastructure.json.impl.GsonJsonTranslator;
import com.revenat.germes.user.core.application.UserFacade;
import com.revenat.germes.user.infrastructure.service.RestUserFacade;
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
        final RestClient restClient = new JavaRestClient(connTimeout, new GsonJsonTranslator());

        return new RestUserFacade(authUrl, restClient);
    }
}
