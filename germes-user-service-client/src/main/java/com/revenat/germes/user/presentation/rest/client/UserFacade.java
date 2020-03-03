package com.revenat.germes.user.presentation.rest.client;

import com.revenat.germes.user.presentation.rest.dto.LoginInfo;
import com.revenat.germes.user.presentation.rest.dto.UserInfo;
import com.revenat.germes.common.core.shared.exception.CommunicationException;

/**
 * Abstraction responsible for communicating with user-related RESTful resources
 * Hides implementation details about how such communication is achieved.
 *
 * @author Vitaliy Dragun
 */
public interface UserFacade {

    /**
     * Try to log in existing user using provided login information
     * @param login information to log in
     * @return object representing logged in user
     * @throws CommunicationException if something goes wrong during communication with RESTful resource
     */
    UserInfo login(LoginInfo login);
}
