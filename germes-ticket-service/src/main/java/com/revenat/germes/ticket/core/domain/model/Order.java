package com.revenat.germes.ticket.core.domain.model;

import com.revenat.germes.common.core.domain.model.AbstractEntity;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Order (booking/reservation) of the ticket by user
 *
 * @author Vitaliy Dragun
 */
@Entity
@Table(name = "ORDERS")
@Setter
public class Order extends AbstractEntity {

    private static final Logger LOGGER = LoggerFactory.getLogger(Order.class);

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
    private String tripId;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "ORDER_STATE", nullable = false)
    public OrderState getState() {
        return state;
    }

    @Column(name = "DUE_DATE", nullable = false)
    public LocalDateTime getDueDate() {
        return dueDate;
    }

    @Column(name = "TRIP_ID", nullable = false)
    public String getTripId() {
        return tripId;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.EAGER)
    @JoinColumn(name = "TICKET_ID")
    public Ticket getTicket() {
        return ticket;
    }

    @Column(name = "CLIENT_NAME", length = 32, nullable = false)
    public String getClientName() {
        return clientName;
    }

    @Column(name = "CLIENT_PHONE", nullable = false, length = 24)
    public String getClientPhone() {
        return clientPhone;
    }

    @Column(name = "CANCELLATION_REASON", length = 128)
    public String getCancellationReason() {
        return cancellationReason;
    }

    @Transient
    public boolean isCompleted() {
        return state == OrderState.COMPLETED;
    }

    @Transient
    public boolean isCancelled() {
        return state == OrderState.CANCELLED;
    }

    /**
     * Cancels current order
     *
     * @param reason cancellation reason
     */
    public void cancel(final String reason) {
        if (dueDate.isBefore(LocalDateTime.now())) {
            LOGGER.warn("This order misses due date and should be automatically cancelled, id: {}", getId());
        }
        state = OrderState.CANCELLED;
        cancellationReason = reason;
    }

    /**
     * Makes necessary checks and completes the order
     *
     * @throws ReservationException if order can not be complete for some reason
     */
    public void complete() {
        if (dueDate.isBefore(LocalDateTime.now())) {
            throw new ReservationException("This order misses due date, id: " + getId());
        }
        state = OrderState.COMPLETED;
    }

    @PrePersist
    void setCreatedAt() {
        if (getCreatedAt() == null) {
            setCreatedAt(LocalDateTime.now());
        }
    }
}
