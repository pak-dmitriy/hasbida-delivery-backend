package com.delivery.habsida.controller;

import com.delivery.habsida.dto.OrderCreateRequest;
import com.delivery.habsida.dto.OrderDTO;
import com.delivery.habsida.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("@storeAccessGuard.canAccessStore(authentication, #storeId)")
    @PostMapping("/stores/{storeId}/orders")
    public ResponseEntity<OrderDTO> createOrder(@PathVariable Long storeId,
                                                @RequestBody @Valid OrderCreateRequest orderCreateRequest) {
        OrderDTO created = orderService.createOrder(storeId, orderCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
