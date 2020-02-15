package com.revenat.germes.application.infrastructure.helper.generator;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Cryptographically secured random number generator
 *
 * @author Vitaliy Dragun
 */
public class SecureRandomNumberGenerator implements NumberGenerator {

    private final Random random = new SecureRandom();

    private final int limit;

    public SecureRandomNumberGenerator(final int limit) {
        this.limit = limit;
    }

    @Override
    public int generate() {
        return random.nextInt(limit);
    }
}
