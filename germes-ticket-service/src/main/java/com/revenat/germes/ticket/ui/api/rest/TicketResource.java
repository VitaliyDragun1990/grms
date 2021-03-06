package com.revenat.germes.ticket.ui.api.rest;

import com.revenat.germes.common.core.shared.transform.Transformer;
import com.revenat.germes.ticket.core.application.TicketService;
import com.revenat.germes.ticket.core.application.TicketDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Vitaliy Dragun
 */
@RestController
@RequestMapping("tickets")
@RequiredArgsConstructor
public class TicketResource {

    private final TicketService ticketService;

    private final Transformer transformer;

    @GetMapping(path = "{tripId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TicketDTO> findTickets(@PathVariable final String tripId) {
        return ticketService.findTickets(tripId).stream()
                .map(ticket -> transformer.transform(ticket, TicketDTO.class))
                .collect(Collectors.toUnmodifiableList());
    }
}
