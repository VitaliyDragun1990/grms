package com.revenat.germes.presentation.admin.client;

import com.revenat.germes.geography.presentation.rest.dto.CityDTO;
import com.revenat.germes.infrastructure.exception.CommunicationException;

import java.util.List;

/**
 * Abstraction responsible for communicating with city-related RESTful resources
 * Hides implementation details about how such communication is achieved.
 *
 * @author Vitaliy Dragun
 */
public interface CityFacade {

    /**
     * Returns list of existing cities
     *
     * @throws CommunicationException if something goes wrong during communication with RESTful resource
     */
    List<CityDTO> findAll();

    /**
     * Creates new city instance and returns URI (location) of newly created city resource
     *
     * @param city city instance to create
     * @throws CommunicationException if something goes wrong during communication with RESTful resource
     */
    String create(final CityDTO city);

    /**
     * Updates existing city instance
     *
     * @param city city instance to update
     * @return updated city instance
     * @throws CommunicationException if something goes wrong during communication with RESTful resource
     */
    CityDTO update(final CityDTO city);

    /**
     * Deletes existing city instance by identifier
     *
     * @param id identifier of the city to delete
     * @throws CommunicationException if something goes wrong during communication with RESTful resource
     */
    void delete(final int id);
}
