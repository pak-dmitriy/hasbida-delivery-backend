package com.delivery.habsida.dto;

import com.delivery.habsida.entity.OrderType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderCreateRequest(

        @NotNull(message = "Order type can not be empty")
        OrderType type,


        String customerNote,

        @NotNull(message = "CustomerID can not be empty")
        Long customerId,

        @NotNull(message = "Customer address ID can not be empty")
        Long customerAddressId,

        @NotEmpty(message = "Order items can not be empty")
        List<OrderItemRequest> items
) {
}
