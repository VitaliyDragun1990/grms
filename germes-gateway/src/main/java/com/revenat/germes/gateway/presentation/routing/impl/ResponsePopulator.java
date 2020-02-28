package com.revenat.germes.gateway.presentation.routing.impl;

import com.revenat.germes.gateway.presentation.routing.exception.RouteException;
import org.springframework.http.ResponseEntity;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Vitaliy Dragun
 */
class ResponsePopulator {

    /**
     * Populates specified {@linkplain HttpServletResponse response} using data
     * from specified {@linkplain ResponseEntity dataHolder}
     *
     * @param response   target response to populate
     * @param dataHolder source of the data
     * @return response populated with data
     * @throws RouteException if some error occurs during request populating process
     */
    HttpServletResponse populateResponseFrom(final HttpServletResponse response, final ResponseEntity<?> dataHolder) {
        response.setStatus(dataHolder.getStatusCodeValue());
        setResponseHeaders(response, dataHolder.getHeaders().entrySet());
        setResponseBody(response, dataHolder.getBody());

        return response;
    }

    private void setResponseBody(HttpServletResponse response, Object body) {
        if (body != null) {
            try {
                final ServletOutputStream outputStream = response.getOutputStream();
                new ObjectOutputStream(outputStream).writeObject(body);
                outputStream.flush();
            } catch (IOException e) {
                throw new RouteException(e);
            }
        }
    }

    private void setResponseHeaders(final HttpServletResponse response, final Set<Map.Entry<String, List<String>>> headers) {
        headers.forEach(header -> header.getValue()
                .forEach(headerValue -> response.addHeader(header.getKey(), headerValue)));
    }
}
