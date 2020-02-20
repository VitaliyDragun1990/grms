package com.revenat.germes.presentation.admin.client;

import com.revenat.germes.geography.presentation.rest.dto.CityDTO;
import com.revenat.germes.infrastructure.environment.Environment;
import com.revenat.germes.infrastructure.http.RestClient;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.List;

/**
 * Abstraction responsible for communication with geography service about city instances
 * Hides implementation details about how such communication is achieved.
 *
 * @author Vitaliy Dragun
 */
@Named
@Dependent
public class CityClient {

    private final String baseUrl;

    private final RestClient restClient;

    @Inject
    public CityClient(final Environment env, final RestClient restClient) {
        baseUrl = env.getProperty("client.geography.url") + "/cities";
        this.restClient = restClient;
    }

    public List<CityDTO> findAll() {
        return Arrays.asList(restClient.get(baseUrl, CityDTO[].class).getBody());
    }

    public CityDTO create(final CityDTO city) {
        return restClient.post(baseUrl, CityDTO.class, city).getBody();
    }

    public CityDTO update(final CityDTO city) {
        return restClient.put(baseUrl + "/" + city.getId(), CityDTO.class, city).getBody();
    }

    public void delete(final int id) {
        restClient.delete(baseUrl + "/" + id);
    }
}
