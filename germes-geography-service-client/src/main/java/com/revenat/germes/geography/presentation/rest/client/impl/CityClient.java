package com.revenat.germes.geography.presentation.rest.client.impl;

import com.revenat.germes.geography.presentation.rest.client.CityFacade;
import com.revenat.germes.geography.presentation.rest.dto.CityDTO;
import com.revenat.germes.common.core.shared.exception.CommunicationException;
import com.revenat.germes.common.core.shared.exception.flow.HttpRestException;
import com.revenat.germes.common.infrastructure.http.RestClient;
import com.revenat.germes.common.infrastructure.http.RestResponse;

import java.util.Arrays;
import java.util.List;

/**
 * Implementation of the {@link CityFacade} that uses
 * {@link RestClient} to manage city instances via RESTful
 * resource
 *
 * @author Vitaliy Dragun
 */
public class CityClient implements CityFacade {

    private static final String HEADER_LOCATION = "Location";

    private final String baseUrl;

    private final RestClient restClient;

    public CityClient(final String baseUrl, final RestClient restClient) {
        this.baseUrl = baseUrl;
        this.restClient = restClient;
    }

    @Override
    public List<CityDTO> findAll() {
        try {
            return Arrays.asList(restClient.get(baseUrl, CityDTO[].class).getBody());
        } catch (HttpRestException e) {
            throw new CommunicationException("Error finding cities", e);
        }
    }

    @Override
    public String create(final CityDTO city) {
        try {
            final RestResponse<CityDTO> response = restClient.post(baseUrl, CityDTO.class, city);
            return response.getHeader(HEADER_LOCATION).get(0);
        } catch (HttpRestException e) {
            throw new CommunicationException("Error creating city", e);
        }
    }

    @Override
    public CityDTO update(final CityDTO city) {
        try {
            return restClient.put(baseUrl + "/" + city.getId(), CityDTO.class, city).getBody();
        } catch (HttpRestException e) {
            throw new CommunicationException("Error updating city", e);
        }
    }

    @Override
    public void delete(final int id) {
        try {
            restClient.delete(baseUrl + "/" + id);
        } catch (HttpRestException e) {
            throw new CommunicationException("Error deleting city", e);
        }
    }
}
