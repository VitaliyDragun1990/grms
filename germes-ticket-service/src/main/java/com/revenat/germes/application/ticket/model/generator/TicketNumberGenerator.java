package com.revenat.germes.application.ticket.model.generator;

import com.revenat.germes.application.infrastructure.helper.generator.text.RandomDigitStringGenerator;
import com.revenat.germes.application.ticket.model.entity.Ticket;

/**
 * Serves as a factory to generate ticket numbers
 *
 * @author Vitaliy Dragun
 */
public class TicketNumberGenerator extends RandomDigitStringGenerator {

    public TicketNumberGenerator() {
        super(Ticket.TICKET_NUMBER_SIZE);
    }
}
