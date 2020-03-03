package com.revenat.germes.ticket.presentation.dto.transformable;

import com.revenat.germes.common.core.shared.transform.Transformable;
import com.revenat.germes.ticket.model.entity.Order;
import com.revenat.germes.ticket.model.entity.OrderState;
import com.revenat.germes.ticket.presentation.dto.OrderDTO;

import java.util.Map;

/**
 * @author Vitaliy Dragun
 */
public class OrderTransformable implements Transformable<Order, OrderDTO> {

    private final Map<String, String> domainMappings = Map.of("ticketId", "ticket");

    @Override
    public Order untransform(OrderDTO orderDTO, Order order) {
        if (order.getState() == null) {
            order.setState(OrderState.PENDING);
        }

        return order;
    }

    @Override
    public Map<String, String> getSourceMapping() {
        return domainMappings;
    }
}
