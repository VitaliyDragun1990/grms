package com.revenat.germes.user.presentation.rest.dto;

import lombok.*;

/**
 * Provides user authentication-specific information
 *
 * @author Vitaliy Dragun
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    private String userName;

    private String hashedPassword;
}
