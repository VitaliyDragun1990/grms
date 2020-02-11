package com.revenat.germes.presentation.admin.metrics;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.revenat.germes.application.infrastructure.environment.Environment;
import com.revenat.germes.application.monitoring.MetricsManager;
import com.revenat.germes.persistence.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.monitoring.healthcheck.MySQLHealthCheck;
import com.revenat.germes.presentation.admin.bean.startup.Eager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.net.InetSocketAddress;
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
    public AdminMonitoringConfiguration(final MetricsManager metricsManager,
                                        final SessionFactoryBuilder sessionFactoryBuilder,
                                        final Environment env) {
        metricsManager.registerHealthCheck(MYSQL_HEALTH_CHECK, new MySQLHealthCheck(sessionFactoryBuilder));

        final boolean reporterEnabled = env.getPropertyAsBoolean("graphite.reporter.enabled");

        if (reporterEnabled) {
            final Graphite graphite = new Graphite(new InetSocketAddress(env.getProperty("graphite.reporter.host"),
                    env.getPropertyAsInt("graphite.reporter.port")));
            final GraphiteReporter graphiteReporter = GraphiteReporter.forRegistry(metricsManager.getMetricRegistry())
                    .prefixedWith("admin")
                    .convertRatesTo(TimeUnit.SECONDS)
                    .convertDurationsTo(TimeUnit.MILLISECONDS)
                    .filter(MetricFilter.ALL)
                    .build(graphite);
            graphiteReporter.start(env.getPropertyAsInt("graphite.reporter.duration"), TimeUnit.SECONDS);
        }
    }

    /**
     * For CDI container purpose
     */
    AdminMonitoringConfiguration() {
    }
}
