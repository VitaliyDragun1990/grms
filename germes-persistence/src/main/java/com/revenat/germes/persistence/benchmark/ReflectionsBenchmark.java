package com.revenat.germes.persistence.benchmark;

import com.revenat.germes.persistence.repository.CityRepository;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.reflections.Reflections;

import javax.persistence.Entity;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Vitaliy Dragun
 */
@Warmup(iterations = 3)
@Measurement(iterations = 3)
@Fork(value = 1, jvmArgsAppend = "-server")
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
public class ReflectionsBenchmark {

    @Benchmark
    public void findClassesByEntityAnnotation(Blackhole blackhole) {
        final Reflections reflections = new Reflections("com.revenat.germes.application.model.entity");

        blackhole.consume(reflections.getTypesAnnotatedWith(Entity.class));
    }

    @Benchmark
    public void findClassesImplementingCityRepositoryInterface(Blackhole blackhole) {
        final Reflections reflections = new Reflections("com.revenat.germes.persistence");

        blackhole.consume(reflections.getSubTypesOf(CityRepository.class));
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(".*")
                .timeUnit(TimeUnit.SECONDS)
                .resultFormat(ResultFormatType.TEXT)
                .build();

        new Runner(options).run();
    }
}
