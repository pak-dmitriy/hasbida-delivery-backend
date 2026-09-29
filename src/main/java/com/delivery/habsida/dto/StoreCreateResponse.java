package com.delivery.habsida.dto;

import com.delivery.habsida.entity.Status;
import com.delivery.habsida.entity.TypeStoreServices;

import java.math.BigDecimal;

public record StoreCreateResponse(
        Long id,
        String name,
        String storeSlug,
        String description,
        TypeStoreServices typeStoreServices,
        String phone,
        String logo,
        Status status,
        String pickupAddress,
        BigDecimal deliveryFee
) {}
