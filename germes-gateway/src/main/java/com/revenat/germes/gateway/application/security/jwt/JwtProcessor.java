package com.revenat.germes.gateway.application.security.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

/**
 * @author Vitaliy Dragun
 */
public class JwtProcessor {

    public static void main(String[] args) {
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        System.out.println(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
    }
}
