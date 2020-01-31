package com.revenat.germes.application.model.entity.travel;

import com.revenat.germes.application.model.entity.base.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Trip ticket
 *
 * @author Vitaliy Dragun
 */
@Entity
@Table(name = "TICKETS")
@AssociationOverride(name = "createdBy", joinColumns = @JoinColumn(name = "CREATED_BY", updatable = false, nullable = false))
public class Ticket extends AbstractEntity {

    /**
     * Link to the underlying trip
     */
    private Trip trip;

    /**
     * Client name/surname
     */
    private String clientName;

    /**
     * Auto-generated ticket identifier (usually random)
     */
    private String uid;

    @NotNull
    @ManyToOne(cascade = {}, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "TRIP_ID", nullable = false)
    public Trip getTrip() {
        return trip;
    }

    @NotBlank
    @Size(max = 32)
    @Column(name = "CLIENT_NAME", length = 32, nullable = false)
    public String getClientName() {
        return clientName;
    }

    @NotBlank
    @Size(max = 60)
    @Column(length = 60, nullable = false, unique = true)
    public String getUid() {
        return uid;
    }

}
