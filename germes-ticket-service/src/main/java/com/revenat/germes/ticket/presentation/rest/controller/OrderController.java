package com.revenat.germes.ticket.presentation.rest.controller;

import com.revenat.germes.common.core.shared.exception.flow.InvalidParameterException;
import com.revenat.germes.common.core.shared.exception.flow.ValidationException;
import com.revenat.germes.common.core.shared.transform.Transformer;
import com.revenat.germes.ticket.application.service.TicketService;
import com.revenat.germes.ticket.model.entity.Order;
import com.revenat.germes.ticket.presentation.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Vitaliy Dragun
 */
@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController {

    private final TicketService ticketService;

    private final Transformer transformer;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> create(@RequestBody @Valid OrderDTO order) {
        try {
            ticketService.makeReservation(transformer.untransform(order, Order.class));
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (InvalidParameterException e) {
            throw new ValidationException(e.getMessage(), e);
        }
    }
}
