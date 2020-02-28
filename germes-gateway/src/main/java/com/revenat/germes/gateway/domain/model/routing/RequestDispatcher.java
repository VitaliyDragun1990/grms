package com.revenat.germes.gateway.domain.model.routing;

/**
 * Abstraction over transferring client request to the remote server(service)
 * Can invoke protocol/data format translation, for example,
 * JSON -> Protobuf
 * HTTP -> TCP
 *
 * @author Vitaliy Dragun
 */
@FunctionalInterface
public interface RequestDispatcher {

    /**
     * Transfers client request to the remote server(service) and
     * returns server response
     *
     * @param requestInfo info about client request
     *
     * @return protocol-agnostic {@link ResponseInfo} with data from received response
     */
    ResponseInfo dispatchRequest(RequestInfo requestInfo);
}
