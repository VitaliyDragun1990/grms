package com.revenat.germes.persistence.benchmark;

import com.revenat.germes.persistence.repository.CityRepository;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import javax.persistence.Entity;
import java.util.concurrent.TimeUnit;

/**
 * @author Vitaliy Dragun
 */
@Warmup(iterations = 3)
@Measurement(iterations = 3)
@Fork(value = 1, jvmArgsAppend = "-server")
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
public class ClassGraphBenchmark {

    @Benchmark
    public void findClassesByEntityAnnotation(final Blackhole blackhole) {
        try (final ScanResult scanResult = new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .whitelistPackages("com.revenat.germes.application.model.entity")
                .scan()) {
            scanResult.getClassesWithAnnotation(Entity.class.getName()).getNames();
            blackhole.consume(scanResult.getClassesWithAnnotation(Entity.class.getName()).getNames());
        }
    }

    @Benchmark
    public void findClassesImplementingCityRepositoryInterface(final Blackhole blackhole) {
        try (final ScanResult scanResult = new ClassGraph()
                .enableClassInfo()
                .whitelistPackages("com.revenat.germes.persistence")
                .scan()) {
            blackhole.consume(scanResult.getClassesImplementing(CityRepository.class.getName()).getNames());
        }
    }

    public static void main(final String[] args) throws RunnerException {
        final Options options = new OptionsBuilder()
                .include(".*")
                .timeUnit(TimeUnit.SECONDS)
                .resultFormat(ResultFormatType.TEXT)
                .build();

        new Runner(options).run();
    }
}
