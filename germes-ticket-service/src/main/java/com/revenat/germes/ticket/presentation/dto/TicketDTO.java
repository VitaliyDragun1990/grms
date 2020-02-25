package com.revenat.germes.ticket.presentation.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author Vitaliy Dragun
 */
@Getter
@Setter
public class TicketDTO {

    @NotBlank
    private String tripId;

    @NotBlank
    private String clientName;

    @NotBlank
    private String uid;
}
