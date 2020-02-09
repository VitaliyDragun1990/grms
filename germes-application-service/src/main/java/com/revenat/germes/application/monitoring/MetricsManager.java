package com.revenat.germes.application.monitoring;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * Registers metrics (metrics set) needed for monitoring purposes.
 *
 * @author Vitaliy Dragun
 */
@Named
@ApplicationScoped
public class MetricsManager {

    private final MetricRegistry metricRegistry;

    public MetricsManager() {
        metricRegistry = new MetricRegistry();

        metricRegistry.register("gc", new GarbageCollectorMetricSet());
        metricRegistry.register("threads", new ThreadStatesGaugeSet());
        metricRegistry.register("memory", new MemoryUsageGaugeSet());
    }

    /**
     * Returns all registered metrics
     */
    public MetricRegistry getMetricRegistry() {
        return metricRegistry;
    }

    /**
     * Registers given metric under given name
     *
     * @param name   name of the metric
     * @param metric metric to register
     * @return registered metric
     */
    public <T extends Metric> T registerMetric(final String name, final T metric) {
        return metricRegistry.register(name, metric);
    }
}
