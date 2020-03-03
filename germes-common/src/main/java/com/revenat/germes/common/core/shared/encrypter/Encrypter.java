package com.revenat.germes.common.core.shared.encrypter;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * Encrypts provided string
 *
 * @author Vitaliy Dragun
 */
public class Encrypter {

    /**
     * Encrypts source text using SHA-256 encoding
     *
     * @param source text to encrypt
     * @return encrypted string
     */
    public String encryptSHA(final String source) {
        return Hashing.sha256().hashString(source, StandardCharsets.UTF_8).toString();
    }
}
