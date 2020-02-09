package com.revenat.germes.presentation.admin.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet;

import javax.servlet.annotation.WebListener;

/**
 * Web listener responsible for injecting {@link MetricRegistry} into servlet context
 *
 * @author Vitaliy Dragun
 */
@WebListener
public class AdminMetricsServletContextListener extends MetricsServlet.ContextListener {

    public static final MetricRegistry METRIC_REGISTRY = new MetricRegistry();

    @Override
    protected MetricRegistry getMetricRegistry() {
        return METRIC_REGISTRY;
    }
}
