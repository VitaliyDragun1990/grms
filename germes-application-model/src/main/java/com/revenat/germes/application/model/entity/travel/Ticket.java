package com.revenat.germes.application.model.entity.travel;

import com.revenat.germes.application.infrastructure.helper.Checker;
import com.revenat.germes.application.infrastructure.helper.generator.text.StringGenerator;
import com.revenat.germes.application.model.entity.base.AbstractEntity;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.function.Supplier;

/**
 * Trip ticket
 *
 * @author Vitaliy Dragun
 */
@Entity
@Table(name = "TICKETS")
@NamedQuery(name = Ticket.QUERY_FIND_ALL, query = "from Ticket")
//@AssociationOverride(name = "createdBy", joinColumns = @JoinColumn(name = "CREATED_BY", updatable = false, nullable = false))
@Setter
public class Ticket extends AbstractEntity {

    public static final String QUERY_FIND_ALL = "Ticket.findAll";

    public static final int TICKET_NUMBER_SIZE = 32;

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

    /**
     * Generates system-unique ticket number
     *
     * @param uidGenerator  string generator that should generate unique uid strings
     */
    public void generateUid(final StringGenerator uidGenerator) {
        new Checker().checkParameter(uidGenerator != null, "uidGenerator should be initialized");
        uid = uidGenerator.generate();
    }

}
