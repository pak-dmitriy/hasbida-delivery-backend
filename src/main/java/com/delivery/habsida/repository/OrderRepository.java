package com.delivery.habsida.repository;

import com.delivery.habsida.entity.Order;
import com.delivery.habsida.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    long countByStoreId(Long storeId);

    List<Order> findByStoreIdAndOrderStatus(Long storeId, OrderStatus orderStatus);
}
