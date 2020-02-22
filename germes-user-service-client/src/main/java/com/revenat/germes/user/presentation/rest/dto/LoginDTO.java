package com.revenat.germes.user.presentation.rest.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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

    @NotBlank
    @Size(min = 5)
    private String userName;

    @NotBlank
    @Size(min = 5)
    private String hashedPassword;
}
