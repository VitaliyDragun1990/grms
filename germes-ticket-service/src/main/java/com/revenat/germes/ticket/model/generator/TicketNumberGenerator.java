package com.revenat.germes.ticket.model.generator;

import com.revenat.germes.infrastructure.helper.generator.text.RandomDigitStringGenerator;
import com.revenat.germes.ticket.model.entity.Ticket;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

/**
 * Serves as a factory to generate ticket numbers
 *
 * @author Vitaliy Dragun
 */
@Named
@Dependent
public class TicketNumberGenerator extends RandomDigitStringGenerator {

    public TicketNumberGenerator() {
        super(Ticket.TICKET_NUMBER_SIZE);
    }
}
