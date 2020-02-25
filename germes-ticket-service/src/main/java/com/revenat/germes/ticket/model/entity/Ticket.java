package com.revenat.germes.ticket.model.entity;

import com.revenat.germes.infrastructure.helper.Asserts;
import com.revenat.germes.model.entity.base.AbstractEntity;
import com.revenat.germes.ticket.model.generator.TicketNumberGenerator;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Trip ticket
 *
 * @author Vitaliy Dragun
 */
@Entity
@Table(name = "TICKETS")
public class Ticket extends AbstractEntity {

    public static final int TICKET_NUMBER_SIZE = 32;

    /**
     * Link to the underlying trip
     */
    @Setter
    private String tripId;

    /**
     * Client name/surname
     */
    @Setter
    private String clientName;

    /**
     * Auto-generated ticket identifier (usually random)
     */
    @Setter(AccessLevel.PACKAGE)
    private String uid;

    @Column(name = "TRIP_ID", nullable = false)
    public String getTripId() {
        return tripId;
    }

    @Column(name = "CLIENT_NAME", length = 32, nullable = false)
    public String getClientName() {
        return clientName;
    }

    @Column(length = 60, nullable = false, unique = true)
    public String getUid() {
        return uid;
    }

    /**
     * Generates system-unique ticket number
     *
     * @param numberGenerator  string generator that should generate unique uid strings
     */
    public void generateUid(final TicketNumberGenerator numberGenerator) {
        Asserts.assertNotNull(numberGenerator, "numberGenerator should be initialized");
        uid = numberGenerator.generate();
    }

    @PrePersist
    void setCreatedAt() {
        if (getCreatedAt() == null) {
            setCreatedAt(LocalDateTime.now());
        }
    }
}
