package com.revenat.germes.application.model.entity.base;

import com.revenat.germes.application.model.entity.person.Account;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Base class for all business entities
 *
 * @author Vitaliy Dragun
 */
public abstract class AbstractEntity {

    private int id;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private Account createdBy;

    private Account modifiedBy;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(final LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Account getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final Account createdBy) {
        this.createdBy = createdBy;
    }

    public Account getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(final Account modifiedBy) {
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
