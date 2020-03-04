package com.revenat.germes.ticket.core.domain.model;

/**
 * All supported states of the ticket order
 *
 * @author Vitaliy Dragun
 */
public enum OrderState {
    CREATED,
    PENDING,
    CANCELLED,
    COMPLETED;
}
