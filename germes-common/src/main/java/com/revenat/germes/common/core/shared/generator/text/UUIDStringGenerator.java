package com.revenat.germes.common.core.shared.generator.text;

import java.util.UUID;

/**
 * Generates random string based on UUID format
 *
 * @author Vitaliy Dragun
 */
public class UUIDStringGenerator implements StringGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
