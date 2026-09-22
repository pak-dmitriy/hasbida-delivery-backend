package com.delivery.habsida.repository;

import com.delivery.habsida.entity.ProductModifierGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductModifierGroupRepository extends JpaRepository<ProductModifierGroup, Long> {
    Optional<ProductModifierGroup> findByProductIdAndModifierGroupId(Long productId, Long modifierGroupId);
    boolean existsByProductIdAndModifierGroupId(Long productId, Long modifierGroupId);
}
