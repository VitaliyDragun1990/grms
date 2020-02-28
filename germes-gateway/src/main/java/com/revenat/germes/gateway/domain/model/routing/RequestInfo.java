package com.revenat.germes.gateway.domain.model.routing;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Component that contains all required info about client's request that
 * should be routed
 *
 * @author Vitaliy Dragun
 */
@RequiredArgsConstructor
@Getter
public class RequestInfo {

    private final String serverPrefix;

    private final String path;

    private final Object body;

    private final Map<String, List<String>> headers;

    private final String queryString;

    private final String method;
}
