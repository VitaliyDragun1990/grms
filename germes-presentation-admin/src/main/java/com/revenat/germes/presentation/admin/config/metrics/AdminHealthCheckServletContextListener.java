package com.revenat.germes.presentation.admin.config.metrics;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.HealthCheckServlet;
import com.revenat.germes.infrastructure.monitoring.MetricsManager;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;

/**
 * Web listener responsible for injecting {@link HealthCheckRegistry} into servlet context
 *
 * @author Vitaliy Dragun
 */
@WebListener
public class AdminHealthCheckServletContextListener extends HealthCheckServlet.ContextListener {

    @Inject
    private MetricsManager metricsManager;

    @Override
    protected HealthCheckRegistry getHealthCheckRegistry() {
        return metricsManager.getHealthCheckRegistry();
    }
}
