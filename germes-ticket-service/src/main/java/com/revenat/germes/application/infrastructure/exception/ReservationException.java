package com.revenat.germes.application.infrastructure.exception;

/**
 * Signals about incorrect state or incorrect parameters in the reservation flow
 *
 * @author Vitaliy Dragun
 */
public class ReservationException extends FlowException {

    private static final long serialVersionUID = -2604240334507663745L;

    public ReservationException(final String message) {
        super(message);
    }
}
