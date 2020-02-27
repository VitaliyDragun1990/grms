package com.revenat.germes.gateway.application.security.jwt;

import com.revenat.germes.gateway.application.security.jwt.exception.ExpiredJwtException;
import com.revenat.germes.gateway.application.security.jwt.exception.JwtException;
import com.revenat.germes.infrastructure.exception.ConfigurationException;
import com.revenat.germes.infrastructure.helper.Asserts;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Component responsible for JWT token parsing/generation
 *
 * @author Vitaliy Dragun
 */
@Service
public class JwtProcessor {

    private static final SignatureAlgorithm JWT_ALGORITHM = SignatureAlgorithm.HS256;

    private final long expirationInMillis;

    private final String issuer;

    private final SecretKey secretKey;

    /**
     * Creates new instance of {@link JwtProcessor}
     *
     * @param secret             secret used to generate token
     * @param expirationInMillis expiration time in milliseconds during which generated token will be considered valid
     * @param issuer             issuer of the generated token
     * @throws IllegalArgumentException if specified issuer is null or blank
     * @throws ConfigurationException   if specified secret is not strong enough
     */
    public JwtProcessor(final String secret, final long expirationInMillis, final String issuer) {
        Asserts.assertNotNullOrBlank(issuer, "issuer can not be null or blank");

        try {
            final byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
            secretKey = Keys.hmacShaKeyFor(bytes);
        } catch (WeakKeyException e) {
            throw new ConfigurationException("Error creating JwtProcessor: ", e);
        }

        this.expirationInMillis = expirationInMillis;
        this.issuer = issuer;
    }

    /**
     * Generates token based on provided {@code userName} argument
     *
     * @param userName subject for whom token will be generated
     * @return generated JWT token
     * @throws IllegalArgumentException if specified userName argument is null or blank
     */
    public String generateToken(final String userName) {
        Asserts.assertNotNullOrBlank(userName, "userName can not be null or blank");

        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationInMillis))
                .signWith(secretKey, JWT_ALGORITHM)
                .compact();
    }

    /**
     * Extracts user name(subject) from specified token
     *
     * @param token JWT token to extract data from
     * @return user name(subject) for whom specified token was generated
     * @throws IllegalArgumentException if provided token is null or empty
     * @throws ExpiredJwtException      is provided token already expired
     * @throws JwtException             if provided token is not a valid JWT token
     */
    public String getUserName(final String token) {
        Asserts.assertNotNullOrBlank(token, "token can not be null or blank");

        return extractUserNameFrom(token);
    }

    private String extractUserNameFrom(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new ExpiredJwtException(e);
        } catch (Exception e) {
            throw new JwtException(e);
        }
    }
}
