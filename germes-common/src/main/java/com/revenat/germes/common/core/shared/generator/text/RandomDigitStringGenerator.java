package com.revenat.germes.common.core.shared.generator.text;

import com.revenat.germes.common.core.shared.generator.NumberGenerator;
import com.revenat.germes.common.core.shared.generator.RandomNumberGenerator;
import com.revenat.germes.common.core.shared.helper.Checker;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Generates random string of the specified length each character of which is some random digit
 *
 * @author Vitaliy Dragun
 */
public class RandomDigitStringGenerator implements StringGenerator {

    /**
     * Size of the generated string
     */
    private final int size;

    private final NumberGenerator numberGenerator;

    public RandomDigitStringGenerator(final int size) {
        Checker.checkParameter(size > 0, "String length should be greater than zero");

        this.size = size;
        numberGenerator = new RandomNumberGenerator(10);
    }

    @Override
    public String generate() {
        return IntStream.range(0, size)
                .map(i -> numberGenerator.generate())
                .boxed()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
