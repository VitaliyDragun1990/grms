package com.revenat.germes.user.core.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity that encapsulates user of the application
 *
 * @author Vitaliy Dragun
 */
@Table(name = "USERS")
@Entity
@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private UUID id;

    @Column(name = "USERNAME", nullable = false, unique = true, length = 24)
    private String userName;

    @Column(name = "PASSWORD", nullable = false, length = 80)
    private String password;

    @Column(name = "FIRST_NAME", nullable = false, length = 24)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false, length = 24)
    private String lastName;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "REGISTRATION_IP", length = 30)
    private String registrationIp;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private Role role;

    @PrePersist
    void setCreatedAt() {
        if (getCreatedAt() == null) {
            setCreatedAt(LocalDateTime.now());
        }
    }
}
