package com.revenat.germes.persistence.repository;

import com.revenat.germes.infrastructure.exception.PersistenceException;

/**
 * Provides system-related operations
 *
 * @author Vitaliy Dragun
 */
public interface SystemRepository {

    /**
     * Checks the availability of the underlying data-source
     * @throws PersistenceException if the underlying data-source in not available
     */
    void healthCheck() throws PersistenceException;
}
