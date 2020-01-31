package com.revenat.germes.application.model.entity.person;

import com.revenat.germes.application.model.entity.base.AbstractEntity;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity that encapsulates user of the application
 *
 * @author Vitaliy Dragun
 */
@Table(name = "USERS")
@Entity
@NamedQueries({
        @NamedQuery(name = User.QUERY_FIND_ALL, query = "from User"),
        @NamedQuery(name = User.QUERY_FIND_BY_USERNAME, query = "from User u where u.userName = :userName")
})
@Setter
public class User extends AbstractEntity {

    public static final String QUERY_FIND_ALL = "User.findAll";

    public static final String QUERY_FIND_BY_USERNAME = "User.findByUsername";

    private String userName;

    private String password;

    @NotNull
    @Size(min = 5, max = 24)
    @Column(name = "USERNAME", nullable = false, unique = true, length = 24)
    public String getUserName() {
        return userName;
    }

    @NotNull
    @Size(min = 5, max = 256)
    @Column(name = "PASSWORD", nullable = false, length = 256)
    public String getPassword() {
        return password;
    }
}
