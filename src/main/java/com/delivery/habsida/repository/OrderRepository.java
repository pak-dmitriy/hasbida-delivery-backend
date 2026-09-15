package com.delivery.habsida.repository;

import com.delivery.habsida.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    long countByStoreId(Long storeId);
}
