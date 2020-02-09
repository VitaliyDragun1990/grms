package com.revenat.germes.application.benchmark;

import com.revenat.germes.application.model.entity.base.AbstractEntity;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.application.service.transfrom.helper.FieldManager;
import com.revenat.germes.application.service.transfrom.helper.SimilarFieldsLocator;
import com.revenat.germes.application.service.transfrom.impl.FieldProvider;
import com.revenat.germes.application.service.transfrom.impl.cache.CachedFieldProvider;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Vitaliy Dragun
 */
@Warmup(iterations = 3)
@Measurement(iterations = 3)
@Fork(value = 1, jvmArgsAppend = "-server")
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
public class FieldProviderBenchmark {

    private Map<String, FieldProvider> providers;

    @Param({"Basic", "Cached"})
    private String provider;

    @Setup
    public void setup() {
        providers = new HashMap<>();
        providers.put("Basic", new FieldProvider(new SimilarFieldsLocator(), new FieldManager()));
        providers.put("Cached", new CachedFieldProvider(new SimilarFieldsLocator(), new FieldManager()));
    }

    @Benchmark
    public void getSimilarFieldNames_objectsWithSimilarFields() {
        providers.get(provider).getSimilarFieldNames(City.class, CityCopy.class);
    }

    @Benchmark
    public void getSimilarFieldNames_objectsWithoutSimilarFields() {
        providers.get(provider).getSimilarFieldNames(City.class, Object.class);
    }

    public static void main(final String[] args) throws RunnerException {
        final Options opts = new OptionsBuilder().include(".*")
                .timeUnit(TimeUnit.NANOSECONDS)
                .resultFormat(ResultFormatType.TEXT)
                .build();

        new Runner(opts).run();
    }

    public static class CityCopy extends AbstractEntity {

        private String name;

        private String district;

        private String region;

        private Set<Station> stations;

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(final String district) {
            this.district = district;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(final String region) {
            this.region = region;
        }

        public Set<Station> getStations() {
            return stations;
        }

        public void setStations(final Set<Station> stations) {
            this.stations = stations;
        }
    }
}
