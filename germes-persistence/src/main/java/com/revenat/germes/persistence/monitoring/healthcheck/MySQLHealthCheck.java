package com.revenat.germes.persistence.monitoring.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import com.revenat.germes.persistence.hibernate.SessionFactoryBuilder;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
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

    private final SessionFactoryBuilder sessionFactoryBuilder;

    public MySQLHealthCheck(final SessionFactoryBuilder sessionFactoryBuilder) {
        this.sessionFactoryBuilder = sessionFactoryBuilder;
    }

    @Override
    protected Result check() throws Exception {
        try (final Session session = sessionFactoryBuilder.getSessionFactory().openSession()) {
            final NativeQuery<?> query = session.createNativeQuery("SHOW TABLES");
            query.getResultList();

            LOGGER.info("MYSQL health check is successful");

            return HealthCheck.Result.healthy();
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            return HealthCheck.Result.unhealthy(e);
        }
    }
}
