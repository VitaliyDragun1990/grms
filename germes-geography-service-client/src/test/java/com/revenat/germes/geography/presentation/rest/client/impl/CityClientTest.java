package com.revenat.germes.geography.presentation.rest.client.impl;

import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.revenat.germes.geography.presentation.rest.client.CityFacade;
import com.revenat.germes.geography.presentation.rest.dto.CityDTO;
import com.revenat.germes.infrastructure.exception.CommunicationException;
import com.revenat.germes.infrastructure.http.RestClient;
import com.revenat.germes.infrastructure.http.impl.JavaRestClient;
import com.revenat.germes.infrastructure.json.JsonClient;
import com.revenat.germes.infrastructure.json.impl.GsonJsonClient;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.setGlobalFixedDelay;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vitaliy Dragun
 */
public class CityClientTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(options().dynamicPort());

    private JsonClient jsonClient;

    private CityFacade cityClient;

    private int timeoutInSeconds = 3;

    @Before
    public void setUp() throws Exception {
        jsonClient = new GsonJsonClient();
        RestClient restClient = new JavaRestClient(timeoutInSeconds, jsonClient);
        cityClient = new CityClient(getCityServiceUrl(wireMockRule.port()), restClient);
    }

    @Test
    public void whenCreatingNewCityShouldReturnCreatedResourceLocation() {
        final String expectedLocation = getCityServiceUrl(wireMockRule.port()) + "/1";
        wireMockRule.stubFor(post(urlEqualTo("/")).willReturn(
                aResponse()
                        .withStatus(200)
                        .withHeader("Location", expectedLocation)));

        CityDTO city = new CityDTO();
        city.setName("Odessa");
        city.setDistrict("Odessa");
        city.setRegion("Odessa");

        final String location = cityClient.create(city);

        assertThat(location, is(expectedLocation));
    }

    @Test(expected = CommunicationException.class)
    public void shouldFailIWhenCreateCityIfServerReturnStatusCode500() {
        wireMockRule.stubFor(post(urlEqualTo("/")).willReturn(
                aResponse()
                        .withStatus(500)));

        CityDTO city = new CityDTO();
        city.setName("Odessa");
        city.setDistrict("Odessa");
        city.setRegion("Odessa");

        cityClient.create(city);
    }

    @Test(expected = CommunicationException.class)
    public void shouldFailIWhenCreateCityIfServerReturnEmptyResponse() {
        wireMockRule.stubFor(post(urlEqualTo("/")).willReturn(
                aResponse()
                        .withFault(Fault.EMPTY_RESPONSE)));

        CityDTO city = new CityDTO();
        city.setName("Odessa");
        city.setDistrict("Odessa");
        city.setRegion("Odessa");

        cityClient.create(city);
    }

    @Test(expected = CommunicationException.class)
    public void shouldFailIWhenCreateCityIfServerReturnMalformedResponse() {
        wireMockRule.stubFor(post(urlEqualTo("/")).willReturn(
                aResponse()
                        .withFault(Fault.MALFORMED_RESPONSE_CHUNK)));

        CityDTO city = new CityDTO();
        city.setName("Odessa");
        city.setDistrict("Odessa");
        city.setRegion("Odessa");

        cityClient.create(city);
    }

    @Test(expected = CommunicationException.class)
    public void shouldFailIWhenCreateCityIfServerReturnRandomData() {
        wireMockRule.stubFor(post(urlEqualTo("/")).willReturn(
                aResponse()
                        .withFault(Fault.RANDOM_DATA_THEN_CLOSE)));

        CityDTO city = new CityDTO();
        city.setName("Odessa");
        city.setDistrict("Odessa");
        city.setRegion("Odessa");

        cityClient.create(city);
    }

    @Test(expected = CommunicationException.class)
    public void shouldFailWhenDeleteCityIfServerReturnsStatusCode500() {
        wireMockRule.stubFor(delete(urlEqualTo("/1")).willReturn(
                aResponse()
                        .withStatus(500)));

        cityClient.delete(1);
    }

    @Test
    public void shouldReturnUpdatedCityWhenUpdateCity() {
        CityDTO city = new CityDTO();
        city.setId(1);
        city.setName("Odessa");
        city.setDistrict("Odessa");
        city.setRegion("Odessa");

        wireMockRule.stubFor(put(urlEqualTo("/1")).willReturn(
                aResponse()
                        .withBody(jsonClient.toJson(city))
                        .withStatus(200)));

        final CityDTO result = cityClient.update(city);

        assertNotNull(result, "updated city should not be null");
    }

    @Test(expected = CommunicationException.class)
    public void shouldFailIWhenUpdateCityIfServerReturnMalformedResponse() {
        CityDTO city = new CityDTO();
        city.setId(1);
        city.setName("Odessa");
        city.setDistrict("Odessa");
        city.setRegion("Odessa");

        wireMockRule.stubFor(put(urlEqualTo("/1")).willReturn(
                aResponse()
                        .withFault(Fault.MALFORMED_RESPONSE_CHUNK)));

        cityClient.update(city);
    }

    @Test(expected = CommunicationException.class)
    public void shouldFailIWhenUpdateCityIfServerReturnRandomData() {
        CityDTO city = new CityDTO();
        city.setId(1);
        city.setName("Odessa");
        city.setDistrict("Odessa");
        city.setRegion("Odessa");

        wireMockRule.stubFor(put(urlEqualTo("/1")).willReturn(
                aResponse()
                        .withFault(Fault.RANDOM_DATA_THEN_CLOSE)));

        cityClient.update(city);
    }

    @Test(expected = CommunicationException.class)
    public void shouldFailIWhenUpdateCityIfServerReturnEmptyResponse() {
        CityDTO city = new CityDTO();
        city.setId(1);
        city.setName("Odessa");
        city.setDistrict("Odessa");
        city.setRegion("Odessa");

        wireMockRule.stubFor(put(urlEqualTo("/1")).willReturn(
                aResponse()
                        .withFault(Fault.EMPTY_RESPONSE)));

        cityClient.update(city);
    }

    @Test
    public void shouldReturnCitiesWhenFindAll() {
        CityDTO cityA = new CityDTO();
        cityA.setId(1);
        cityA.setName("Odessa");
        cityA.setDistrict("Odessa");
        cityA.setRegion("Odessa");
        CityDTO cityB = new CityDTO();
        cityB.setId(2);
        cityB.setName("Kiyv");
        cityB.setDistrict("Kiyv");
        cityB.setRegion("Kiyv");
        wireMockRule.stubFor(get(urlEqualTo("/")).willReturn(
                aResponse()
                        .withBody(jsonClient.toJson(new CityDTO[] {cityA, cityB}))));

        final List<CityDTO> result = cityClient.findAll();

        assertThat(result, hasSize(2));
    }

    @Test(expected = CommunicationException.class)
    public void shouldFailWhenFindAllIfServerReturnEmptyResponse() {
        wireMockRule.stubFor(get(urlEqualTo("/")).willReturn(
                aResponse()
                        .withFault(Fault.EMPTY_RESPONSE)));

        cityClient.findAll();
    }

    @Test(expected = CommunicationException.class)
    public void shouldFailWhenFindAllIfServerReturnRandomData() {
        wireMockRule.stubFor(get(urlEqualTo("/")).willReturn(
                aResponse()
                        .withFault(Fault.RANDOM_DATA_THEN_CLOSE)));

        cityClient.findAll();
    }

    @Test(expected = CommunicationException.class)
    public void shouldFailWhenFindAllIfServerReturnMalformedResponse() {
        wireMockRule.stubFor(get(urlEqualTo("/")).willReturn(
                aResponse()
                        .withFault(Fault.MALFORMED_RESPONSE_CHUNK)));

        cityClient.findAll();
    }

    @Test(expected = CommunicationException.class)
    public void shouldFailWhenFindAllIfServerDoesNotResponse() {
        wireMockRule.stubFor(get(urlEqualTo("/")).willReturn(
                aResponse()
                        .withBody((String)null)));
        setGlobalFixedDelay((timeoutInSeconds + 1) * 1_000);

        cityClient.findAll();
    }

    private String getCityServiceUrl(int port) {
        return "http://localhost:" + port;
    }
}