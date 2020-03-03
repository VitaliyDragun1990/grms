package com.revenat.germes.user.presentation.rest.dto;

import com.revenat.germes.common.core.shared.helper.Asserts;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Provides user authentication-specific information
 *
 * @author Vitaliy Dragun
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginInfo {

    @NotBlank
    @Size(min = 5)
    private String userName;

    @NotBlank
    @Size(min = 5)
    private String hashedPassword;

    public LoginInfo(final String userName, final String hashedPassword) {
        setUserName(userName);
        setHashedPassword(hashedPassword);
    }

    private void setHashedPassword(final String hashedPassword) {
        Asserts.assertNotNullOrBlank(hashedPassword, "hashedPassword can not be null or blank");
        this.hashedPassword = hashedPassword;
    }

    private void setUserName(final String userName) {
        Asserts.assertNotNullOrBlank(userName, "userName can not be null or blank");
        this.userName = userName;
    }
}
