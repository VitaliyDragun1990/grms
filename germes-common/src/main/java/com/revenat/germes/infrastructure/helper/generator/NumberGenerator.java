package com.revenat.germes.infrastructure.helper.generator;

/**
 * Provides random numbers
 *
 * @author Vitaliy Dragun
 */
public interface NumberGenerator {

    /**
     * Returns a unique number that should differ from previously generated numbers
     */
    int generate();
}
