package com.revenat.germes.user.presentation.rest.client.impl;

import com.revenat.germes.user.presentation.rest.client.UserFacade;
import com.revenat.germes.user.presentation.rest.dto.LoginDTO;
import com.revenat.germes.user.presentation.rest.dto.UserDTO;

/**
 * @author Vitaliy Dragun
 */
public class UserClient implements UserFacade {

    @Override
    public UserDTO login(final LoginDTO login) {
        // Stub implementation
        final UserDTO user = new UserDTO();
        user.setUserName(login.getUserName());
        return user;
    }
}
