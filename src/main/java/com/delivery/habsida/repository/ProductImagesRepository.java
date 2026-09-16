package com.delivery.habsida.repository;

import com.delivery.habsida.entity.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImagesRepository extends JpaRepository<ProductImages, Long> {
    List<ProductImages> findByProductId(Long productId);
}
