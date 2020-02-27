package com.revenat.germes.gateway.domain.model.token;

import com.revenat.germes.gateway.domain.model.token.exception.ExpiredTokenException;
import com.revenat.germes.gateway.domain.model.token.exception.TokenException;

/**
 * Component responsible for token parsing/generation
 *
 * @author Vitaliy Dragun
 */
public interface TokenProcessor {
    /**
     * Generates token based on provided {@code userName} argument
     *
     * @param userName subject for whom token will be generated
     * @return generated token
     * @throws IllegalArgumentException if specified userName argument is null or blank
     */
    String generateToken(String userName);

    /**
     * Extracts user name(subject) from specified token
     *
     * @param token token to extract data from
     * @return user name(subject) for whom specified token was generated
     * @throws IllegalArgumentException if provided token is null or empty
     * @throws ExpiredTokenException    is provided token already expired
     * @throws TokenException           if provided token is not a valid token
     */
    String getUserName(String token);
}
