package com.revenat.germes.gateway.presentation.routing.helper;

import com.revenat.germes.gateway.domain.model.routing.ResponseInfo;
import com.revenat.germes.gateway.domain.model.routing.exception.RouteException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Vitaliy Dragun
 */
public class ResponsePopulator {

    /**
     * Populates specified {@linkplain HttpServletResponse response} using data
     * from specified {@linkplain ResponseInfo responseInfo}
     *
     * @param response   target response to populate
     * @param responseInfo source of the data
     * @return response populated with data
     * @throws RouteException if some error occurs during request populating process
     */
    public HttpServletResponse populateResponseFrom(final HttpServletResponse response, final ResponseInfo responseInfo) {
        response.setStatus(responseInfo.getStatusCode());
        setResponseHeaders(response, responseInfo.getHeaders().entrySet());
        setResponseBody(response, responseInfo.getBody());

        return response;
    }

    private void setResponseBody(final HttpServletResponse response, final String body) {
        if (body != null) {
            try {
                final ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(body.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            } catch (final IOException e) {
                throw new RouteException(e);
            }
        }
    }

    private void setResponseHeaders(final HttpServletResponse response, final Set<Map.Entry<String, List<String>>> headers) {
        headers.forEach(header -> header.getValue()
                .forEach(headerValue -> response.addHeader(header.getKey(), headerValue)));
    }
}
