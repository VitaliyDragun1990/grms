package com.revenat.germes.application.model.entity.base;

import com.revenat.germes.application.model.entity.person.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Base class for all business entities
 *
 * @author Vitaliy Dragun
 */
@MappedSuperclass
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

    public void setId(final int id) {
        this.id = id;
    }

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Column(name = "MODIFIED_AT", insertable = false)
    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(final LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = {})
    @JoinColumn(name = "CREATED_BY", updatable = false)
    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final User createdBy) {
        this.createdBy = createdBy;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = {})
    @JoinColumn(name = "MODIFIED_BY", insertable = false)
    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(final User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AbstractEntity that = (AbstractEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
