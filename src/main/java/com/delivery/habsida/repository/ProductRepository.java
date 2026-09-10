package com.delivery.habsida.repository;

import com.delivery.habsida.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
