package com.revenat.germes.gateway.infrastructure.config.advice;

import com.revenat.germes.gateway.domain.model.route.RouteProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Returns CORS headers to the client application
 *
 * @author Vitaliy Dragun
 */
@ControllerAdvice
@RequiredArgsConstructor
public class CorsHeaderResponseAdvice implements ResponseBodyAdvice<Object> {

    private final RouteProvider routeProvider;

    @Override
    public boolean supports(final MethodParameter returnType, final Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(final Object body, final MethodParameter returnType,
                                  final MediaType selectedContentType,
                                  final Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  final ServerHttpRequest request,
                                  final ServerHttpResponse response) {
        if (routeProvider.containsRouteMatching(request.getURI().getPath())) {
            response.getHeaders().add("Access-Control-Allow-Origin", "*");
        }
        response.getHeaders().add("Access-Control-Allow-Credentials", "true");
        response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");

        return body;
    }
}
