package com.revenat.germes.user.presentation.rest.client.impl;

import com.revenat.germes.infrastructure.exception.CommunicationException;
import com.revenat.germes.infrastructure.exception.flow.HttpRestException;
import com.revenat.germes.infrastructure.http.RestClient;
import com.revenat.germes.infrastructure.http.RestResponse;
import com.revenat.germes.user.presentation.rest.client.UserFacade;
import com.revenat.germes.user.presentation.rest.dto.LoginDTO;
import com.revenat.germes.user.presentation.rest.dto.UserDTO;
import lombok.RequiredArgsConstructor;

/**
 * @author Vitaliy Dragun
 */
@RequiredArgsConstructor
public class UserClient implements UserFacade {

    private final String baseUrl;

    private final RestClient restClient;

    @Override
    public UserDTO login(final LoginDTO login) {
        try {
            final RestResponse<UserDTO> response = restClient.post(baseUrl + "/login", UserDTO.class, login);
            if (!response.isSuccess()) {
                return null;
            }
            return response.getBody();
        } catch (final HttpRestException e) {
            throw new CommunicationException("Error authenticating user", e);
        }
    }
}
