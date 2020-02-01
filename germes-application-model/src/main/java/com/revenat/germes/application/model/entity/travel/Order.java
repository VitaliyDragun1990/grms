package com.revenat.germes.application.model.entity.travel;

import com.revenat.germes.application.model.entity.base.AbstractEntity;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Order (booking/reservation) of the ticket by user
 *
 * @author Vitaliy Dragun
 */
// TODO: make user who created such Order obligatory attribute (createdBy property)
@Entity
@Table(name = "ORDERS")
@Setter
public class Order extends AbstractEntity {

    public static final String FIELD_TRIP = "trip";

    /**
     * Current order state
     */
    private OrderState state;

    /**
     * Date/time when user should pay for the order(ticket)
     */
    private LocalDateTime dueDate;

    /**
     * Link to the ticket's trip
     */
    private Trip trip;

    /**
     * Link to the payed ticket(if order is completed)
     */
    private Ticket ticket;

    /**
     * Client name/surname
     */
    private String clientName;

    /**
     * Client contact phone for communication
     */
    private String clientPhone;

    /**
     * If the order was cancelled then it's the reason for cancellation
     */
    private String cancellationReason;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "ORDER_STATE", nullable = false)
    public OrderState getState() {
        return state;
    }

    @NotNull
    @Column(name = "DUE_DATE", nullable = false)
    public LocalDateTime getDueDate() {
        return dueDate;
    }

    @NotNull
    @ManyToOne(cascade = {}, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "TRIP_ID", nullable = false)
    public Trip getTrip() {
        return trip;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.EAGER)
    @JoinColumn(name = "TICKET_ID")
    public Ticket getTicket() {
        return ticket;
    }

    @NotBlank
    @Size(max = 32)
    @Column(name = "CLIENT_NAME", length = 32, nullable = false)
    public String getClientName() {
        return clientName;
    }

    @NotBlank
    @Size(max = 24)
    @Column(name = "CLIENT_PHONE", nullable = false, length = 24)
    public String getClientPhone() {
        return clientPhone;
    }

    @Size(max = 128)
    @Column(name = "CANCELLATION_REASON", length = 128)
    public String getCancellationReason() {
        return cancellationReason;
    }
}
