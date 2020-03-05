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

    private String id;

    private String userName;

    private String firstName;

    private String lastName;

    private String role;

    private UserInfo(final String id, final String userName, final String firstName, final String lastName, final String role) {
        setId(id);
        setUserName(userName);
        setFirstName(firstName);
        setLastName(lastName);
        setRole(role);
    }

    private void setUserName(final String userName) {
        Asserts.assertNotNullOrBlank(userName, "userName can not be null or blank");
        this.userName = userName;
    }

    private void setId(final String id) {
        Asserts.assertNotNullOrBlank(id, "id can not be null or blank");
        this.id = id;
    }

    private void setFirstName(final String firstName) {
        Asserts.assertNotNullOrBlank(firstName, "firstName can not be null or blank");
        this.firstName = firstName;
    }

    private void setLastName(final String lastName) {
        Asserts.assertNotNullOrBlank(lastName, "lastName can not be null or blank");
        this.lastName = lastName;
    }

    private void setRole(final String role) {
        Asserts.assertNotNullOrBlank(role, "role can not be null or blank");
        this.role = role;
    }

    public static class Builder {

        private String id;

        private String userName;

        private String firstName;

        private String lastName;

        private String role;

        public Builder withId(final String id) {
            this.id = id;
            return this;
        }

        public Builder withUserName(final String userName) {
            this.userName = userName;
            return this;
        }

        public Builder withFirstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withRole(final String role) {
            this.role = role;
            return this;
        }

        public UserInfo build() {
            return new UserInfo(id, userName, firstName, lastName, role);
        }
    }
}
