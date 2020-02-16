package com.revenat.germes.ticket.model.entity;

import com.revenat.germes.infrastructure.helper.Asserts;
import com.revenat.germes.model.entity.base.AbstractEntity;
import com.revenat.germes.ticket.model.generator.TicketNumberGenerator;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Trip ticket
 *
 * @author Vitaliy Dragun
 */
@Entity
@Table(name = "TICKETS")
@NamedQuery(name = Ticket.QUERY_FIND_ALL_BY_TRIP, query = "from Ticket t where t.trip = :trip")
public class Ticket extends AbstractEntity {

    public static final String QUERY_FIND_ALL_BY_TRIP = "Ticket.findAll";

    public static final int TICKET_NUMBER_SIZE = 32;

    /**
     * Link to the underlying trip
     */
    @Setter
    private String trip;

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
    public String getTrip() {
        return trip;
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

}
