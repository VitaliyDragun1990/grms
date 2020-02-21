package com.revenat.germes.user.presentation.rest.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides user authentication-specific information
 *
 * @author Vitaliy Dragun
 */
@Getter
@Setter
public class LoginDTO {

    private String userName;

    private String hashedPassword;
}
