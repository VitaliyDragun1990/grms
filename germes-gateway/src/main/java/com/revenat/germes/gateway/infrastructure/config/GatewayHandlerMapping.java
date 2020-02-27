package com.revenat.germes.gateway.infrastructure.config;

import com.revenat.germes.gateway.domain.model.route.RouteProvider;
import com.revenat.germes.gateway.presentation.routing.RequestRouter;
import com.revenat.germes.gateway.presentation.security.interceptor.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Custom handler mapping that adds dynamic endpoints for gateway purposes
 *
 * @author Vitaliy Dragun
 */
@RequiredArgsConstructor
public class GatewayHandlerMapping extends RequestMappingHandlerMapping {

    private final RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();

    private final RouteProvider routeProvider;

    private final RequestRouter requestRouter;

    /**
     * Define our custom jwt interceptor to manually register it for this handler mapping
     */
    private final JwtInterceptor jwtInterceptor;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    protected void initHandlerMethods() {
        super.initHandlerMethods();

        final Method handlerMethod = BeanUtils.findDeclaredMethod(requestRouter.getClass(), RequestRouter.HANDLER_NAME,
                HttpServletRequest.class, HttpServletResponse.class);

        routeProvider.getRoutePrefixes().stream()
                .map(this::createGatewayEndpoint)
                .forEach(requestInfo -> registerHandlerMethod(requestRouter, handlerMethod, requestInfo));
    }

    @Override
    protected void initInterceptors() {
        // Register our custom jwt interceptor
        setInterceptors(jwtInterceptor);
        super.initInterceptors();
    }

    /**
     * Creates custom endpoint for specified path prefix
     *
     * @param prefix path prefix to create endpoint for
     */
    private RequestMappingInfo createGatewayEndpoint(final String prefix) {
        final RequestMappingInfo.Builder builder = RequestMappingInfo
                .paths(prefix + "/**")
                .methods(RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.DELETE);

        return builder.options(config).build();
    }
}
