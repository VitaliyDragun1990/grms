package com.revenat.germes.presentation.admin.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricFilter;
import com.revenat.germes.application.monitoring.MetricsManager;
import com.revenat.germes.persistence.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.monitoring.healthcheck.MySQLHealthCheck;
import com.revenat.germes.presentation.admin.bean.startup.Eager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.TimeUnit;

/**
 * Responsible for registering metrics monitoring configuration on application startup
 *
 * @author Vitaliy Dragun
 */
@Named
@ApplicationScoped
@Eager
public class AdminMonitoringConfiguration {

    private static final String MYSQL_HEALTH_CHECK = "mysql";

    @Inject
    public AdminMonitoringConfiguration(final MetricsManager metricsManager, final SessionFactoryBuilder sessionFactoryBuilder) {
        metricsManager.registerHealthCheck(MYSQL_HEALTH_CHECK, new MySQLHealthCheck(sessionFactoryBuilder));

        final ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metricsManager.getMetricRegistry())
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .filter(MetricFilter.ALL)
                .build();
        consoleReporter.start(30, TimeUnit.SECONDS);
    }

    /**
     * For CDI container purpose
     */
    AdminMonitoringConfiguration() {
    }
}
