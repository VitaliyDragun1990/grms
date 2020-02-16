package com.revenat.germes.model.entity.base;

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

    public static final String FIELD_ID = "id";

    public static final String FIELD_CREATED_AT = "createdAt";

    private int id;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    /**
     * Identifier of the user who created entity
     */
    private String createdBy;

    /**
     * Identifier of the user who created entity
     */
    private String modifiedBy;

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

    @Column(name = "CREATED_BY", updatable = false)
    public String getCreatedBy() {
        return createdBy;
    }

    @Column(name = "MODIFIED_BY", insertable = false)
    public String getModifiedBy() {
        return modifiedBy;
    }

}
