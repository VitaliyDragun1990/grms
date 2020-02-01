package com.revenat.germes.application.infrastructure.benchmark;

import com.revenat.germes.application.infrastructure.helper.generator.NumberGenerator;
import com.revenat.germes.application.infrastructure.helper.generator.RandomNumberGenerator;
import com.revenat.germes.application.infrastructure.helper.generator.SecureRandomNumberGenerator;
import com.revenat.germes.application.infrastructure.helper.generator.SequentialNumberGenerator;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Vitaliy Dragun
 */
@Warmup(iterations = 3)
@Measurement(iterations = 3)
@Fork(value = 1, jvmArgsAppend = "-server")
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
public class NumberGeneratorBenchmark {

    private static final int GENERATION_LIMIT = 100;

    private Map<String, NumberGenerator> generators;

    @Param({"Seq", "Random", "Secured"})
    private String generator;

    @Setup
    public void setup() {
        generators = new HashMap<>();
        generators.put("Seq", new SequentialNumberGenerator(GENERATION_LIMIT));
        generators.put("Random", new RandomNumberGenerator(GENERATION_LIMIT));
        generators.put("Secured", new SecureRandomNumberGenerator(GENERATION_LIMIT));
    }

    @Benchmark
    public int generateSingleRandomNumber() {
        return generators.get(generator).generate();
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(".*")
                .timeUnit(TimeUnit.NANOSECONDS)
                .resultFormat(ResultFormatType.TEXT)
                .build();

        new Runner(options).run();
    }
}
