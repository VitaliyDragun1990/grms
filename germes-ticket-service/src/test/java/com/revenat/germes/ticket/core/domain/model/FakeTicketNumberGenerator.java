package com.revenat.germes.ticket.core.domain.model;

import com.revenat.germes.ticket.core.domain.model.TicketNumberGenerator;

/**
 * @author Vitaliy Dragun
 */
public class FakeTicketNumberGenerator extends TicketNumberGenerator {
    private String number;

    public FakeTicketNumberGenerator(String number) {
        this.number = number;
    }

    @Override
    public String generate() {
        return number;
    }
}
