package com.revenat.germes.gateway.core.routing;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

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
@ToString
public class RequestInfo {

    private final String serverPrefix;

    private final String path;

    private final Object body;

    private final Map<String, List<String>> headers;

    private final String queryString;

    private final String method;
}
