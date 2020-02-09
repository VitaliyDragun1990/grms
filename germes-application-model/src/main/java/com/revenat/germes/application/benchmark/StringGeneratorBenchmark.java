package com.revenat.germes.application.benchmark;

import com.revenat.germes.application.infrastructure.helper.generator.text.RandomDigitStringGenerator;
import com.revenat.germes.application.infrastructure.helper.generator.text.StringGenerator;
import com.revenat.germes.application.infrastructure.helper.generator.text.UUIDStringGenerator;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author Vitaliy Dragun
 */
@Warmup(iterations = 3)
@Measurement(iterations = 3)
@Fork(value = 1, jvmArgsAppend = "-server")
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
public class StringGeneratorBenchmark {

    private static final int STRING_SIZE = 32;

    private StringGenerator digitGenerator;

    private StringGenerator uuidGenerator;

    @Setup
    public void setup() {
        digitGenerator = new RandomDigitStringGenerator(STRING_SIZE);
        uuidGenerator = new UUIDStringGenerator();
    }

    @Benchmark
    public String generateDigitString() {
        return digitGenerator.generate();
    }

    @Benchmark
    public String generateUUIDString() {
        return uuidGenerator.generate();
    }

    public static void main(final String[] args) throws RunnerException {
        final Options options = new OptionsBuilder()
                .include(".*")
                .timeUnit(TimeUnit.NANOSECONDS)
                .resultFormat(ResultFormatType.TEXT)
                .build();

        new Runner(options).run();
    }
}
