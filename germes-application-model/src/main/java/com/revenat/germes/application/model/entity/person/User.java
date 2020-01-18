package com.revenat.germes.application.model.entity.person;

import com.revenat.germes.application.model.entity.base.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity that encapsulates user of the application
 *
 * @author Vitaliy Dragun
 */
@Table(name = "USERS")
@Entity
public class User extends AbstractEntity {

    private String userName;

    private String password;

    @Column(name = "USERNAME", nullable = false, unique = true, length = 24)
    public String getUserName() {
        return userName;
    }

    @Column(name = "PASSWORD", nullable = false, length = 24)
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
