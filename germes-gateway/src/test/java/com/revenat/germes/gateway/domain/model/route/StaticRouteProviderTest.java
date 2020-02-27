package com.revenat.germes.gateway.domain.model.route;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("static route provider")
class StaticRouteProviderTest {

    private RouteProvider routeProvider = new StaticRouteProvider();

    @Test
    void shouldReturnValidPrefixes() {
        final List<String> result = routeProvider.getRoutePrefixes();

        assertThat(result, hasSize(greaterThan(0)));
        assertThat(result, everyItem(not(blankOrNullString())));
    }
}