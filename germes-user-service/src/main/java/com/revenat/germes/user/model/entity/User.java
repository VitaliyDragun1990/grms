package com.revenat.germes.user.model.entity;

import com.revenat.germes.model.entity.base.AbstractEntity;
import lombok.Setter;

import javax.persistence.*;

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

    @Column(name = "USERNAME", nullable = false, unique = true, length = 24)
    public String getUserName() {
        return userName;
    }

    @Column(name = "PASSWORD", nullable = false, length = 256)
    public String getPassword() {
        return password;
    }
}
