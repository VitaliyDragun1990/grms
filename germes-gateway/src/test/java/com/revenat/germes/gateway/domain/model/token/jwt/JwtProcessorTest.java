package com.revenat.germes.gateway.domain.model.token.jwt;

import com.revenat.germes.gateway.domain.model.token.TokenProcessor;
import com.revenat.germes.gateway.domain.model.token.exception.ExpiredTokenException;
import com.revenat.germes.common.core.shared.exception.ConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        TokenProcessor tokenProcessor = new JwtProcessor(VALID_SECRET, THIRTY_MINUTES, ISSUER);

        String token = tokenProcessor.generateToken(USER_NAME);

        assertNotNull(token);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "     "})
    @NullSource
    void shouldFailToGenerateTokenIfProvidedUserNameIsUninitialized(String userName) {
        TokenProcessor tokenProcessor = new JwtProcessor(VALID_SECRET, THIRTY_MINUTES, ISSUER);

        assertThrows(IllegalArgumentException.class, () -> tokenProcessor.generateToken(userName));
    }

    @Test
    void shouldExtractUsernameFromToken() {
        TokenProcessor tokenProcessor = new JwtProcessor(VALID_SECRET, THIRTY_MINUTES, ISSUER);
        String token = tokenProcessor.generateToken(USER_NAME);

        final String userName = tokenProcessor.getUserName(token);

        assertThat(userName, equalTo(USER_NAME));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "     "})
    @NullSource
    void shouldFailToExtractUserNameIfSpecifiedTokenIsUninitialized(String token) {
        TokenProcessor tokenProcessor = new JwtProcessor(VALID_SECRET, THIRTY_MINUTES, ISSUER);

        assertThrows(IllegalArgumentException.class,  () -> tokenProcessor.getUserName(token));
    }

    @Test
    void shouldFailToExtractUserNameFromTokenIfSuchTokenIsAlreadyExpired() throws InterruptedException {
        TokenProcessor tokenProcessor = new JwtProcessor(VALID_SECRET, TEN_MILLIS, ISSUER);
        String token = tokenProcessor.generateToken(USER_NAME);

        Thread.sleep(TEN_MILLIS + 1);
        assertThrows(ExpiredTokenException.class, () -> tokenProcessor.getUserName(token));
    }
}