package com.revenat.germes.gateway.presentation.routing.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Objects that contains all required info about request
 *
 * @author Vitaliy Dragun
 */
@RequiredArgsConstructor
@Getter
class RequestInfo {

    private final String serverPrefix;

    private final String path;

    private final Object body;

    private final Map<String, String[]> parameters;

    private final String queryString;
}
