package com.delivery.habsida.entity;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public enum OrderStatus {
    CREATED,
    ACCEPTED,
    CANCELLED,
    REJECTED,
    IN_PROGRESS,
    COMPLETED;

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS;

    static {
        ALLOWED_TRANSITIONS = new EnumMap<>(OrderStatus.class);
        ALLOWED_TRANSITIONS.put(CREATED, Set.of(ACCEPTED, REJECTED, CANCELLED));
        ALLOWED_TRANSITIONS.put(ACCEPTED, Set.of(IN_PROGRESS));
        ALLOWED_TRANSITIONS.put(IN_PROGRESS, Set.of(COMPLETED));
    }

    public static boolean canTransition(OrderStatus from, OrderStatus to) {
        Set<OrderStatus> allowed = ALLOWED_TRANSITIONS.get(from);
        return allowed != null && allowed.contains(to);

    }
}
