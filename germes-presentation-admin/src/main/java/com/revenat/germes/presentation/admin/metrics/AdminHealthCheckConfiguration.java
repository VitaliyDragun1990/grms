package com.revenat.germes.presentation.admin.metrics;

import com.revenat.germes.application.monitoring.MetricsManager;
import com.revenat.germes.persistence.monitoring.healthcheck.MySQLHealthCheck;
import com.revenat.germes.persistence.hibernate.SessionFactoryBuilder;
import com.revenat.germes.presentation.admin.bean.startup.Eager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Responsible for registering custom health checks on startup
 *
 * @author Vitaliy Dragun
 */
@Named
@ApplicationScoped
@Eager
public class AdminHealthCheckConfiguration {

    private static final String MYSQL_HEALTH_CHECK = "mysql";

    @Inject
    public AdminHealthCheckConfiguration(final MetricsManager metricsManager, final SessionFactoryBuilder sessionFactoryBuilder) {
        metricsManager.registerHealthCheck(MYSQL_HEALTH_CHECK, new MySQLHealthCheck(sessionFactoryBuilder));
    }

    /**
     * For CDI container purpose
     */
    AdminHealthCheckConfiguration() {
    }
}
