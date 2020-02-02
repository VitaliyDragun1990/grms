package com.revenat.germes.application.model.entity.travel.generator;

import com.revenat.germes.application.infrastructure.helper.generator.text.RandomDigitStringGenerator;
import com.revenat.germes.application.model.entity.travel.Ticket;

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
