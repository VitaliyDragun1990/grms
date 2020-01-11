package com.revenat.germes.application.model.entity.base;

import com.revenat.germes.application.model.entity.person.Account;

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

    private int id;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private Account createdBy;

    private Account modifiedBy;

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
    public Account getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final Account createdBy) {
        this.createdBy = createdBy;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = {})
    @JoinColumn(name = "MODIFIED_BY", insertable = false)
    public Account getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(final Account modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public void prePersist() {
        if (getId() == 0) {
            setCreatedAt(LocalDateTime.now());
        }
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
