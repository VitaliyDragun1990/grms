package com.revenat.germes.gateway.config;

import com.revenat.germes.gateway.core.route.RouteProvider;
import com.revenat.germes.gateway.core.routing.RequestRouter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.core.Ordered;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Custom handler mapping that adds dynamic endpoints for gateway purposes
 *
 * @author Vitaliy Dragun
 */
@RequiredArgsConstructor
public class GatewayHandlerMapping extends AbstractHandlerMapping {

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
    protected Object getHandlerInternal(final HttpServletRequest request) throws Exception {
        if (routeProvider.containsRouteMatching(request.getRequestURI())) {
            return buildHandler();
        }
        return null;
    }

    private HandlerMethod buildHandler() {
        final Method handlerMethod = findHandlerMethod();

        return new HandlerMethod(requestRouter, handlerMethod);
    }

    private Method findHandlerMethod() {
        return BeanUtils.findDeclaredMethod(requestRouter.getClass(), RequestRouter.HANDLER_NAME,
                HttpServletRequest.class, HttpServletResponse.class);
    }

    @Override
    protected void initInterceptors() {
        // Register our custom jwt interceptor
        setInterceptors(jwtInterceptor);
        super.initInterceptors();
    }
}
