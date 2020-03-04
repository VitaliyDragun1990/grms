package com.revenat.germes.user.core.application;

import com.revenat.germes.common.core.shared.helper.Asserts;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Provides common info about user
 *
 * @author Vitaliy Dragun
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfo {

    private String userName;

    public UserInfo(final String userName) {
        setUserName(userName);
    }

    private void setUserName(final String userName) {
        Asserts.assertNotNullOrBlank(userName, "userName can not be null or blank");
        this.userName = userName;
    }
}
