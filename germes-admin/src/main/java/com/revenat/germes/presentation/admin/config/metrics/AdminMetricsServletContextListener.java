package com.revenat.germes.presentation.admin.config.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet;
import com.revenat.germes.infrastructure.monitoring.MetricsManager;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;

/**
 * Web listener responsible for injecting {@link MetricRegistry} into servlet context
 *
 * @author Vitaliy Dragun
 */
@WebListener
public class AdminMetricsServletContextListener extends MetricsServlet.ContextListener {

    @Inject
    private MetricsManager metricsManager;

    @Override
    protected MetricRegistry getMetricRegistry() {
        return metricsManager.getMetricRegistry();
    }
}
