package com.revenat.germes.application.infrastructure.helper.generator.text;

import com.revenat.germes.application.infrastructure.helper.Checker;
import com.revenat.germes.application.infrastructure.helper.generator.NumberGenerator;
import com.revenat.germes.application.infrastructure.helper.generator.RandomNumberGenerator;

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
        new Checker().checkParameter(size > 0, "String length should be greater than zero");
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
