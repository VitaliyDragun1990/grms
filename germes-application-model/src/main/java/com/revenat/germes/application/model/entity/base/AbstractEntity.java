package com.revenat.germes.application.model.entity.base;

import com.revenat.germes.application.model.entity.person.User;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Base class for all business entities
 *
 * @author Vitaliy Dragun
 */
@MappedSuperclass
@Setter
@EqualsAndHashCode(of = "id")
public abstract class AbstractEntity {

    public static final String FIELD_CREATED_AT = "createdAt";

    private int id;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private User createdBy;

    private User modifiedBy;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Column(name = "MODIFIED_AT", insertable = false)
    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = {})
    @JoinColumn(name = "CREATED_BY", updatable = false)
    public User getCreatedBy() {
        return createdBy;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = {})
    @JoinColumn(name = "MODIFIED_BY", insertable = false)
    public User getModifiedBy() {
        return modifiedBy;
    }

}
