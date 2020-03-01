package com.revenat.germes.user.domain.model;

import com.revenat.germes.model.entity.base.AbstractEntity;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Entity that encapsulates user of the application
 *
 * @author Vitaliy Dragun
 */
@Table(name = "USERS")
@Entity
@Setter
public class User extends AbstractEntity {

    private String userName;

    private String password;

    @Column(name = "USERNAME", nullable = false, unique = true, length = 24)
    public String getUserName() {
        return userName;
    }

    @Column(name = "PASSWORD", nullable = false, length = 256)
    public String getPassword() {
        return password;
    }

    @PrePersist
    void setCreatedAt() {
        if (getCreatedAt() == null) {
            setCreatedAt(LocalDateTime.now());
        }
    }
}
