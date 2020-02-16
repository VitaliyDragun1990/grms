package com.revenat.germes.infrastructure.helper.generator;

/**
 * Returns random numbers using Math.random() method
 *
 * @author Vitaliy Dragun
 */
public class RandomNumberGenerator implements NumberGenerator {

    private final int limit;

    public RandomNumberGenerator(final int limit) {
        this.limit = limit;
    }

    @Override
    public int generate() {
        return (int) (Math.random() * limit);
    }
}
