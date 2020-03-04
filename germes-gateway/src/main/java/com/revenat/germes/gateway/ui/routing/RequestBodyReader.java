package com.revenat.germes.gateway.ui.routing;

import com.revenat.germes.gateway.core.routing.exception.RouteException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * Responsible for reading request body from {@link HttpServletRequest}
 *
 * @author Vitaliy Dragun
 */
class RequestBodyReader {

    /**
     * Reads request body if any, or returns {@code null} if no body is present
     *
     * @param request {@link HttpServletRequest} instance
     */
    public Object readRequestBody(HttpServletRequest request) {
        try {
            final ServletInputStream requestInputStream = request.getInputStream();
            if (isBodyPresent(requestInputStream)) {
                return readBody(requestInputStream);
            }
            return null;
        } catch (IOException e) {
            throw new RouteException(e);
        }
    }

    private boolean isBodyPresent(ServletInputStream requestInputStream) throws IOException {
        return requestInputStream.available() > 0;
    }

    private Object readBody(InputStream is) {
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return ois.readObject();
        } catch (final ClassNotFoundException | IOException e) {
            throw new RouteException(e);
        }
    }
}
