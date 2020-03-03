package com.revenat.germes.gateway.domain.model.routing;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * Component that contains all required data extracted from response from remote server(service)
 * Protocol-agnostic
 *
 * @author Vitaliy Dragun
 */
@Getter
@RequiredArgsConstructor
@ToString
public class ResponseInfo {

    private final int statusCode;

    private final String body;

    private final Map<String, List<String>> headers;
}
