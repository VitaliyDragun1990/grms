package com.revenat.germes.presentation.rest.dto.transport;

import com.revenat.germes.application.model.entity.travel.Ticket;
import com.revenat.germes.presentation.rest.dto.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * Stores ticket state to transfer it between client and server
 *
 * @author Vitaliy Dragun
 */
@Getter
@Setter
@ApiModel(description = "Trip ticket for the single passenger")
public class TicketDTO extends BaseDTO<Ticket> {

    @ApiModelProperty(value = "Identifier of the trip", required = true)
    @Positive
    private int tripId;

    @ApiModelProperty(value = "Client name", required = true)
    @Size(min = 2, max = 32)
    @NotBlank
    private String clientName;

    @ApiModelProperty(value = "Auto-generated ticket identifier", required = true)
    @Size(min = 2, max = 32)
    @NotBlank
    private String uid;
}
