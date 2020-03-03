package com.revenat.germes.infrastructure.monitoring.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Issues health check request to underlying DB server and provided
 * appropriate health check response
 *
 * @author Vitaliy Dragun
 */
public class DBHealthCheck extends HealthCheck {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBHealthCheck.class);

    private final DataSourceStateProvider stateProvider;

    public DBHealthCheck(final DataSourceStateProvider stateProvider) {
        this.stateProvider = stateProvider;
    }

    @Override
    protected Result check() throws Exception {
        try {
            stateProvider.healthCheck();

            LOGGER.info("Database health check is successful");

            return Result.healthy();
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Result.unhealthy(e);
        }
    }
}
