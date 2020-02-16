package com.revenat.germes.infrastructure.helper.generator;

/**
 * Returns sequential numbers in the ascending order
 *
 * @author Vitaliy Dragun
 */
public class SequentialNumberGenerator implements NumberGenerator {

    private int counter;

    private final int limit;

    public SequentialNumberGenerator(final int limit) {
        this.limit = limit;
    }

    @Override
    public int generate() {
        if (counter >= limit) {
            counter = 1;
        } else {
            counter ++;
        }
        return counter;
    }
}
