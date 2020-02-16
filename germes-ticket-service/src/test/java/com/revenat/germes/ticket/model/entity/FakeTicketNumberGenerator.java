package com.revenat.germes.ticket.model.entity;

import com.revenat.germes.ticket.model.generator.TicketNumberGenerator;

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
