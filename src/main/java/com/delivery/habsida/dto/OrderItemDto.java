package com.delivery.habsida.dto;

import com.delivery.habsida.entity.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public record OrderItemDto(
        Long id,
        Long orderId,
        String productName,
        BigDecimal productPrice,
        Integer quantity,
        BigDecimal subtotal,
        Integer discountPercent,
        BigDecimal discountAmount,
        List<OrderItemModifierDto> orderItemModifiers
        ) {

    public static OrderItemDto from(OrderItem orderItem, List<OrderItemModifierDto> orderItemModifiers) {
        return new OrderItemDto(
                orderItem.getId(),
                orderItem.getOrder().getId(),
                orderItem.getProductName(),
                orderItem.getProductPrice(),
                orderItem.getQuantity(),
                orderItem.getSubtotal(),
                orderItem.getDiscountPercent(),
                orderItem.getDiscountAmount(),
                orderItemModifiers
        );
    }
}
