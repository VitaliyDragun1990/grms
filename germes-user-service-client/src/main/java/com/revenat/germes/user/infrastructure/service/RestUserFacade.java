package com.revenat.germes.user.infrastructure.service;

import com.revenat.germes.common.core.shared.exception.CommunicationException;
import com.revenat.germes.common.core.shared.exception.flow.HttpRestException;
import com.revenat.germes.common.infrastructure.http.RestClient;
import com.revenat.germes.common.infrastructure.http.RestResponse;
import com.revenat.germes.user.core.application.LoginInfo;
import com.revenat.germes.user.core.application.UserInfo;
import com.revenat.germes.user.core.application.UserFacade;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents adapter that makes request using REST client
 * and transforms obtained response in accordance to application-specific
 * context
 *
 * @author Vitaliy Dragun
 */
@RequiredArgsConstructor
public class RestUserFacade implements UserFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestUserFacade.class);

    private static final int UNAUTHORIZED = 401;

    private final String baseUrl;

    private final RestClient restClient;

    @Override
    public UserInfo login(final LoginInfo login) {
        try {
            LOGGER.debug("Sending login request at URL: '{}{}'", baseUrl, "/login");

            final RestResponse<UserInfo> response = restClient.post(baseUrl + "/login", UserInfo.class, login);

            if (!response.isSuccess()) {
                return null;
            }

            return response.getBody();
        } catch (final HttpRestException e) {
            if (e.isStatus(UNAUTHORIZED)) {
                return null;
            }
            throw new CommunicationException("Error authenticating user: " + e.getMessage(), e);
        }
    }
}
