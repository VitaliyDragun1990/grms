package com.revenat.germes.gateway.application.security.jwt;

import com.revenat.germes.gateway.application.security.jwt.exception.ExpiredJwtException;
import com.revenat.germes.infrastructure.exception.ConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("jwt processor")
class JwtProcessorTest {

    private static final String USER_NAME = "test";
    private static final String ISSUER = "Germes";
    private static final String VALID_SECRET = "ZUSb5prVYqrnR2HjXIQ614s4Ac+2kNBlhpMyGBEr31I=";
    private static final long THIRTY_MINUTES = 1000 * 60 * 30L;
    private static final long TEN_MILLIS = 10L;

    @Test
    void shouldFailIToBeCreatedWithInvalidSecret() {
        String invalidSecret = "secret";

        assertThrows(ConfigurationException.class, () -> new JwtProcessor(invalidSecret, THIRTY_MINUTES, ISSUER));
    }

    @Test
    void shouldGenerateToken() {
        JwtProcessor jwtProcessor = new JwtProcessor(VALID_SECRET, THIRTY_MINUTES, ISSUER);

        String token = jwtProcessor.generateToken(USER_NAME);

        assertNotNull(token);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "     "})
    @NullSource
    void shouldFailToGenerateTokenIfProvidedUserNameIsUninitialized(String userName) {
        JwtProcessor jwtProcessor = new JwtProcessor(VALID_SECRET, THIRTY_MINUTES, ISSUER);

        assertThrows(IllegalArgumentException.class, () -> jwtProcessor.generateToken(userName));
    }

    @Test
    void shouldExtractUsernameFromToken() {
        JwtProcessor jwtProcessor = new JwtProcessor(VALID_SECRET, THIRTY_MINUTES, ISSUER);
        String token = jwtProcessor.generateToken(USER_NAME);

        final String userName = jwtProcessor.getUserName(token);

        assertThat(userName, equalTo(USER_NAME));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "     "})
    @NullSource
    void shouldFailToExtractUserNameIfSpecifiedTokenIsUninitialized(String token) {
        JwtProcessor jwtProcessor = new JwtProcessor(VALID_SECRET, THIRTY_MINUTES, ISSUER);

        assertThrows(IllegalArgumentException.class,  () -> jwtProcessor.getUserName(token));
    }

    @Test
    void shouldFailToExtractUserNameFromTokenIfSuchTokenIsAlreadyExpired() throws InterruptedException {
        JwtProcessor jwtProcessor = new JwtProcessor(VALID_SECRET, TEN_MILLIS, ISSUER);
        String token = jwtProcessor.generateToken(USER_NAME);

        Thread.sleep(TEN_MILLIS + 1);
        assertThrows(ExpiredJwtException.class, () -> jwtProcessor.getUserName(token));
    }
}