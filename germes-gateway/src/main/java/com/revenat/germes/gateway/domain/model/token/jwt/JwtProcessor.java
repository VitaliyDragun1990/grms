package com.revenat.germes.gateway.domain.model.token.jwt;

import com.revenat.germes.gateway.domain.model.token.TokenProcessor;
import com.revenat.germes.gateway.domain.model.token.exception.ExpiredTokenException;
import com.revenat.germes.gateway.domain.model.token.exception.TokenException;
import com.revenat.germes.common.core.shared.exception.ConfigurationException;
import com.revenat.germes.common.core.shared.helper.Asserts;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Component responsible for JWT token parsing/generation
 *
 * @author Vitaliy Dragun
 */
@ConfigurationProperties("germes.gateway.jwt")
@ConstructorBinding
public class JwtProcessor implements TokenProcessor {

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

    @Override
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

    @Override
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
            throw new ExpiredTokenException(e);
        } catch (Exception e) {
            throw new TokenException(e);
        }
    }
}
