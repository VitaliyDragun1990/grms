package com.revenat.germes.application.monitoring.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import com.revenat.germes.persistence.repository.SystemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Issues health check request to MySQL server and provided
 * appropriate health check response
 *
 * @author Vitaliy Dragun
 */
public class MySQLHealthCheck extends HealthCheck {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLHealthCheck.class);

    private final SystemRepository systemRepository;

    public MySQLHealthCheck(final SystemRepository systemRepository) {
        this.systemRepository = systemRepository;
    }

    @Override
    protected Result check() throws Exception {
        try {
            systemRepository.healthCheck();

            LOGGER.info("MYSQL health check is successful");

            return HealthCheck.Result.healthy();
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            return HealthCheck.Result.unhealthy(e);
        }
    }
}
