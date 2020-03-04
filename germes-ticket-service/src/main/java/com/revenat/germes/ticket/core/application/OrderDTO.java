package com.revenat.germes.ticket.core.application;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * @author Vitaliy Dragun
 */
@Getter
@Setter
public class OrderDTO {

    @Future
    private LocalDateTime dueDate;

    @NotBlank
    private String tripId;

    @Positive
    private int ticketId;

    @NotBlank
    private String clientName;

    @NotBlank
    private String clientPhone;

}
